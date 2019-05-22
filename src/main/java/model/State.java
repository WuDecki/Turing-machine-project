package model;

import java.util.HashMap;
import java.util.Map;

public class State {

    private StateType type;
    private Map<Character, Operation> operations;

    public State(StateType type) {
        this.type = type;
        operations = new HashMap<>();
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
