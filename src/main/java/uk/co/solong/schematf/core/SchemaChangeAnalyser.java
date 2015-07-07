package uk.co.solong.schematf.core;

import com.fasterxml.jackson.databind.JsonNode;

import uk.co.solong.steam4j.tf2.data.schema.TF2Schema;

public class SchemaChangeAnalyser {
    public SchemaAnalysis schemaChangeAnalyser(TF2Schema schemaT1, TF2Schema schemaT2) {
        
        //count the items
        JsonNode j2Items = schemaT2.getRawItems();
        JsonNode j1Items = schemaT1.getRawItems();
        
        //count the rawqualities
        JsonNode j2Qualities = schemaT2.getRawQualities();
        JsonNode j1Qualities = schemaT1.getRawQualities();
        
        SchemaAnalysis schemaAnalysis = new SchemaAnalysis();
        schemaAnalysis.setNewItems(j2Items.size()-j1Items.size());
        schemaAnalysis.setNewQualities(j2Qualities.size()-j1Qualities.size());
        //need to set more data here
        return schemaAnalysis;
    }
}
