package uk.co.solong.schematf.core.strategy;

import uk.co.solong.steam4j.tf2.data.schema.TF2Schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ItemSimpleStrategy implements DerivativeDataLoader<JsonNode> {

    @Override
    public JsonNode deriveData(JsonNode schemaJson) {
        TF2Schema schema = new TF2Schema(schemaJson);
        JsonNode items = schema.getRawItems().deepCopy();
        for (JsonNode item : items) {
            ObjectNode n = (ObjectNode) item;
            n.put("image_url", n.get("image_url").asText().replaceFirst("http://media.steampowered.com/", "https://steamcdn-a.akamaihd.net/"));
        }
        return items;
    }

}
