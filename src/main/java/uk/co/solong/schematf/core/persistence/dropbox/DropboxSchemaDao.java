package uk.co.solong.schematf.core.persistence.dropbox;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.DatatypeConverter;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.solong.schematf.core.persistence.SchemaDao;
import uk.co.solong.schematf.model.MetaData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class DropboxSchemaDao implements SchemaDao {

    private final DropboxDao dropboxDao;
    private final ObjectMapper m = new ObjectMapper();
    private CacheLoader<String, JsonNode> loader;
    private LoadingCache<String, JsonNode> cache;
    private static final Logger logger = LoggerFactory.getLogger(DropboxSchemaDao.class);
    @Override
    public void persist(JsonNode schema) {
        try {
            String hashCode = getHashCode(schema);
            String schemaFileName = generateSchemaFilename(schema, hashCode);
            String metadataFileName = generateMetadataFilename(schema, hashCode);

            // create meta data
            MetaData metadata = new MetaData();
            metadata.setObservedDate(new DateTime().getMillis());
            metadata.setHashcode(hashCode);

            dropboxDao.uploadFile(schema, schemaFileName); // has to come first
            dropboxDao.uploadFile(m.valueToTree(metadata), metadataFileName);
            cache.invalidateAll();
        } catch (Throwable e) {

        }

    }

    private String getHashCode(JsonNode schema) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(schema.toString().getBytes());
            return DatatypeConverter.printHexBinary(thedigest);
            //return new String(thedigest, "UTF-8");
        } catch (NoSuchAlgorithmException   e) {
            throw new RuntimeException(e);
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
            return cache.get("LATEST");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public DropboxSchemaDao(DropboxDao dropboxDao) {
        this.dropboxDao = dropboxDao;
        this.loader = new CacheLoader<String, JsonNode>() {
            public JsonNode load(String key) throws RuntimeException, JsonProcessingException, IOException {
                return loadDataFromDropbox(key);
            }
        };
        this.cache = CacheBuilder.newBuilder().build(loader);
    }

    protected JsonNode loadDataFromDropbox(String key) {
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

    /*
    */
}
