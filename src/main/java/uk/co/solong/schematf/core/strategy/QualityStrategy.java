package uk.co.solong.schematf.core.strategy;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.solong.steam4j.tf2.data.schema.TF2Schema;
import uk.co.solong.steam4j.tf2.data.schema.TF2SchemaQuality;

public class QualityStrategy implements Strategy {

    @Override
    public JsonNode execute(JsonNode schemaJson) {
        TF2Schema schema = new TF2Schema(schemaJson);
        ObjectMapper m = new ObjectMapper();
        Map<Integer, TF2SchemaQuality> map = schema.getCompleteQualityMap();
        return m.valueToTree(map);
    }

}
