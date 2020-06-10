package uk.co.solong.schematf.detection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;
import java.util.SortedMap;

public class SchemaIndex {
    private final SortedMap<Long, SchemaEntry> schemaEntries;

    public SortedMap<Long, SchemaEntry> getSchemaEntries() {
        return schemaEntries;
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public SchemaIndex(@JsonProperty("schemaEntries") SortedMap<Long, SchemaEntry> schemaEntries) {
        this.schemaEntries = schemaEntries;
    }

    @JsonIgnore
    public Optional<Long> getLatestSchemaPublicationTime() {
        if (schemaEntries.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(schemaEntries.lastKey());
        }
    }

}
