package uk.co.solong.schematf.schemadao;

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
    private final SchemaPersistance schemaPersistance;
    private final TwitterPoster twitterPosted;

    public SchemaFetcher(SchemaPersistance schemaPersistance, TF2Template tf2Template, TwitterPoster twitterPoster) {
        this.schemaPersistance = schemaPersistance;
        this.tf2Template = tf2Template;
        this.twitterPosted = twitterPoster;
    }

    public boolean checkNewSchema(Long dummy) {
        Long anotherDummy = 1L;
        Long latestSchemaPublicationTime = schemaPersistance.getLatestSchemaPublicationTime(anotherDummy);
        Optional<ResponseData> newSchema = tf2Template.getNewSchema(latestSchemaPublicationTime);
        if (newSchema.isPresent()) {
            logger.info("New schema detected. Fetching outstanding pages");
            List<JsonNode> otherPages = tf2Template.getMoreSchemaData(newSchema.get().getData().get("result").get("next").asLong());

            List<JsonNode> fullSchema = new ArrayList<>(otherPages.size() + 1);
            fullSchema.add(newSchema.get().getData());
            fullSchema.addAll(otherPages);
            schemaPersistance.persistSchema(newSchema.get().getLastModified(), fullSchema);
            twitterPosted.createTweet("Looks like something changed");

            //write the schema to S3 using the schemaDAO
            //write a record to the schemaMetadata
            //return true to indicate new schema. and then aws lambda can trigger analysis
            logger.info("All done");
            return true;
        } else {
            logger.info("No schema changes detected");
            return false;
        }
    }
}
