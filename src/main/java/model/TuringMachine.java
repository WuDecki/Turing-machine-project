package model;

import model.controllers.TuringMachineController;

public class TuringMachine {

    private TuringMachineController controller;
    private Pommel pommel;
    private Ribbon ribbon;
    private TuringMachineProgram program;
    private State actualState;

    public TuringMachine(TuringMachineController controller) {
        this.controller = controller;
    }

    public void loadProgram(TuringMachineProgram program) {
        this.program = program;
        this.actualState = null;
    }

    public TuringMachineResponse startProgram(Character[] tape) throws TuringMachineException, InterruptedException {
        checkTapeSymbols(tape);
        ribbon = new Ribbon(tape);
        configurePommel();
        actualState = program.getFirstState();
        return executeActualState();
    }

    private void checkTapeSymbols(Character[] tape) throws TuringMachineException {
        TapeValidator tapeValidator = new TapeValidator(program, tape);
        if (!tapeValidator.isValid())
            throw new TuringMachineException("A tape includes incorrect symbols for loaded program");
    }

    private void configurePommel() throws TuringMachineException {
        Integer pommelStartPosition = calculatePommelStartPosition();
        if (pommelStartPosition == -1)
            throw new TuringMachineException("A pommel has invalid position");

        pommel = new Pommel(pommelStartPosition);
    }

    private Integer calculatePommelStartPosition() {
        PommelStartPosition startPosition = program.getStartPosition();
        switch (startPosition) {
            case BEGINNING: {
                return 0;
            }
            case END: {
                return ribbon.getInputCharactersLength() -1;
            }
        }

        return -1;
    }

    private TuringMachineResponse executeActualState() throws TuringMachineException, InterruptedException {
        StateType actualStateType = getActualStateType();

        if (actualStateType == StateType.ACCEPTABLE) {
            return TuringMachineResponse.ACCEPT;
        } else if (actualStateType == StateType.REJECTABLE) {
            return TuringMachineResponse.REJECT;
        } else {
            Character actualCharacter = pommel.readCharacter(ribbon);
            controller.onMakeDecision(actualState, actualCharacter);
            Operation operation = actualState.getOperation(actualCharacter);

            final State previousState = actualState;
            actualState = processOperation(operation);

            controller.onChangeState(previousState, actualCharacter, operation.getNextState());
            return executeActualState();
        }
    }

    private StateType getActualStateType() {
        return actualState.getType();
    }

    private State processOperation(Operation operation) throws InterruptedException {
        pommel.writeCharacter(ribbon, operation.getNewChar());
        pommel.move(operation.getMovement());
        controller.onProcessOperation(operation);
        return operation.getNextState();
    }

    public Character[] getRibbonTape() {
        return ribbon.getTape();
    }
}
