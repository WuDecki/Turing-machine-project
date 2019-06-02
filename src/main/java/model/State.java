package model;

import java.util.HashMap;
import java.util.Map;

public class State {

    private String idn;
    private StateType type;
    private Map<Character, Operation> operations;

    public State(String idn, StateType type) {
        this.idn = idn;
        this.type = type;
        this.operations = new HashMap<>();
    }

    public String getIdn() {
        return idn;
    }

    public StateType getType() {
        return type;
    }

    public void addOperation(final Character character, final Operation operation) {
        operations.put(character, operation);
    }

    public Operation getOperation(final Character character) {
        return operations.get(character);
    }

    public Map<Character, Operation> getOperations() {
        return operations;
    }

    @Override
    public String toString() {
        return idn;
    }
}
