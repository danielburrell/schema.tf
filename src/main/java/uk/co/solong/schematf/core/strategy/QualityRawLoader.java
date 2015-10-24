package uk.co.solong.schematf.core.strategy;

import java.util.Map;

import uk.co.solong.steam4j.tf2.data.schema.TF2Schema;
import uk.co.solong.steam4j.tf2.data.schema.TF2SchemaQuality;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QualityRawLoader implements DerivativeDataLoader<JsonNode> {

    private ObjectMapper m = new ObjectMapper();
    
    @Override
    public JsonNode deriveData(JsonNode schemaJson) {
        TF2Schema schema = new TF2Schema(schemaJson);
        Map<Integer, TF2SchemaQuality> map = schema.getCompleteQualityMap();
        return m.valueToTree(map);
    }

}
