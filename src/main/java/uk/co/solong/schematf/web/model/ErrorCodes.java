package uk.co.solong.schematf.web.model;

public enum ErrorCodes {
    SCHEMA_LOOKUP_FAILED(1);
    private final int id;

    private ErrorCodes(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
