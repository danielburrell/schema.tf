package uk.co.solong.schematf.core.persistence.dropbox;

import uk.co.solong.schematf.core.strategy.DerivativeDataLoader;

import com.fasterxml.jackson.databind.JsonNode;

public class SchemaStrategy implements DerivativeDataLoader<JsonNode> {

    @Override
    public JsonNode deriveData(JsonNode schema) {
        return schema;
    }

}
