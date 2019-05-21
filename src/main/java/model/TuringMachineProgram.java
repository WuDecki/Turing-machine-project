package model;

import java.util.List;

public class TuringMachineProgram {

    List<Character> symbols;
    List<State> states;
    State firstState;
    State actualState;
    PommelStartPosition startPosition;

    public TuringMachineProgram(List<Character> symbols, List<State> states, State firstState, PommelStartPosition startPosition) {
        this.symbols = symbols;
        this.states = states;
        this.firstState = firstState;
        this.actualState = firstState;
        this.startPosition = startPosition;
    }

    public List<Character> getSymbols() {
        return symbols;
    }

    public State getFirstState() {
        return firstState;
    }

    public List<State> getStates() {
        return states;
    }

    public PommelStartPosition getStartPosition() {
        return startPosition;
    }

    public Operation executeAndGetOperation(Character character) {
        Operation operation = actualState.getOperation(character);
        actualState = operation.getNextState();
        return operation;
    }

    public State getActualState() {
        return actualState;
    }

    public void setActualState(State actualState) {
        this.actualState = actualState;
    }
}
