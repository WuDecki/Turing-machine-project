package model;

import java.util.List;

public class TuringMachine {

    Pommel pommel;
    Ribbon ribbon;
    TuringMachineProgram program;

    public void loadProgram(TuringMachineProgram program) {
        this.program = program;

    }

    public void loadTapeToRibbon(Character[] tape) throws TuringMachineException {
        if (program == null)
            throw new TuringMachineException("A tape cannot be load before program");

        checkTapeSymbols(tape);
        ribbon = new Ribbon(tape);
        configurePommel();
    }

    private void checkTapeSymbols(Character[] tape) throws TuringMachineException {
        if (!symbolsAreCorrect(tape))
            throw new  TuringMachineException("A tape includes incorrect symbols for loaded program");
    }

    private boolean symbolsAreCorrect(Character[] tape) {
        List<Character> symbols = program.getSymbols();
        for (Character character: tape) {
            if (!symbolIsCorrect(symbols, character)) {
                return false;
            }
        }

        return true;
    }

    private boolean symbolIsCorrect(List<Character> symbols, Character character) {
        for (Character symbol: symbols) {
            if (symbol.equals(character)) {
                return true;
            }
        }

        return false;
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

    public void startProgram() {
        executeNextState();
    }

    private void executeNextState() {
        StateType actualStateType = getActualStateType();

        if (actualStateType == StateType.ACCEPTABLE || actualStateType == StateType.REJECTABLE) {
            return;
        } else {
            Character actualCharacter = pommel.readCharacter(ribbon);
            Operation operation = program.executeAndGetOperation(actualCharacter);
            processOperation(operation);
            executeNextState();
        }
    }

    private StateType getActualStateType() {
        State actualState = program.getActualState();
        return actualState.getType();
    }

    private void processOperation(Operation operation) {
        pommel.writeCharacter(ribbon, operation.getNewChar());
        pommel.move(operation.getMovement());
    }

    public Character[] getRibbonTape() {
        return ribbon.getTape();
    }
}
