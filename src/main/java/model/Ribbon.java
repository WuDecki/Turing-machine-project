package model;

public class Ribbon {

    private final static Character EMPTY_CHARACTER = 0;
    private Character[] tape;

    public Ribbon(Character[] tape) {
        this.tape = tape;
    }

    public Character[] getTape() {
        return tape;
    }

    public Integer getInputCharactersLength() {
        Integer length = 0;
        for (int i =0; i < tape.length; i++) {
            if (tape[i] == EMPTY_CHARACTER)
                break;

            length++;
        }

        return length;
    }

    public Character getCharacterAtPosition(Integer position) {
        return tape[position];
    }

    public void setCharacterAtPosition(Character character, Integer position) {
        tape[position] = character;
    }

}
