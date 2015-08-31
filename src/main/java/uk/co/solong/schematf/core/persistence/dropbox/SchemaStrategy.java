package uk.co.solong.schematf.core.persistence.dropbox;

import uk.co.solong.schematf.core.strategy.Strategy;

import com.fasterxml.jackson.databind.JsonNode;

public class SchemaStrategy implements Strategy {

    @Override
    public JsonNode execute(JsonNode schema) {
        return schema;
    }

}
