package gui.components;

import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import model.PommelMovement;
import model.PommelStartPosition;

public class Pommel extends Polygon {
    public void setPosition(Ribbon ribbon, PommelStartPosition position) {
        ObservableList<Node> cells = ribbon.getChildren();
        double cellWidth = ((Label) cells.get(0)).getWidth();
        double cellsWidth = Math.round(cells.size() * cellWidth);
        double movementValue = Math.round(cellsWidth / 2.0);

        final TranslateTransition transition = getTransition();

        if (position.equals(PommelStartPosition.BEGINNING)) {
            transition.setByX(-movementValue + Math.round(cellWidth / 2) - Math.round(getTranslateX()));
        } else {
            transition.setByX(movementValue - Math.round(cellWidth / 2) - Math.round(getTranslateX()));
        }

        transition.play();
    }

    public void move(Ribbon ribbon, PommelMovement movement) {
        ObservableList<Node> cells = ribbon.getChildren();
        double movementValue = ((Label) cells.get(0)).getWidth() * movement.getIndexChange();

        final TranslateTransition transition = getTransition();

        transition.setByX(movementValue);

        transition.play();
    }

    private TranslateTransition getTransition() {
        final TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(1));
        transition.setNode(this);

        return transition;
    }

}
