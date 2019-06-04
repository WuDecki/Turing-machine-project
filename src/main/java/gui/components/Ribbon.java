package gui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Ribbon extends HBox {

    public void init(Character[] tape) {
        List<Label> ribbonLabels = Arrays.stream(tape)
                .map(character -> new Label(character.toString()))
                .collect(Collectors.toList());


        getChildren().setAll(ribbonLabels);

        applyCss();
        layout();
    }

    public void setCharacter(int position, Character newChar) {
        ((Label) getChildren().get(position)).setText(newChar.toString());
    }
}
