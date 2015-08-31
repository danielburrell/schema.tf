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
import uk.co.solong.schematf.core.persistence.SchemaDao;
import uk.co.solong.schematf.core.strategy.ItemStrategy;
import uk.co.solong.schematf.core.strategy.QualityStrategy;
import uk.co.solong.schematf.core.strategy.Strategy;
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
    private CacheLoader<Strategy, JsonNode> loader;
    private LoadingCache<Strategy, JsonNode> cache;

    private static final Strategy ITEM_KEY = new ItemStrategy();
    private static final Strategy QUALITY_KEY = new QualityStrategy();
    private static final Strategy SCHEMA_KEY = new SchemaStrategy();
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

    @Override
    public JsonNode getLatestSchema() {
        try {
            return cache.get(SCHEMA_KEY);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public DropboxSchemaDao(DropboxDao dropboxDao) {
        this.dropboxDao = dropboxDao;
        this.loader = new CacheLoader<Strategy, JsonNode>() {
            public JsonNode load(Strategy key) throws RuntimeException, JsonProcessingException, IOException, ExecutionException {
                if (key==SCHEMA_KEY) {
                    return loadDataFromDropbox();
                } else {
                    return key.execute(cache.get(SCHEMA_KEY));
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
    public JsonNode getItems() {
        try {
            return cache.get(ITEM_KEY);
        } catch (ExecutionException e) {
            return new POJONode(null);
        }
    }

    @Override
    public JsonNode getQualities() {
        try {
            return cache.get(QUALITY_KEY);
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
