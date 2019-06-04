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

    public static TuringMachineProgram prepareProgram() {
        final List<Character> symbols = prepareSymbols();
        final List<State> states = prepareStates();

        return new TuringMachineProgram(symbols, '$', states, states.get(0), PommelStartPosition.BEGINNING);
    }

    private static List<Character> prepareSymbols() {
        final List<Character> symbols = new ArrayList<>();
        symbols.add('a');
        symbols.add('b');
        symbols.add('c');
        symbols.add('$');

        return symbols;
    }

    private static List<State> prepareStates() {
        final List<State> states = new ArrayList<>();
        final State state0 = new State("S0", StateType.NORMAL);
        final State state1 = new State("S1", StateType.NORMAL);
        final State state2 = new State("S2", StateType.NORMAL);
        final State state3 = new State("S3", StateType.NORMAL);
        final State state4 = new State("S4", StateType.NORMAL);
        final State state5 = new State("S5", StateType.NORMAL);
        final State state6 = new State("S6", StateType.NORMAL);
        final State state7 = new State("S7", StateType.NORMAL);
        final State state8 = new State("S8", StateType.ACCEPTABLE);
        final State state9 = new State("S9", StateType.REJECTABLE);

        state0.addOperation('a', new Operation('$', PommelMovement.RIGHT, state1));
        state0.addOperation('b', new Operation('$', PommelMovement.RIGHT, state4));
        state0.addOperation('c', new Operation('$', PommelMovement.RIGHT, state6));
        state0.addOperation('$', new Operation('$', PommelMovement.NONE, state8));

        state1.addOperation('a', new Operation('a', PommelMovement.RIGHT, state1));
        state1.addOperation('b', new Operation('b', PommelMovement.RIGHT, state1));
        state1.addOperation('c', new Operation('c', PommelMovement.RIGHT, state1));
        state1.addOperation('$', new Operation('$', PommelMovement.LEFT, state2));

        state2.addOperation('a', new Operation('$', PommelMovement.LEFT, state3));
        state2.addOperation('b', new Operation('$', PommelMovement.NONE, state9));
        state2.addOperation('c', new Operation('$', PommelMovement.NONE, state9));
        state2.addOperation('$', new Operation('$', PommelMovement.NONE, state8));

        state3.addOperation('a', new Operation('a', PommelMovement.LEFT, state3));
        state3.addOperation('b', new Operation('b', PommelMovement.LEFT, state3));
        state3.addOperation('c', new Operation('c', PommelMovement.LEFT, state3));
        state3.addOperation('$', new Operation('$', PommelMovement.RIGHT, state0));

        state4.addOperation('a', new Operation('a', PommelMovement.RIGHT, state4));
        state4.addOperation('b', new Operation('b', PommelMovement.RIGHT, state4));
        state4.addOperation('c', new Operation('c', PommelMovement.RIGHT, state4));
        state4.addOperation('$', new Operation('$', PommelMovement.LEFT, state5));

        state5.addOperation('a', new Operation('$', PommelMovement.NONE, state9));
        state5.addOperation('b', new Operation('$', PommelMovement.LEFT, state3));
        state5.addOperation('c', new Operation('$', PommelMovement.NONE, state9));
        state5.addOperation('$', new Operation('$', PommelMovement.NONE, state8));

        state6.addOperation('a', new Operation('a', PommelMovement.RIGHT, state6));
        state6.addOperation('b', new Operation('b', PommelMovement.RIGHT, state6));
        state6.addOperation('c', new Operation('c', PommelMovement.RIGHT, state6));
        state6.addOperation('$', new Operation('$', PommelMovement.LEFT, state7));

        state7.addOperation('a', new Operation('$', PommelMovement.NONE, state9));
        state7.addOperation('b', new Operation('$', PommelMovement.NONE, state9));
        state7.addOperation('c', new Operation('$', PommelMovement.LEFT, state3));
        state7.addOperation('$', new Operation('$', PommelMovement.NONE, state8));

        states.add(state0);
        states.add(state1);
        states.add(state2);
        states.add(state3);
        states.add(state4);
        states.add(state5);
        states.add(state6);
        states.add(state7);
        states.add(state8);
        states.add(state9);

        return states;
    }

    public static Character[] prepareTape(final String text) {
        final List<Character> characters = new ArrayList<>();

        for (final char character : text.toCharArray()) {
            characters.add(Character.valueOf(character));
        }

        return characters.toArray(new Character[characters.size()]);
    }
}
