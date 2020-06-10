package uk.co.solong.schematf.detection;

public class SchemaChangeResult {
    private Long schemaId;
    private boolean schemaChanged;

    public boolean isSchemaChanged() {
        return schemaChanged;
    }

    public void setSchemaChanged(boolean schemaChanged) {
        this.schemaChanged = schemaChanged;
    }

    public Long getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(Long schemaId) {
        this.schemaId = schemaId;
    }
}
