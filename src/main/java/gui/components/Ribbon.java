package gui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Ribbon extends HBox {

    private int size;
    private List<Label> labels;

    public void init(Character[] tape) {
        labels = Arrays.stream(tape)
                .map(character -> new Label(character.toString()))
                .collect(Collectors.toList());


        getChildren().setAll(labels);
        size = labels.size();

        applyCss();
        layout();
    }

    public void setCharacter(int position, Character newChar) {
        if (position < size && position >= 0) {
            labels.get(position).setText(newChar.toString());
        }
    }
}
