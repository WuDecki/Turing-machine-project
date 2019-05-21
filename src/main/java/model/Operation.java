package model;

public class Operation {

    Character newChar;
    PommelMovement movement;
    State nextState;

    public Operation(Character newChar, PommelMovement movement, State nextState) {
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
}