package uk.co.solong.schematf.core;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.co.solong.schematf.core.analysis.SchemaAnalysis;
import uk.co.solong.schematf.core.analysis.SchemaChangeAnalyser;
import uk.co.solong.schematf.core.notification.NotificationDao;
import uk.co.solong.schematf.core.persistence.SchemaDao;
import uk.co.solong.steam4j.tf2.TF2Template;
import uk.co.solong.steam4j.tf2.data.schema.TF2Schema;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class SchemaPoller {
    private final TF2Template tf2Template;
    private final SchemaDao schemaDao;
    private final NotificationDao notificationDao;
    private static final Logger logger = LoggerFactory.getLogger(SchemaPoller.class);
    private final SchemaChangeAnalyser schemaChangeAnalyser = new SchemaChangeAnalyser();
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
        if (!valveSchema.equals(githubSchema) || true) {
            logger.info("Schema change detected");
            try {
                schemaDao.persist(valveSchema);
                SchemaAnalysis schemaAnalysis = schemaChangeAnalyser.schemaChangeAnalyser(new TF2Schema(githubSchema), new TF2Schema(valveSchema));
                notificationDao.notifySchemaChange(schemaAnalysis);
            } catch (Throwable commitAndSyncError) {
                logger.error("Some error has occured attempting to persist/notify new changes.", commitAndSyncError);
            }
        }
    }

    public SchemaPoller(TF2Template tf2Template, SchemaDao schemaDao, NotificationDao notificationDao) {
        this.tf2Template = tf2Template;
        this.schemaDao = schemaDao;
        this.notificationDao = notificationDao;
    }

}
