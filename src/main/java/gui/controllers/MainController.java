package gui.controllers;

import gui.animations.TuringAnimation;
import gui.builders.TuringMachineProgramBuilder;
import gui.components.Pommel;
import gui.components.Ribbon;
import gui.components.TuringGrid;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import model.*;
import model.controllers.TuringMachineController;

import java.net.URL;
import java.util.Arrays;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController extends AbstractController {

    @FXML
    private ToolsAndOptionsController toolsAndOptionsController;

    @FXML
    public TuringGrid grid;
    @FXML
    public Button startProgramButton;
    @FXML
    public CheckBox stepByStepCheckBox;
    @FXML
    public HBox toolsAndOptions;
    @FXML
    public Button restartProgramButton;
    @FXML
    public Pommel pommel;
    @FXML
    public Ribbon ribbon;

    private TuringMachineProgram program;
    private BooleanProperty isProgramRunning = new SimpleBooleanProperty(false);
    private TuringAnimation turingAnimation;
    private Thread turingGridAnimationThread;
    private Character[] tape;
    private TuringMachineResponse turingMachineResponse;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        initToolsAndOptions();
        initDefaults();
        initializeDisablingOnProgramRun();
        initializeRibbonAndPommel(TuringMachineProgramBuilder.convertToTapeCharacters("$$$$$"));
    }

    private void initToolsAndOptions() {
        toolsAndOptionsController.setMainController(this);
    }

    private void initDefaults() {
        initializeTuringProgramGrid(TuringMachineProgramBuilder.getDefaultProgram());
    }

    private void initializeDisablingOnProgramRun() {
        stepByStepCheckBox.disableProperty().bind(isProgramRunning);
        grid.disableProperty().bind(isProgramRunning);
        toolsAndOptions.disableProperty().bind(isProgramRunning);
    }

    public void initializeTuringProgramGrid(final TuringMachineProgram program) {
        this.program = program;

        toolsAndOptionsController.removeSymbolInput.setItems(
                FXCollections.observableList(
                        program.getSymbols().stream()
                                .filter(character -> character != '$')
                                .collect(Collectors.toList())
                )
        );

        if (program.getSymbols().size() > 1) {
            toolsAndOptionsController.removeSymbolInput.setValue(program.getSymbols().get(1));
        }

        toolsAndOptionsController.removeStateInput.setItems(
                FXCollections.observableList(
                        program.getStates().stream()
                                .filter(state -> state.getType().equals(StateType.NORMAL))
                                .collect(Collectors.toList())
                )
        );

        if (program.getStates().size() > 2) {
            toolsAndOptionsController.removeStateInput.setValue(program.getStates().get(0));
        }

        grid.initialize(program);
        toolsAndOptionsController.pommelStartingPositionChoiceBox.setValue(program.getStartPosition());
    }

    @FXML
    public void startProgram() {
        if (tape == null || tape.length == 0) {
            showError("Please initialize tape!");
            return;
        }

        if (program.getStates().size() < 3) {
            showError("Turing machine program needs more than 3 states to be executed!");
            return;
        }

        if (program.getSymbols().size() < 2) {
            showError("Turing machine program needs more than 2 symbols to be executed!");
            return;
        }

        turingMachineResponse = null;
        isProgramRunning.setValue(true);

        pommel.setPosition(ribbon, toolsAndOptionsController.getPommelStartingPosition());

        turingAnimation = new TuringAnimation(grid, pommel, ribbon, () -> {
            this.startProgramButton.setText("Start program");
            this.startProgramButton.setOnAction(event -> this.startProgram());
            this.startProgramButton.setDisable(false);
            this.restartProgramButton.setDisable(false);
            this.isProgramRunning.setValue(false);
            if (this.turingMachineResponse != null) {
                Platform.runLater(() -> showSuccess(String.format("The results of the program ended with an  %s state!", turingMachineResponse.toString())));
            }
        });

        if (stepByStepCheckBox.isSelected()) {
            startProgramButton.setText("Next step");
            turingAnimation.setTimeoutSeconds(1024);
            startProgramButton.setOnAction(event -> this.startProgramNextStep());
        } else {
            startProgramButton.setDisable(true);
        }

        final Queue<TuringAnimation.MakeDecision> onMakeDecisionQueue = turingAnimation.getOnMakeDecisionQueue();
        final Queue<TuringAnimation.ChangeState> onChangeStateQueue = turingAnimation.getOnChangeStateQueue();
        final Queue<Operation> onProcessOperationQueue = turingAnimation.getOnProcessOperationQueue();

        final TuringMachine machine = new TuringMachine(new TuringMachineController() {
            @Override
            public void onMakeDecision(final model.State actualState, final Character actualCharacter) {
                System.out.println(String.format("onMakeDecision %s %c", actualState, actualCharacter));
                onMakeDecisionQueue.add(new TuringAnimation.MakeDecision(actualState, actualCharacter));
            }

            @Override
            public void onChangeState(final model.State actualState, final model.State nextState) {
                System.out.println(String.format("onChangeState %s %s", actualState, nextState));
                onChangeStateQueue.add(new TuringAnimation.ChangeState(actualState, nextState));
            }

            @Override
            public void onProcessOperation(final Operation operation) {
                System.out.println(String.format("onProcessOperation %s %s", operation.getNewChar(), operation.getMovement()));
                onProcessOperationQueue.add(operation);
            }
        });

        machine.loadProgram(program);
        try {
            turingMachineResponse = machine.startProgram(tape);
            System.out.println(turingMachineResponse.toString());
        } catch (final TuringMachineException e) {
            showError(e.getMessage());
            isProgramRunning.setValue(false);
            startProgramButton.setDisable(false);
            restartProgram();
            return;
        }

        System.out.println(Arrays.toString(machine.getRibbonTape()));


        turingGridAnimationThread = new Thread(turingAnimation);
        turingGridAnimationThread.start();
    }

    public void initializeRibbonAndPommel(Character[] tape) {
        this.tape = tape;
        ribbon.init(tape);
        pommel.setPosition(ribbon, toolsAndOptionsController.getPommelStartingPosition());
    }

    private void startProgramNextStep() {
        turingGridAnimationThread.interrupt();
    }

    @FXML
    public void restartProgram() {
        if (turingAnimation != null && turingAnimation.isTaskRunning()) {
            restartProgramButton.setDisable(true);
            turingGridAnimationThread.interrupt();
            turingAnimation.setRunning(false);
        }

        startProgramButton.setDisable(false);
        grid.clearHighlights();
        pommel.setPosition(ribbon, toolsAndOptionsController.getPommelStartingPosition());
        isProgramRunning.setValue(false);
    }

    public TuringMachineProgram getProgram() {
        return program;
    }

}
