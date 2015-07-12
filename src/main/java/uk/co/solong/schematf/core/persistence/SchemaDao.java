package uk.co.solong.schematf.core.persistence;

import com.fasterxml.jackson.databind.JsonNode;

public interface SchemaDao {
    
    public void persist(JsonNode schema);

    public JsonNode getLatestSchema();
    
}
