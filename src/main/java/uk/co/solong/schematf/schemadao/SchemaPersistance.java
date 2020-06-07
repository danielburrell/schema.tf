package uk.co.solong.schematf.schemadao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class SchemaPersistance {

    private static final Logger logger = LoggerFactory.getLogger(SchemaPersistance.class);
    private final AmazonS3 s3Client;
    private final String bucketName;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SchemaPersistance(AmazonS3 s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public Long getLatestSchemaPublicationTime(Long x) {
        try {
            S3Object indexFile = s3Client.getObject(bucketName, "metadata/index.json");
            S3ObjectInputStream indexFileInputStream = indexFile.getObjectContent();
            SchemaIndex schemaIndex = objectMapper.readValue(indexFileInputStream, SchemaIndex.class);
            return schemaIndex.getLatestSchemaPublicationTime();
        } catch (IOException e) {
            throw new CouldNotRetrieveLatestSchemaPublicationTimeException(e);
        }
    }

    public void persistSchema(Long id, List<JsonNode> schema) {
        try {
            for (int i = 0; i < schema.size(); i++) {
                logger.info("Persisting {}", i);
                PutObjectResult putObjectResult = s3Client.putObject(bucketName, "data/" + id + "/" + id + "-" + i + ".json", objectMapper.writeValueAsString(schema));
            }

            logger.info("Updating metadata index");
            S3Object indexFile = s3Client.getObject(bucketName, "metadata/index.json");
            S3ObjectInputStream indexFileInputStream = indexFile.getObjectContent();
            SchemaIndex schemaIndex = objectMapper.readValue(indexFileInputStream, SchemaIndex.class);
            schemaIndex.getSchemaEntries().put(id, new SchemaEntry(id, "data/" + id, id, Source.SCHEMATF, true));
            PutObjectResult putObjectResult = s3Client.putObject(bucketName, "metadata/index.json", objectMapper.writeValueAsString(schemaIndex));
            logger.info("Done updating index");

        } catch (IOException e) {
            throw new CouldNotRetrieveLatestSchemaPublicationTimeException(e);
        }
        logger.info("Done persisting");
    }
}
