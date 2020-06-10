package uk.co.solong.schematf.analysis;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.solong.schematf.detection.SchemaEntry;
import uk.co.solong.schematf.detection.SchemaIndex;
import uk.co.solong.schematf.detection.SchemaDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;

public class ChangeCalculator {

    private final DiffManager diffManager;
    private final SchemaDatabase schemaDatabase;
    private final static Logger logger = LoggerFactory.getLogger(ChangeCalculator.class);

    public ChangeCalculator(DiffManager diffManager, SchemaDatabase schemaDatabase) {
        this.diffManager = diffManager;
        this.schemaDatabase = schemaDatabase;
    }

    public String generateChanges(DiffRequest diffRequest) {
        //FIXME validate that the diffRequest is valid. (needs at least currentSchemaId)
        try {
            Long currentSchemaId = diffRequest.getCurrent();

            Optional<Long> previousSchemaId = Optional.ofNullable(diffRequest.getPrevious())
                    .or(() -> calculatePreviousSchemaId(diffRequest.getCurrent()));

            //if you could derive it, then...
            if (previousSchemaId.isPresent()) {
                Long previousSchema = previousSchemaId.get();
                logger.info("Fetching {} and {} ", currentSchemaId, previousSchema);
                List<JsonNode> current = schemaDatabase.getSchema(currentSchemaId);
                List<JsonNode> previous = schemaDatabase.getSchema(previousSchema);
                logger.info("Comparing {} and {} ", currentSchemaId, previousSchema);
                DifferenceReport differenceReport = diffManager.calculateDiff(current, previous);
                differenceReport.setCurrentId(currentSchemaId);
                differenceReport.setPreviousId(previousSchema);
                String reportKey = schemaDatabase.persistDifferenceReport(differenceReport);
                return reportKey;
            } else {
                logger.info("No prior schema. Skipping delta generation");
                //FIXME eugh empty string
                return "";
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Long> calculatePreviousSchemaId(Long currentSchemaId) {
        logger.info("Finding schema prior to {}", currentSchemaId);
        SchemaIndex schemaIndex = schemaDatabase.getSchemaIndex();
        SortedMap<Long, SchemaEntry> schemaEntries = schemaIndex.getSchemaEntries();
        if (schemaEntries.isEmpty()) {
            return Optional.empty();
        }
        // [1,2,3,4,5,6],7
        //            ^ LastKey of headmap
        SortedMap<Long, SchemaEntry> longSchemaEntrySortedMap = schemaEntries.headMap(currentSchemaId);
        if (longSchemaEntrySortedMap.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(longSchemaEntrySortedMap.lastKey());
        }
    }
}
