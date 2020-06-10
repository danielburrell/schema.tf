package uk.co.solong.schematf.analysis;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.solong.schematf.analysis.modules.DiffModule;

import java.util.List;

public class DiffManager {
    private static final Logger logger = LoggerFactory.getLogger(DiffManager.class);
    private final List<DiffModule> diffModules;

    public DiffManager(List<DiffModule> diffModules) {
        this.diffModules = diffModules;
    }

    public DifferenceReport calculateDiff(List<JsonNode> current, List<JsonNode> previous) {
        logger.info("Running {} diff reports", diffModules.size());

        DifferenceReport differenceReport = new DifferenceReport();
        for (DiffModule currentModule : diffModules) {
            logger.info("Running {} report", currentModule.getName());
            currentModule.calculateDiff(current, previous, differenceReport);
        }
        return differenceReport;
    }
}
