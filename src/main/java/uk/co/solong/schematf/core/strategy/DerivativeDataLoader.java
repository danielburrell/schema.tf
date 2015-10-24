package uk.co.solong.schematf.core.strategy;

import com.fasterxml.jackson.databind.JsonNode;

public interface DerivativeDataLoader<T> {

    public T deriveData(JsonNode schema);
}
