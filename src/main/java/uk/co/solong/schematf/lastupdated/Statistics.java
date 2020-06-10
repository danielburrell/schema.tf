package uk.co.solong.schematf.lastupdated;

public class Statistics {
    //this can be the time that the event is emitted. this is the 'checked time' as it occurs
    //for all paths, and isn't published till the end.
    private Long lastWorkflowStartedTime;
    //this only happens on the main route on success
    private Long lastChangeDetectedId;
    //this only happens on the intermittent route
    private Long lastIntermittent;
    //this only happens on the forbidden route
    private Long lastForbidden;
    //FIXME a measure of latency. diff between now and the schema id.
    //FIXME beware the timezone difference, and do not update when force==true
    //FIXME as backdated latencies are meaningless.
    private Long latency;

    //TODO
    private Long lastForceUpdate;

    public Long getLastChangeDetectedId() {
        return lastChangeDetectedId;
    }

    public void setLastChangeDetectedId(Long lastChangeDetectedId) {
        this.lastChangeDetectedId = lastChangeDetectedId;
    }

    public Long getLastIntermittent() {
        return lastIntermittent;
    }

    public void setLastIntermittent(Long lastIntermittent) {
        this.lastIntermittent = lastIntermittent;
    }

    public Long getLastForbidden() {
        return lastForbidden;
    }

    public void setLastForbidden(Long lastForbidden) {
        this.lastForbidden = lastForbidden;
    }

    public Long getLastWorkflowStartedTime() {
        return lastWorkflowStartedTime;
    }

    public void setLastWorkflowStartedTime(Long lastWorkflowStartedTime) {
        this.lastWorkflowStartedTime = lastWorkflowStartedTime;
    }

    public Long getLatency() {
        return latency;
    }

    public void setLatency(Long latency) {
        this.latency = latency;
    }

    public Long getLastForceUpdate() {
        return lastForceUpdate;
    }

    public void setLastForceUpdate(Long lastForceUpdate) {
        this.lastForceUpdate = lastForceUpdate;
    }
}
