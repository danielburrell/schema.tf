package uk.co.solong.schematf.core.persistence;

import uk.co.solong.schematf.model.MetaData;

import com.fasterxml.jackson.databind.JsonNode;

public interface SchemaDao {
    
    public void persist(JsonNode schema);

    public JsonNode getLatestSchema();

    public JsonNode getItems();

    public JsonNode getQualities();

    public boolean exists(String hashCode);

    void persist(JsonNode schema, MetaData metadata);
    
}
