package gui.components;

import gui.configuration.Config;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import model.Operation;
import model.PommelMovement;
import model.State;
import model.TuringMachineProgram;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TuringGridCellEditor extends VBox implements Initializable {

    private final TuringMachineProgram program;
    private final Operation operation;

    @FXML
    public ComboBox<Character> characterComboBox;

    @FXML
    public ComboBox<PommelMovement> pommelMovementComboBox;

    @FXML
    public ComboBox<State> nextStateComboBox;

    @FXML
    public Button saveButton;


    public TuringGridCellEditor(final TuringMachineProgram program, final Operation operation) {
        this.program = program;
        this.operation = operation;
        final FXMLLoader fxmlLoader = new FXMLLoader(TuringGridCellEditor.class.getResource(Config.Nodes.TURING_GRID_CELL_EDITOR));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        characterComboBox.setItems(FXCollections.observableList(program.getSymbols()));
        pommelMovementComboBox.setItems(FXCollections.observableArrayList(PommelMovement.values()));
        nextStateComboBox.setItems(FXCollections.observableList(program.getStates()));

        characterComboBox.setValue(operation.getNewChar());
        pommelMovementComboBox.setValue(operation.getMovement());
        nextStateComboBox.setValue(operation.getNextState());
    }
}
