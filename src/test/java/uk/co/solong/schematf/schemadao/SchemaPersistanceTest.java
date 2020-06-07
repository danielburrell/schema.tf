package uk.co.solong.schematf.schemadao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.SortedMap;
import java.util.TreeMap;

class SchemaPersistanceTest {

    @Test
    public void canSerializedDeserialize() throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        SortedMap<Long, SchemaEntry> schemaEntries = new TreeMap<>();

        SchemaIndex s = new SchemaIndex(schemaEntries);
        String x = m.writeValueAsString(s);
        m.readValue(x, SchemaIndex.class);

    }
}