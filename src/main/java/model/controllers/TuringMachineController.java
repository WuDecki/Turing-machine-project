package model.controllers;

import model.Operation;
import model.State;

public interface TuringMachineController {

    void onMakeDecision(State actualState, Character actualCharacter);
    void onChangeState(State actualState, State nextState);

    void onProcessOperation(Operation operation);
}
