package gui.controllers;

import gui.animations.TuringGridAnimation;
import gui.builders.TuringMachineProgramBuilder;
import gui.components.TuringGrid;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;
import model.controllers.TuringMachineController;

import java.net.URL;
import java.util.Arrays;
import java.util.Queue;
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
    @FXML
    public Button startProgramButton;
    @FXML
    public CheckBox stepByStepCheckBox;

    private TuringMachineProgram program;
    private BooleanProperty isProgramRunning = new SimpleBooleanProperty(false);
    private TuringGridAnimation turingGridAnimation;
    private Thread turingGridAnimationThread;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
//        program = TuringMachineProgramBuilder.getDefaultProgram();
        program = TuringMachineProgramBuilder.prepareProgram();

        initializeTuringGrid(program);
        initializeProgramStartRestartButtons();
    }

    private void initializeProgramStartRestartButtons() {
        stepByStepCheckBox.disableProperty().bind(isProgramRunning);
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
    public void startProgram() {
        isProgramRunning.setValue(true);

        turingGridAnimation = new TuringGridAnimation(grid, () -> {
            this.isProgramRunning.setValue(false);
            this.startProgramButton.setText("Start program");
            this.startProgramButton.setOnAction(event -> this.startProgram());
        });

        if (stepByStepCheckBox.isSelected()) {
            startProgramButton.setText("Next step");
            turingGridAnimation.setTimeoutSeconds(1024);
            startProgramButton.setOnAction(event -> this.startProgramNextStep());
        }

        final Queue<TuringGridAnimation.MakeDecision> onMakeDecisionQueue = turingGridAnimation.getOnMakeDecisionQueue();
        final Queue<TuringGridAnimation.ChangeState> onChangeStateQueue = turingGridAnimation.getOnChangeStateQueue();

        final TuringMachine machine = new TuringMachine(new TuringMachineController() {
            @Override
            public void onMakeDecision(final model.State actualState, final Character actualCharacter) {
                System.out.println(String.format("onMakeDecision %s %c", actualState, actualCharacter));
                onMakeDecisionQueue.add(new TuringGridAnimation.MakeDecision(actualState, actualCharacter));
            }

            @Override
            public void onChangeState(final model.State actualState, final model.State nextState) {
                System.out.println(String.format("onChangeState %s %s", actualState, nextState));
                onChangeStateQueue.add(new TuringGridAnimation.ChangeState(actualState, nextState));
            }
        });

        machine.loadProgram(program);
        final Character[] tape = TuringMachineProgramBuilder.prepareTape("abcccbba$");
        try {
            final TuringMachineResponse response = machine.startProgram(tape);
            System.out.println(response.toString());
        } catch (final TuringMachineException e) {
            e.printStackTrace();
        }

        System.out.println(Arrays.toString(machine.getRibbonTape()));


        turingGridAnimationThread = new Thread(turingGridAnimation);
        turingGridAnimationThread.start();
    }

    private void startProgramNextStep() {
        turingGridAnimationThread.interrupt();
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

    @FXML
    public void restartProgram() {
        if (turingGridAnimation != null && turingGridAnimation.isRunning()) {
            turingGridAnimation.setRunning(false);
        }

        grid.clearHighlights();
        isProgramRunning.setValue(false);
    }

    private void showError(final String message) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
