package uk.co.solong.schematf.analysis;

public class DiffRequest {
    private Long current;
    private Long previous;

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public Long getPrevious() {
        return previous;
    }

    public void setPrevious(Long previous) {
        this.previous = previous;
    }
}
