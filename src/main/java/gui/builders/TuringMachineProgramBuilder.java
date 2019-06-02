package gui.builders;

import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TuringMachineProgramBuilder {

    public static final char EMPTY_CHARACTER = '$';

    private TuringMachineProgramBuilder() {
    }

    public static TuringMachineProgram getDefaultProgram() {
        final List<Character> symbols = new ArrayList<>();
        symbols.add(EMPTY_CHARACTER);

        final State acceptable = new State("S1", StateType.ACCEPTABLE);
        final State rejectable = new State("S2", StateType.REJECTABLE);

        final List<State> states = new ArrayList<>(Arrays.asList(
                acceptable,
                rejectable
        ));

        return new TuringMachineProgram(symbols, EMPTY_CHARACTER, states, states.get(0), PommelStartPosition.BEGINNING);
    }

    public static void addSymbol(final TuringMachineProgram program, final Character symbol) {
        final State lastState = program.getStates().get(program.getStates().size() - 1);
        program.getSymbols().add(symbol);
        program.getStates().stream()
                .filter(state -> state.getType().equals(StateType.NORMAL))
                .forEach(state -> state.getOperations().put(symbol, new Operation(EMPTY_CHARACTER, PommelMovement.NONE, lastState)));
    }

    public static void removeSymbol(final TuringMachineProgram program, final Character symbol) {
        program.getStates().stream()
                .filter(state -> state.getType().equals(StateType.NORMAL))
                .forEach(state -> {
                    state.getOperations().remove(symbol);
                    state.getOperations().forEach((character, operation) -> {
                        if (operation.getNewChar() == symbol) {
                            operation.setNewChar(EMPTY_CHARACTER);
                        }
                    });
                });

        program.getSymbols().remove(symbol);
    }

    public static void addNewState(final TuringMachineProgram program) {
        final List<State> states = program.getStates();
        final State lastState = states.get(states.size() - 1);
        final State newState = new State(String.format("S%d", states.size() - 2), StateType.NORMAL);

        program.getSymbols().forEach(character -> newState.addOperation(character, new Operation(EMPTY_CHARACTER, PommelMovement.NONE, lastState)));

        states.add(states.size() - 2, newState);

        for (int i = states.size() - 2; i < states.size(); i++) {
            states.get(i).setIdn("S" + (i));
        }
    }

    public static void removeState(final TuringMachineProgram program, final State toRemove) {
        final List<State> states = program.getStates();
        final State lastState = states.get(states.size() - 1);

        states.remove(toRemove);
        states.forEach(state -> state.getOperations().forEach((character, operation) -> {
            if (operation.getNextState().equals(toRemove)) {
                operation.setNextState(lastState);
            }
        }));
    }
}
