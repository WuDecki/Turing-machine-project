package model;

import java.util.HashMap;
import java.util.Map;

public class State {

    private static int COUNT = 0;

    private final int id;
    private final StateType type;
    private final Map<Character, Operation> operations;

    public State(final StateType type) {
        this.type = type;
        id = COUNT++;
        operations = new HashMap<>();
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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "S" + id;
    }
}
