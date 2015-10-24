package uk.co.solong.schematf.core.strategy;

import uk.co.solong.steam4j.tf2.data.schema.TF2Schema;

import com.fasterxml.jackson.databind.JsonNode;

public class ItemRawStrategy implements DerivativeDataLoader<JsonNode> {

    @Override
    public JsonNode deriveData(JsonNode schemaJson) {
        TF2Schema schema = new TF2Schema(schemaJson);
        return schema.getRawItems();
    }

}
