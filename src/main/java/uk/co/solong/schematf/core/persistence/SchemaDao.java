package uk.co.solong.schematf.core.persistence;

import java.util.concurrent.ExecutionException;

import uk.co.solong.schematf.core.strategy.DerivativeDataLoader;
import uk.co.solong.schematf.model.MetaData;

import com.fasterxml.jackson.databind.JsonNode;

public interface SchemaDao {
    
    public void persist(JsonNode schema);

    public boolean exists(String hashCode);

    void persist(JsonNode schema, MetaData metadata);

    public JsonNode getFromDataCache(DerivativeDataLoader<JsonNode> ITEM_KEY) throws ExecutionException;
    
}
