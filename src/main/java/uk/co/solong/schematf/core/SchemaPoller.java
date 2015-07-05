package uk.co.solong.schematf.core;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.co.solong.steam4j.tf2.TF2Template;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class SchemaPoller {
    private final TF2Template tf2Template;
    private final SchemaDao schemaDao;
    private static final Logger logger = LoggerFactory.getLogger(SchemaPoller.class);

    /**
     * Every minute, compare the valve schema to our latest copy. Persist any
     * updates, or if we do not have the latest copy when we push, discard the
     * updates.
     * 
     * @throws ExecutionException
     */
    @Scheduled(fixedDelay = 60000)
    public void reconcileSchemas() throws ExecutionException {
        JsonNode valveSchema = tf2Template.getSchema().getRawData();
        JsonNode githubSchema = schemaDao.getLatestSchema();
        if (!valveSchema.equals(githubSchema)) {
            logger.info("Schema change detected");
            try {
                schemaDao.persist(valveSchema);
            } catch (CannotCommitException e) {
                logger.info("Another process committed. Discarding and resyncing");
                try {
                    schemaDao.discardAndResync();
                } catch (Throwable discardAndResyncError) {
                    logger.error("Some error has occured attempting to commit.", discardAndResyncError);
                }
            } catch (Throwable commitAndSyncError) {
                logger.error("Some error has occured attempting to commit.", commitAndSyncError);
            }
        }
    }

    public SchemaPoller(TF2Template tf2Template, SchemaDao schemaDao) {
        this.tf2Template = tf2Template;
        this.schemaDao = schemaDao;
    }

}
