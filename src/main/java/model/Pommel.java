package model;

public class Pommel {

    private Integer actualPosition;

    public Pommel(Integer pommelPosition) {
        this.actualPosition = pommelPosition;
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
