package model.controllers;

import model.Operation;
import model.State;

public interface TuringMachineController {

    void onMakeDecision(State actualState, Character actualCharacter) throws InterruptedException;

    void onChangeState(State actualState, Character actualCharacter, State nextState) throws InterruptedException;

    void onProcessOperation(Operation operation) throws InterruptedException;
}
