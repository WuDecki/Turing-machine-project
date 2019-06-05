package gui.controllers;

import gui.StaticContext;
import gui.animations.TuringAnimation;
import gui.builders.TuringMachineProgramBuilder;
import gui.components.Pommel;
import gui.components.Ribbon;
import gui.components.TuringGrid;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import model.StateType;
import model.TuringMachineProgram;
import model.TuringMachineResponse;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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
    private Character[] tape;

    private BooleanProperty isProgramRunning = new SimpleBooleanProperty(false);
    private Thread turingAnimationThread;
    private Task<TuringMachineResponse> turingAnimationTask;
    private TuringAnimation turingAnimation;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        initToolsAndOptions();
        initDefaults();
        initializeDisablingOnProgramRun();
        initializeRibbonAndPommel(TuringMachineProgramBuilder.convertToTapeCharacters("$$$$$"));

        StaticContext.STAGE.setOnCloseRequest(event -> {
            if (turingAnimationTask != null && turingAnimationThread.isAlive()) {
                turingAnimation.getController().setRestart(true);
                turingAnimationTask.cancel(true);
            }
        });
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

        grid.clearHighlights();
        long sleep = 1000;
        boolean isStepByStep = false;

        if (stepByStepCheckBox.isSelected()) {
            startProgramButton.setText("Next step");
            startProgramButton.setOnAction(event -> {
                startProgramButton.setDisable(true);
                this.startProgramNextStep();
                CompletableFuture.runAsync(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException ignored) {
                    } finally {
                        startProgramButton.setDisable(false);
                    }
                });
            });
            sleep = 360000;
            isStepByStep = true;
        } else {
            startProgramButton.setDisable(true);
        }

        isProgramRunning.setValue(true);
        pommel.setPosition(ribbon, program.getStartPosition());

        turingAnimation = TuringAnimation.getInstance(program, tape, grid, pommel, ribbon, sleep, isStepByStep);
        turingAnimationTask = turingAnimation.getTuringAnimationTask();

        turingAnimationTask.setOnSucceeded(event -> {
            showSuccess(String.format("The results of the program ended with an  %s state!", turingAnimationTask.getValue()));
            enableButtons();
        });

        turingAnimationTask.setOnFailed(event -> {
            if (!(turingAnimationTask.getException() instanceof InterruptedException)) {
                showError(turingAnimationTask.getException().getMessage());
            }

            enableButtons();
            restartProgram();
        });

        turingAnimationThread = new Thread(turingAnimationTask);
        turingAnimationThread.start();
    }

    private void enableButtons() {
        Platform.runLater(() -> {
            startProgramButton.setText("Start program");
            startProgramButton.setOnAction(event -> this.startProgram());
            startProgramButton.setDisable(false);
            restartProgramButton.setDisable(false);
            isProgramRunning.setValue(false);
        });
    }

    public void initializeRibbonAndPommel(Character[] tape) {
        this.tape = tape;
        ribbon.init(tape);
        pommel.setPosition(ribbon, program.getStartPosition());
    }

    private void startProgramNextStep() {
        turingAnimationThread.interrupt();
    }

    @FXML
    public void restartProgram() {
        restartProgramButton.setDisable(true);

        CompletableFuture.runAsync(() -> {
            if (turingAnimationTask != null && turingAnimationThread.isAlive()) {
                turingAnimation.getController().setRestart(true);
                turingAnimationTask.cancel(true);
            }

            grid.clearHighlights();
            pommel.setPosition(ribbon, program.getStartPosition());
        }).thenRun(this::enableButtons);
    }

    public TuringMachineProgram getProgram() {
        return program;
    }

}
