package gui.controllers;

import gui.builders.TuringMachineProgramBuilder;
import gui.components.TuringGrid;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.State;
import model.StateType;
import model.TuringMachineProgram;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController extends AbstractController {

    @FXML
    public TextField addSymbolInput;
    @FXML
    public ComboBox<Character> removeSymbolInput;
    @FXML
    public ComboBox<State> removeStateInput;
    @FXML
    public TuringGrid grid;
    private TuringMachineProgram program;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        program = TuringMachineProgramBuilder.getDefaultProgram();

        initializeTuringGrid(program);
    }

    private void initializeTuringGrid(final TuringMachineProgram program) {
        removeSymbolInput.setItems(
                FXCollections.observableList(
                        program.getSymbols().stream()
                                .filter(character -> character != '$')
                                .collect(Collectors.toList())
                )
        );

        if (program.getSymbols().size() > 1) {
            removeSymbolInput.setValue(program.getSymbols().get(1));
        }

        removeStateInput.setItems(
                FXCollections.observableList(
                        program.getStates().stream()
                                .filter(state -> state.getType().equals(StateType.NORMAL))
                                .collect(Collectors.toList())
                )
        );

        if (program.getStates().size() > 2) {
            removeStateInput.setValue(program.getStates().get(0));
        }

        grid.initialize(program);
    }

    @FXML
    public void addState(final ActionEvent event) {
        TuringMachineProgramBuilder.addNewState(program);
        initializeTuringGrid(program);
    }

    @FXML
    public void removeState(final ActionEvent event) {
        final State toRemove = removeStateInput.getValue();

        if (toRemove == null) {
            showError("State need to be chosen!");

            return;
        }

        try {
            TuringMachineProgramBuilder.removeState(program, toRemove);
            initializeTuringGrid(program);
        } catch (final Exception ignored) {
            showError("Sorry, state could not be removed!");
        }
    }

    @FXML
    public void addSymbol(final ActionEvent event) {
        final String input = addSymbolInput.getText();

        if (input == null || input.length() != 1) {
            showError("Symbol need to be a single character!");

            return;
        }

        if (!input.matches("[a-zA-Z]")) {
            showError("Symbol need to be an alphabetical english character!");

            return;
        }

        if (program.getSymbols().contains(input.charAt(0))) {
            showError("Alphabet already contains that character!");

            return;
        }

        TuringMachineProgramBuilder.addSymbol(program, input.charAt(0));
        initializeTuringGrid(program);
        addSymbolInput.clear();
    }

    @FXML
    public void removeSymbol(final ActionEvent event) {
        final Character symbol = removeSymbolInput.getValue();

        if (symbol == null) {
            showError("Symbol need to be chosen!");

            return;
        }

        try {
            TuringMachineProgramBuilder.removeSymbol(program, symbol);
            initializeTuringGrid(program);
        } catch (final Exception ignored) {
            showError("Sorry, symbol could not be removed!");
        }
    }

    private void showError(final String message) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
