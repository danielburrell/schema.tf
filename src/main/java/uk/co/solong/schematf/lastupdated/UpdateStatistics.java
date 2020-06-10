package uk.co.solong.schematf.lastupdated;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.solong.schematf.detection.SchemaDatabase;

import java.io.IOException;
import java.time.OffsetDateTime;

public class UpdateStatistics extends Statistics {

    private static final Logger logger = LoggerFactory.getLogger(UpdateStatistics.class);
    private final SchemaDatabase schemaDatabase;

    public UpdateStatistics(SchemaDatabase schemaDatabase) {
        this.schemaDatabase = schemaDatabase;
    }
    public JsonNode updateStatistics(JsonNode stateMachineData) {
        try {
            Statistics statistics = schemaDatabase.getStatistics();
            //1589307548000
            //checkForSchemaChangesResults.schemaId
            long schemaPublishTime = stateMachineData.path("checkForSchemaChangesResults").path("schemaId").asLong();
            if (schemaPublishTime > 0) {
                statistics.setLastChangeDetectedId(schemaPublishTime);
                //latency check
                JsonNode forceUpdate = stateMachineData.path("generateParametersResult").path("forceUpdate");
                if (forceUpdate.isBoolean() && !forceUpdate.asBoolean()) {
                    statistics.setLatency(System.currentTimeMillis() - schemaPublishTime);
                    logger.info("Updated latency");
                } else {
                    logger.info("Update was forced, skipping latency update");
                }
            }

            String time = stateMachineData.path("time").asText();
            if (!time.isBlank()) {
                //2020-06-10T18:58:01Z
                OffsetDateTime odt = OffsetDateTime.parse(time);
                long l1 = odt.toInstant().toEpochMilli();
                statistics.setLastWorkflowStartedTime(l1);
                JsonNode forceUpdate = stateMachineData.path("generateParametersResult").path("forceUpdate");
                if (forceUpdate.isBoolean() && forceUpdate.asBoolean()) {
                    statistics.setLastForceUpdate(l1);
                    logger.info("Updated last force time");
                } else {
                    logger.info("Update was not forced, skipping lastforceupdate update");
                }
            }
            schemaDatabase.persistStatistics(statistics);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stateMachineData;
    }

    public JsonNode handleUnauthorized(JsonNode jsonNode) {
        try {
            Statistics statistics = schemaDatabase.getStatistics();
            statistics.setLastForbidden(System.currentTimeMillis());
            schemaDatabase.persistStatistics(statistics);
            return jsonNode;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode handleSteamIntermittent(JsonNode jsonNode) {
        try {
            Statistics statistics = schemaDatabase.getStatistics();
            statistics.setLastIntermittent(System.currentTimeMillis());
            schemaDatabase.persistStatistics(statistics);
            return jsonNode;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
