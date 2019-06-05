package model;

import java.util.List;

public class TapeValidator {

    TuringMachineProgram program;
    Character[] tape;

    public TapeValidator(TuringMachineProgram program, Character[] tape) {
        this.program = program;
        this.tape = tape;
    }

    public boolean isValid() {
        return symbolsAreCorrect();
    }

    private boolean symbolsAreCorrect() {
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
}
