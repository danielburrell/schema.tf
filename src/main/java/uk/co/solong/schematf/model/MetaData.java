package uk.co.solong.schematf.model;

import com.fasterxml.jackson.databind.JsonNode;

public class MetaData {
    private Long observedDate;
    private String hashcode;

    public Long getObservedDate() {
        return observedDate;
    }

    public void setObservedDate(Long observedDate) {
        this.observedDate = observedDate;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }
    
    public MetaData() {
        // TODO Auto-generated constructor stub
    }
    public MetaData(JsonNode node) {
        observedDate = node.get("observedDate").asLong();
        hashcode = node.get("hashcode").asText();
    }
}
