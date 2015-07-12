package uk.co.solong.schematf.core.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import uk.co.solong.steam4j.tf2.data.schema.TF2Schema;
import uk.co.solong.steam4j.tf2.data.schema.TF2SchemaItem;
import uk.co.solong.steam4j.tf2.data.schema.TF2SchemaQuality;

import com.fasterxml.jackson.databind.JsonNode;

public class SchemaChangeAnalyser {
    public SchemaAnalysis schemaChangeAnalyser(TF2Schema schemaT1, TF2Schema schemaT2) {

        List<TF2SchemaItem> items1 = schemaT1.getItems();
        List<TF2SchemaItem> items2 = schemaT2.getItems();

        // count the rawqualities
        Map<Integer, TF2SchemaQuality> j1Qualities = schemaT1.getCompleteQualityMap();
        Map<Integer, TF2SchemaQuality> j2Qualities = schemaT2.getCompleteQualityMap();

        SchemaAnalysis schemaAnalysis = new SchemaAnalysis();
        schemaAnalysis.setNewItems(calculateNewItems(items1, items2));
        schemaAnalysis.setNewQualities(calculateNewQualities(j1Qualities, j2Qualities));
        schemaAnalysis.setModifiedItems(calculcateModifiedItems(items1, items2));
        schemaAnalysis.setNewCrates(calculateNewCrates(items1,items2));
        // schemaAnalysis.set
        // need to set more data here
        return schemaAnalysis;
    }

    private List<Integer> calculateNewCrates(List<TF2SchemaItem> items1, List<TF2SchemaItem> items2) {
        Set<TF2SchemaItem> items2Crates = items2.stream().filter(i -> i.isCrate()).collect(Collectors.toSet());
        Set<TF2SchemaItem> items1Crates = items1.stream().filter(i -> i.isCrate()).collect(Collectors.toSet());
        
        Set<Integer> setOfAllCrates2 = items2Crates.stream().map(i -> i.getCrateSeries()).collect(Collectors.toSet());
        Set<Integer> setOfAllCrates1 = items1Crates.stream().map(i -> i.getCrateSeries()).collect(Collectors.toSet());
        Collection<Integer> disjunction = CollectionUtils.disjunction(setOfAllCrates2, setOfAllCrates1);

        return new ArrayList<Integer>(disjunction);
    }

    private int calculcateModifiedItems(List<TF2SchemaItem> items1, List<TF2SchemaItem> items2) {

        Set<Long> setOfAllItem2DefIndex = items2.stream().map(i -> i.getDefIndex()).collect(Collectors.toSet());
        Set<Long> setOfAllItem1DefIndex = items1.stream().map(i -> i.getDefIndex()).collect(Collectors.toSet());
        Collection<Long> commonDefIndexSet = CollectionUtils.intersection(setOfAllItem2DefIndex, setOfAllItem1DefIndex);

        List<TF2SchemaItem> resultSet1 = items1.stream().filter(i -> commonDefIndexSet.contains(i.getDefIndex())).collect(Collectors.toList());
        List<TF2SchemaItem> resultSet2 = items2.stream().filter(i -> commonDefIndexSet.contains(i.getDefIndex())).collect(Collectors.toList());

        List<JsonNode> raw1 = resultSet1.stream().map(i -> i.getRawData()).collect(Collectors.toList());
        List<JsonNode> raw2 = resultSet2.stream().map(i -> i.getRawData()).collect(Collectors.toList());
        
        Collection<JsonNode> unchangedItems = CollectionUtils.intersection(raw1, raw2);
        int unchangedItemCount =  raw1.size()-unchangedItems.size();
        return unchangedItemCount;

    }

    private int calculateNewQualities(Map<Integer, TF2SchemaQuality> j1Qualities, Map<Integer, TF2SchemaQuality> j2Qualities) {
        return j2Qualities.size() - j1Qualities.size();
    }

    private int calculateNewItems(List<TF2SchemaItem> items1, List<TF2SchemaItem> items2) {
        return items2.size() - items1.size();
    }
}
