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

    public void addOperation(Character character, Operation operation) {
        operations.put(character, operation);
    }

    public Operation getOperation(Character character) {
        return operations.get(character);
    }
}
