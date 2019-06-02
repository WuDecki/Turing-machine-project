package model.conversion;

import model.PommelStartPosition;
import model.State;

import java.util.List;

class TuringMachineDefinition {

    private List<Character> symbols;
    private Character movementCharacter;
    private List<State> states;
    private State firstState;
    private PommelStartPosition pommelStartPosition;

    public List<Character> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<Character> symbols) {
        this.symbols = symbols;
    }

    public Character getMovementCharacter() {
        return movementCharacter;
    }

    public void setMovementCharacter(Character movementCharacter) {
        this.movementCharacter = movementCharacter;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public State getFirstState() {
        return firstState;
    }

    public void setFirstState(State firstState) {
        this.firstState = firstState;
    }

    public PommelStartPosition getPommelStartPosition() {
        return pommelStartPosition;
    }

    public void setPommelStartPosition(PommelStartPosition pommelStartPosition) {
        this.pommelStartPosition = pommelStartPosition;
    }

    public State getStateByIdn(String idn) {
        for (State state: states) {
            if (state.getIdn().equals(idn))
                return state;
        }

        return null;
    }
}
