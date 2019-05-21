package model;

public enum PommelMovement {

    LEFT(-1),
    RIGHT(1),
    NONE(0);

    Integer indexChange;

    PommelMovement(Integer indexChange) {
        this.indexChange = indexChange;
    }

    public Integer getIndexChange() {
        return indexChange;
    }
}
