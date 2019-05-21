package model;

public class Pommel {

    Integer startPosition;
    Integer actualPosition;

    public Pommel(Integer pommelPosition) {
        this.startPosition = pommelPosition;
    }

    public void resetPommelPosition() {
        actualPosition = startPosition;
    }

    public Character readCharacter(Ribbon ribbon) {
        return ribbon.getCharacterAtPosition(actualPosition);
    }

    public void writeCharacter(Ribbon ribbon, Character character) {
        ribbon.setCharacterAtPosition(character, actualPosition);
    }

    public void move(PommelMovement movement) {
        actualPosition += movement.getIndexChange();
    }
}
