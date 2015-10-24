package uk.co.solong.schematf.core.strategy;

import java.util.HashMap;
import java.util.Map;

import uk.co.solong.steam4j.tf2.data.schema.TF2Schema;
import uk.co.solong.steam4j.tf2.data.schema.TF2SchemaQuality;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class QualitySimpleLoader implements DerivativeDataLoader<JsonNode> {

    private final ObjectMapper m = new ObjectMapper();
    private final Map<Integer, String> colorMap;
    
    /**
     * Extract the schema, extract the set of qualities from that, then apply the colours to that.
     */
    @Override
    public JsonNode deriveData(JsonNode schemaJson) {
        TF2Schema schema = new TF2Schema(schemaJson);
        Map<Integer, TF2SchemaQuality> originalMap = schema.getCompleteQualityMap();
        JsonNode rawNode = m.valueToTree(originalMap);
        JsonNode rawNodeCopy = rawNode.deepCopy();
        for (JsonNode quality : rawNodeCopy) {
            ObjectNode n = (ObjectNode) quality;
            int id = n.get("id").asInt(-1);
            String color = colorMap.get(id);
            if (color == null) {
                color = "000000";
            }
            n.put("color", color);
            //TODO insert active or not based on capital letter + number n.put("active", colorMap.get(n.get("id").asInt()));
        }
        return rawNodeCopy;
    }

    public QualitySimpleLoader() {
        this.colorMap = new HashMap<>();
        this.colorMap.put(-1, "000000"); //default failure
        this.colorMap.put(0, "B2B2B2"); //normal
        this.colorMap.put(1, "4D7455"); //genuine
        this.colorMap.put(2, "000000"); //rarity2
        this.colorMap.put(3, "476291"); //vintage
        this.colorMap.put(4, "000000"); //rarity3
        this.colorMap.put(5, "8650AC"); //unusual
        this.colorMap.put(6, "FFD700"); //unique
        this.colorMap.put(7, "70B04A"); //community
        this.colorMap.put(8, "A50F79"); //valve
        this.colorMap.put(9, "70B04A"); //selfmade
        this.colorMap.put(10, "000000"); //customized
        this.colorMap.put(11, "CF6A32"); //strange
        this.colorMap.put(12, "000000"); //completed
        this.colorMap.put(13, "38F3AB"); //haunted
        this.colorMap.put(14, "AA0000"); //collectors
        this.colorMap.put(15, "FAFAFA"); //decorated
    }
}
