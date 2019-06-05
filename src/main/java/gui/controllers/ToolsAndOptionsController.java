package gui.controllers;

import gui.StaticContext;
import gui.builders.TuringMachineProgramBuilder;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import model.PommelStartPosition;
import model.State;
import model.TuringMachineProgram;
import model.conversion.JsonFileManager;
import model.conversion.JsonToProgramConverter;
import model.conversion.ProgramToJsonConverter;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ToolsAndOptionsController extends AbstractController implements Initializable {

    public HBox toolsAndOptions;
    public ComboBox<State> removeStateInput;
    public TextField addSymbolInput;
    public ComboBox<Character> removeSymbolInput;
    public ChoiceBox<PommelStartPosition> pommelStartingPositionChoiceBox;
    private MainController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pommelStartingPositionChoiceBox.setItems(
                FXCollections.observableArrayList(PommelStartPosition.BEGINNING, PommelStartPosition.END)
        );
        pommelStartingPositionChoiceBox.setValue(PommelStartPosition.BEGINNING);
        pommelStartingPositionChoiceBox.setOnAction(event -> onPommelStartingPositionChange());
    }

    private void onPommelStartingPositionChange() {
        final PommelStartPosition position = pommelStartingPositionChoiceBox.getValue();

        mainController.pommel.setPosition(
                mainController.ribbon, position
        );
        mainController.getProgram().setStartPosition(position);
    }

    @FXML
    public void addState() {
        final TuringMachineProgram program = mainController.getProgram();

        TuringMachineProgramBuilder.addNewState(program);
        mainController.initializeTuringProgramGrid(program);
    }

    @FXML
    public void removeState() {
        final State toRemove = removeStateInput.getValue();

        if (toRemove == null) {
            showError("State need to be chosen!");

            return;
        }

        try {
            final TuringMachineProgram program = mainController.getProgram();

            TuringMachineProgramBuilder.removeState(program, toRemove);
            mainController.initializeTuringProgramGrid(program);
        } catch (final Exception ignored) {
            showError("Sorry, state could not be removed!");
        }
    }

    @FXML
    public void addSymbol() {
        final String input = addSymbolInput.getText();

        if (input == null || input.length() != 1) {
            showError("Symbol need to be a single character!");

            return;
        }

        if (!input.matches("[a-zA-Z]")) {
            showError("Symbol need to be an alphabetical english character!");

            return;
        }

        final TuringMachineProgram program = mainController.getProgram();

        if (program.getSymbols().contains(input.charAt(0))) {
            showError("Alphabet already contains that character!");

            return;
        }

        TuringMachineProgramBuilder.addSymbol(program, input.charAt(0));
        mainController.initializeTuringProgramGrid(program);
        addSymbolInput.clear();
    }

    @FXML
    public void removeSymbol() {
        final Character symbol = removeSymbolInput.getValue();

        if (symbol == null) {
            showError("Symbol need to be chosen!");

            return;
        }

        try {
            final TuringMachineProgram program = mainController.getProgram();
            TuringMachineProgramBuilder.removeSymbol(program, symbol);
            mainController.initializeTuringProgramGrid(program);
        } catch (final Exception ignored) {
            showError("Sorry, symbol could not be removed!");
        }
    }

    @FXML
    public void clearProgram() {
        final TuringMachineProgram defaultProgram = TuringMachineProgramBuilder.getDefaultProgram();
        mainController.initializeTuringProgramGrid(defaultProgram);
        mainController.restartProgram();
    }

    @FXML
    public void initializeTape() {
        final TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Tape Input Dialog");
        dialog.setHeaderText("Provide tape characters");

        dialog.showAndWait()
                .ifPresent(characters -> {
                    if (!characters.matches("[a-zA-Z$]*")) {
                        showError("Symbol need to be an alphabetical english character! (Character \"$\" is acceptable)");
                        return;
                    }

                    if (characters.isEmpty()) {
                        showError("Tape can't be empty!");
                        return;
                    }

                    final Character[] tapeCharacters = TuringMachineProgramBuilder.convertToTapeCharacters(characters);
                    mainController.initializeRibbonAndPommel(tapeCharacters);
                });
    }

    @FXML
    public void importTuringProgram() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Turing Machine Program File");
        final File file = fileChooser.showOpenDialog(StaticContext.stage);

        try {
            if (file != null) {
                final JSONObject jsonProgram = JsonFileManager.readJsonObject(file.getAbsolutePath());
                final TuringMachineProgram program = new JsonToProgramConverter(jsonProgram).convert();
                mainController.initializeTuringProgramGrid(program);
                showSuccess("Program successfully imported!");
            }
        } catch (Exception e) {
            showError("Error during Turing machine program import!");
        }
    }

    @FXML
    public void exportTuringProgram() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Turing Machine Program File");
        FileChooser.ExtensionFilter fileExtensions =
                new FileChooser.ExtensionFilter("JSON", "*.json");

        fileChooser.getExtensionFilters().add(fileExtensions);
        final File file = fileChooser.showSaveDialog(StaticContext.stage);

        try {
            if (file != null) {
                JsonFileManager.writeJsonObject(file.getAbsolutePath(),
                        new ProgramToJsonConverter(mainController.getProgram()).convert());
                showSuccess("Program successfully saved!");
            }
        } catch (Exception e) {
            showError("Error during Turing machine program export!");
        }
    }

    public PommelStartPosition getPommelStartingPosition() {
        return pommelStartingPositionChoiceBox.getValue();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
