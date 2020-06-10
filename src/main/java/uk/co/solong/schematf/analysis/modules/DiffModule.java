package uk.co.solong.schematf.analysis.modules;

import com.fasterxml.jackson.databind.JsonNode;
import uk.co.solong.schematf.analysis.DifferenceReport;

import java.util.List;

public interface DiffModule {

    void calculateDiff(List<JsonNode> current, List<JsonNode> previous, DifferenceReport difference);

    String getName();
}
