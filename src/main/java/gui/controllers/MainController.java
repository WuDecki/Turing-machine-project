package gui.controllers;

import gui.components.TuringGrid;
import javafx.fxml.FXML;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class MainController extends AbstractController {

    @FXML
    public TuringGrid grid;

    private static List<State> prepareStates() {
        final List<State> states = new ArrayList<>();
        final State state0 = new State(StateType.NORMAL);
        final State state1 = new State(StateType.NORMAL);
        final State state2 = new State(StateType.NORMAL);
        final State state3 = new State(StateType.NORMAL);
        final State state4 = new State(StateType.NORMAL);
        final State state5 = new State(StateType.NORMAL);
        final State state6 = new State(StateType.NORMAL);
        final State state7 = new State(StateType.NORMAL);
        final State state8 = new State(StateType.ACCEPTABLE);
        final State state9 = new State(StateType.REJECTABLE);

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

    private static TuringMachineProgram prepareProgram() {
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

    public void initialize() {
        final TuringMachineProgram program = prepareProgram();
        grid.initialize(program);
    }

}
