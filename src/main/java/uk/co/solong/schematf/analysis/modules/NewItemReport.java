package uk.co.solong.schematf.analysis.modules;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.solong.schematf.analysis.DifferenceReport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NewItemReport implements DiffModule{
    private static final Logger logger = LoggerFactory.getLogger(NewItemReport.class);

    @Override
    public void calculateDiff(List<JsonNode> current, List<JsonNode> previous, DifferenceReport difference) {
        List<Long> allCurrentDefIndex = getAllDefIndex(current);
        List<Long> allPreviousDefIndex = getAllDefIndex(previous);
        Collection<Long> disjunction = CollectionUtils.disjunction(allCurrentDefIndex, allPreviousDefIndex);
        difference.setNewItems(disjunction);
    }

    private List<Long> getAllDefIndex(List<JsonNode> partialSchemaPages) {
        List<Long> result = new ArrayList<>();
        for (JsonNode partialSchema : partialSchemaPages) {
            if (partialSchema.has("result") && partialSchema.get("result").has("items")) {
                JsonNode itemsList = partialSchema.get("result").get("items");
                for (int i = 0; i < itemsList.size(); i++) {
                    JsonNode item = itemsList.get(i);
                    Long currentDefIndex = item.get("defindex").asLong();
                    result.add(currentDefIndex);
                }
            } else {
                if (!partialSchema.has("result")) {
                    logger.warn("Result node missing in schema");
                } else {
                    logger.warn("Items node missing in schema");
                }
            }
        }
        return result;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
