package uk.co.solong.schematf.detection;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.solong.schematf.analysis.DifferenceReport;
import uk.co.solong.schematf.lastupdated.Statistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SchemaDatabase {

    private static final Logger logger = LoggerFactory.getLogger(SchemaDatabase.class);
    private final AmazonS3 s3Client;
    private final String bucketName;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SchemaDatabase(AmazonS3 s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public SchemaIndex getSchemaIndex() {
        try {
            S3Object indexFile = s3Client.getObject(bucketName, "metadata/index.json");
            S3ObjectInputStream indexFileInputStream = indexFile.getObjectContent();
            SchemaIndex schemaIndex = objectMapper.readValue(indexFileInputStream, SchemaIndex.class);
            return schemaIndex;
        } catch (IOException e) {
            throw new CouldNotRetrieveLatestSchemaPublicationTimeException(e);
        }
    }

    public Optional<Long> getLatestSchemaPublicationTime() {
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
                PutObjectResult putObjectResult = s3Client.putObject(bucketName, "data/" + id + "/" + id + "-" + i + ".json", objectMapper.writeValueAsString(schema.get(i)));
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

    public String persistDifferenceReport(DifferenceReport differenceReport) throws JsonProcessingException {
        String key = "diff/" + differenceReport.getCurrentId() + "-" + differenceReport.getPreviousId() + ".json";
        PutObjectResult putObjectResult = s3Client.putObject(bucketName, key, objectMapper.writeValueAsString(differenceReport));
        return key;
    }

    public List<JsonNode> getSchema(Long schemaId) throws IOException {
        List<JsonNode> multipartSchema = new ArrayList<>();
        int i = 0;
        while (true) {
            String objectName = "data/" + schemaId + "/" + schemaId + "-" + i + ".json";
            if (!s3Client.doesObjectExist(bucketName, objectName)) break;
            logger.info("Loading {}",objectName);
            S3Object indexFile = s3Client.getObject(bucketName, objectName);
            S3ObjectInputStream indexFileInputStream = indexFile.getObjectContent();
            JsonNode partialSchema = objectMapper.readTree(indexFileInputStream);
            multipartSchema.add(partialSchema);
            i++;
        }
        return multipartSchema;
    }

    public DifferenceReport loadReport(String pathToReport) throws IOException {
        if (!s3Client.doesObjectExist(bucketName, pathToReport)) {
            //FIXME eugh returning empty when you should throw an exception and handle it in the flow
            logger.warn("Attempted to read report that doesn't exist {}", pathToReport);
            return new DifferenceReport();
        } else {
            S3Object reportFile = s3Client.getObject(bucketName, pathToReport);
            S3ObjectInputStream indexFileInputStream = reportFile.getObjectContent();
            DifferenceReport partialSchema = objectMapper.readValue(indexFileInputStream, DifferenceReport.class);
            return partialSchema;
        }

    }

    public Statistics getStatistics() throws IOException {
        String fileName = "statistics/statistics.json";
        if (!s3Client.doesObjectExist(bucketName, fileName)) {
            logger.warn("Statistics doesn't exist. If this is the first time you're running this, ignore this warning");
            return new Statistics();
        } else {
            S3Object reportFile = s3Client.getObject(bucketName, fileName);
            S3ObjectInputStream indexFileInputStream = reportFile.getObjectContent();
            Statistics statistics = objectMapper.readValue(indexFileInputStream, Statistics.class);
            return statistics;
        }
    }

    public void persistStatistics(Statistics statistics) throws JsonProcessingException {
        String key = "statistics/statistics.json";
        PutObjectResult putObjectResult = s3Client.putObject(bucketName, key, objectMapper.writeValueAsString(statistics));
    }
}
