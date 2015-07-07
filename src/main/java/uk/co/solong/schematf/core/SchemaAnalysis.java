package uk.co.solong.schematf.core;

import java.util.ArrayList;
import java.util.List;

public class SchemaAnalysis {
    private int newItems;
    private int newQualities;
    private List<Integer> newCrates = new ArrayList<>();
    private int modifiedItems;

    public int getNewItems() {
        return newItems;
    }

    public void setNewItems(int newItems) {
        this.newItems = newItems;
    }

    public int getNewQualities() {
        return newQualities;
    }

    public void setNewQualities(int newQualities) {
        this.newQualities = newQualities;
    }

    public List<Integer> getNewCrates() {
        return newCrates;
    }

    public void setNewCrates(List<Integer> newCrates) {
        this.newCrates = newCrates;
    }

    public int getModifiedItems() {
        return modifiedItems;
    }

    public void setModifiedItems(int modifiedItems) {
        this.modifiedItems = modifiedItems;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (newItems > 0) {
            sb.append(newItems).append(" new items").append("\n");
        }
        if (newQualities > 0) {
            sb.append(newQualities).append(" new qualities").append("\n");
        }
        if (newCrates.size() > 0) {
            sb.append("Crate# ").append(newCrates.toString()).append("\n");
        }
        if (modifiedItems > 0) {
            sb.append(modifiedItems).append(" modified Items").append("\n");
        }
        
        String status = sb.toString();
        if ("".equals(status)) {
            return "Schema changed silently";
        } else {
            return status;
        }

    }
}
