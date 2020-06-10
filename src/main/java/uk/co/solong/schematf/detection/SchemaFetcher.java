package uk.co.solong.schematf.detection;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.solong.schematf.twitter.TwitterPoster;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SchemaFetcher {
    private final Logger logger = LoggerFactory.getLogger(SchemaFetcher.class);

    private final TF2Template tf2Template;
    private final SchemaDatabase schemaDatabase;

    public SchemaFetcher(SchemaDatabase schemaDatabase, TF2Template tf2Template, TwitterPoster twitterPoster) {
        this.schemaDatabase = schemaDatabase;
        this.tf2Template = tf2Template;
    }

    public SchemaChangeResult checkNewSchema(Boolean forceUpdate) {
        //if forceUpdate, then set the latestSchemaPublicationTime as 0.
        Optional<Long> latestSchemaPublicationTime = forceUpdate ? Optional.empty() : schemaDatabase.getLatestSchemaPublicationTime();

        Optional<ResponseData> newSchema = tf2Template.getNewSchema(latestSchemaPublicationTime);
        if (newSchema.isPresent()) {
            logger.info("New schema detected. Fetching outstanding pages");
            List<JsonNode> otherPages = tf2Template.getMoreSchemaData(newSchema.get().getData().get("result").get("next").asLong());

            List<JsonNode> fullSchema = new ArrayList<>(otherPages.size() + 1);
            fullSchema.add(newSchema.get().getData());
            fullSchema.addAll(otherPages);
            schemaDatabase.persistSchema(newSchema.get().getLastModified(), fullSchema);

            //write the schema to S3 using the schemaDAO
            //write a record to the schemaMetadata
            //return true to indicate new schema. and then aws lambda can trigger analysis
            logger.info("All done");
            SchemaChangeResult schemaChangeResult = new SchemaChangeResult();
            schemaChangeResult.setSchemaChanged(true);
            schemaChangeResult.setSchemaId(newSchema.get().getLastModified());
            return schemaChangeResult;
        } else {
            logger.info("No schema changes detected");
            SchemaChangeResult schemaChangeResult = new SchemaChangeResult();
            schemaChangeResult.setSchemaChanged(false);
            return schemaChangeResult;
        }
    }
}
