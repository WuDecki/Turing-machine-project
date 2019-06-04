package model;

public class Operation {

    private Character newChar;
    private Character conditionalChar;
    private PommelMovement movement;
    private State nextState;

    public Operation(final Character newChar, final PommelMovement movement, final State nextState) {
        this.newChar = newChar;
        this.movement = movement;
        this.nextState = nextState;
    }

    public Character getNewChar() {
        return newChar;
    }

    public PommelMovement getMovement() {
        return movement;
    }

    public State getNextState() {
        return nextState;
    }

    public void setNewChar(final Character newChar) {
        this.newChar = newChar;
    }

    public void setMovement(final PommelMovement movement) {
        this.movement = movement;
    }

    public void setNextState(final State nextState) {
        this.nextState = nextState;
    }

    public Character getConditionalChar() {
        return conditionalChar;
    }

    public void setConditionalChar(Character conditionalChar) {
        this.conditionalChar = conditionalChar;
    }

    @Override
    public String toString() {
        return String.format("%s\n%c %c", getNextState().getIdn(), getNewChar(), getMovement().equals(PommelMovement.NONE) ? '-' : getMovement().name().charAt(0));
    }
}
