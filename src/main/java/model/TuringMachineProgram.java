package model;

import java.util.List;

public class TuringMachineProgram {

    private List<Character> symbols;
    private Character movementCharacter;
    private List<State> states;
    private State firstState;
    private PommelStartPosition startPosition;

    public TuringMachineProgram(List<Character> symbols, Character movementCharacter, List<State> states, State firstState, PommelStartPosition startPosition) {
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

}
