package uk.co.solong.schematf.core.persistence.dropbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.solong.schematf.core.HashCodeGenerator;
import uk.co.solong.schematf.core.cachekeys.Keys;
import uk.co.solong.schematf.core.persistence.SchemaDao;
import uk.co.solong.schematf.core.strategy.DerivativeDataLoader;
import uk.co.solong.schematf.model.MetaData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class DropboxSchemaDao implements SchemaDao {

    private final DropboxDao dropboxDao;
    private final ObjectMapper m = new ObjectMapper();
    private CacheLoader<DerivativeDataLoader, JsonNode> loader;
    private LoadingCache<DerivativeDataLoader, JsonNode> cache;

    private final HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
    private static final Logger logger = LoggerFactory.getLogger(DropboxSchemaDao.class);

    @Override
    public void persist(JsonNode schema) {
        try {
            String hashCode = hashCodeGenerator.getHashCode(schema);

            // create meta data
            MetaData metadata = new MetaData();
            metadata.setObservedDate(new DateTime().getMillis());
            metadata.setHashcode(hashCode);
            persist(schema, metadata);
        } catch (Throwable e) {

        }

    }
    
    @Override
    public void persist(JsonNode schema, MetaData metadata) {
        try {
            String schemaFileName = generateSchemaFilename(schema, metadata.getHashcode());
            String metadataFileName = generateMetadataFilename(schema, metadata.getHashcode());

            dropboxDao.uploadFile(schema, schemaFileName); // has to come first
            dropboxDao.uploadFile(m.valueToTree(metadata), metadataFileName);
            cache.invalidateAll();
        } catch (Throwable e) {

        }

    }

    

    private String generateMetadataFilename(JsonNode schema, String hashCode) {
        return hashCode + ".metadata";
    }

    private String generateSchemaFilename(JsonNode schema, String hashCode) {
        return hashCode + ".schema";
    }

    @Override @Deprecated
    public JsonNode getLatestSchema() {
        try {
            return cache.get(Keys.SCHEMA_KEY);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public DropboxSchemaDao(DropboxDao dropboxDao) {
        this.dropboxDao = dropboxDao;
        this.loader = new CacheLoader<DerivativeDataLoader, JsonNode>() {
            public JsonNode load(DerivativeDataLoader key) throws RuntimeException, JsonProcessingException, IOException, ExecutionException {
                //if the request is the schema key then execute a load from dropbox.
                if (key==Keys.SCHEMA_KEY) {
                    return loadDataFromDropbox();
                } else {
                    //otherwise the data to be loaded will be derived via a strategy applied to the schema (pulled from the cache).
                    return key.deriveData(cache.get(Keys.SCHEMA_KEY));
                }
            }
        };
        this.cache = CacheBuilder.newBuilder().build(loader);
    }

    protected JsonNode loadDataFromDropbox() {
        logger.info("Start cache load");
        JsonNode directoryContents = dropboxDao.ls("");
        JsonNode contents = directoryContents.get("contents");
        List<String> paths = new ArrayList<String>();
        for (JsonNode content : contents) {
            String path = content.get("path").asText("");
            if (path.endsWith("metadata")) {
                paths.add(path);
            }
        }
        NavigableMap<Long, String> results = new TreeMap<Long, String>();
        for (String path : paths) {
            JsonNode node = dropboxDao.getFile(path);
            MetaData metadata = new MetaData(node);
            results.put(metadata.getObservedDate(), metadata.getHashcode());
            // JsonNode node = dropboxDao.getFile(path);
            // node.
        }
        Entry<Long, String> entry = results.lastEntry();
        String fileName = "/" + entry.getValue() + ".schema";
        JsonNode latestSchema = dropboxDao.getFile(fileName);
        logger.info("End cache load");
        return latestSchema;
    }

    @Override
    public JsonNode getFromDataCache(DerivativeDataLoader<JsonNode> ITEM_KEY) throws ExecutionException {
        return cache.get(ITEM_KEY);
    }
    
    @Override @Deprecated
    public JsonNode getItems() {
        try {
            return cache.get(Keys.ITEM_RAW_KEY);
        } catch (ExecutionException e) {
            return new POJONode(null);
        }
    }

    @Override @Deprecated
    public JsonNode getQualities() {
        try {
            return cache.get(Keys.QUALITY_KEY);
        } catch (ExecutionException e) {
            return new POJONode(null);
        }
    }

    @Override
    public boolean exists(String hashCode) {
        JsonNode directoryContents = dropboxDao.ls("");
        JsonNode contents = directoryContents.get("contents");
        for (JsonNode content : contents) {
            String path = content.get("path").asText("");
            if (path.contains(hashCode)) {
                return true;
            }
        }
        return false;
    }
    /*
    */
}
