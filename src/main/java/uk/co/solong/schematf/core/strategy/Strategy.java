package uk.co.solong.schematf.core.strategy;

import com.fasterxml.jackson.databind.JsonNode;

public interface Strategy {

    public JsonNode execute(JsonNode schema);
}
