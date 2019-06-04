package model;

import java.util.HashMap;
import java.util.Map;

public class State {

    private String idn;
    private final StateType type;
    private final Map<Character, Operation> operations;

    public State(final String idn, final StateType type) {
        this.idn = idn;
        this.type = type;
        operations = new HashMap<>();
    }

    public String getIdn() {
        return idn;
    }

    public void setIdn(final String idn) {
        this.idn = idn;
    }

    public StateType getType() {
        return type;
    }

    public void addOperation(final Character character, final Operation operation) {
        operation.setConditionalChar(character);
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
