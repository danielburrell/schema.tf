package uk.co.solong.schematf.analysis;

import java.util.Collection;

public class DifferenceReport {
    private Long currentId;
    private Long previousId;
    private Collection<Long> newItems;
    private Collection<Long> modifiedItems;
    private String shortSummary = "";

    public Long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Long currentId) {
        this.currentId = currentId;
    }

    public Long getPreviousId() {
        return previousId;
    }

    public void setPreviousId(Long previousId) {
        this.previousId = previousId;
    }

    public Collection<Long> getNewItems() {
        return newItems;
    }

    public void setNewItems(Collection<Long> newItems) {
        this.newItems = newItems;
    }

    public Collection<Long> getModifiedItems() {
        return modifiedItems;
    }

    public void setModifiedItems(Collection<Long> modifiedItems) {
        this.modifiedItems = modifiedItems;
    }

    public String getShortSummary() {
        return shortSummary;
    }

    public void setShortSummary(String shortSummary) {
        this.shortSummary = shortSummary;
    }
}
