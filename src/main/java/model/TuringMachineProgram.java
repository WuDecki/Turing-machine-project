package model;

import java.util.List;

public class TuringMachineProgram {

    private final List<Character> symbols;
    private final Character movementCharacter;
    private final List<State> states;
    private final State firstState;
    private final PommelStartPosition startPosition;

    public TuringMachineProgram(final List<Character> symbols, final Character movementCharacter, final List<State> states, final State firstState, final PommelStartPosition startPosition) {
        this.symbols = symbols;
        this.movementCharacter = movementCharacter;
        this.states = states;
        this.firstState = firstState;
        this.startPosition = startPosition;
    }

    public List<Character> getSymbols() {
        return symbols;
    }

    public Character getMovementCharacter() {
        return movementCharacter;
    }

    public State getFirstState() {
        return firstState;
    }

    public PommelStartPosition getStartPosition() {
        return startPosition;
    }

    public List<State> getStates() {
        return states;
    }
}
