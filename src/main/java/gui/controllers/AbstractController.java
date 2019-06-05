package gui.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

public abstract class AbstractController implements Initializable {

    protected void showError(final String message) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void showSuccess(final String message) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
