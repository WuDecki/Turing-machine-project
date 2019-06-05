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

    private int actualPosition = 0;

    public void setPosition(Ribbon ribbon, PommelStartPosition position) {
        ObservableList<Node> cells = ribbon.getChildren();
        double cellWidth = ((Label) cells.get(0)).getWidth();
        double cellsWidth = Math.round(cells.size() * cellWidth);
        double movementValue = Math.round(cellsWidth / 2.0);

        final TranslateTransition transition = getTransition();

        if (position.equals(PommelStartPosition.BEGINNING)) {
            transition.setByX(-movementValue + Math.round(cellWidth / 2) - Math.round(getTranslateX()));
            actualPosition = 0;
        } else {
            transition.setByX(movementValue - Math.round(cellWidth / 2) - Math.round(getTranslateX()));
            actualPosition = cells.size();
        }

        transition.play();
    }

    public void move(Ribbon ribbon, PommelMovement movement) {
        ObservableList<Node> cells = ribbon.getChildren();
        final Integer indexChange = movement.getIndexChange();

        if ((indexChange == -1 && actualPosition == 0) || (indexChange == 1 && actualPosition == cells.size())) {
            return;
        }


        double movementValue = ((Label) cells.get(0)).getWidth() * indexChange;

        final TranslateTransition transition = getTransition();

        transition.setByX(movementValue);

        transition.play();


        if (indexChange == 1) {
            actualPosition += 1;
        } else if (indexChange == -1) {
            actualPosition -= 1;
        }

    }

    private TranslateTransition getTransition() {
        final TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(500));
        transition.setNode(this);

        return transition;
    }

    public int getActualPosition() {
        return actualPosition;
    }
}
