package gui.animations;

import gui.components.Pommel;
import gui.components.Ribbon;
import gui.components.TuringGrid;
import gui.components.TuringGridCell;
import javafx.application.Platform;
import javafx.concurrent.Task;
import model.*;
import model.controllers.TuringMachineController;

import java.util.List;

public class TuringAnimation {

    private DefaultTuringMachineController controller;
    private TuringMachine machine;
    private Character[] tape;

    private TuringAnimation(DefaultTuringMachineController controller, TuringMachine machine, Character[] tape) {
        this.controller = controller;
        this.machine = machine;
        this.tape = tape;
    }

    public static TuringAnimation getInstance(
            final TuringMachineProgram program, final Character[] tape, final TuringGrid grid,
            final gui.components.Pommel pommel, final Ribbon ribbon, final long sleep, final boolean isStepByStep) {
        final DefaultTuringMachineController controller = new DefaultTuringMachineController(grid, pommel, ribbon, sleep, isStepByStep);
        final TuringMachine machine = new TuringMachine(controller);

        machine.loadProgram(program);

        return new TuringAnimation(controller, machine, tape);
    }

    public Task<TuringMachineResponse> getTuringAnimationTask() {
        return new Task<TuringMachineResponse>() {
            @Override
            protected TuringMachineResponse call() throws Exception {
                return machine.startProgram(tape);
            }
        };
    }

    public DefaultTuringMachineController getController() {
        return controller;
    }

    public static class DefaultTuringMachineController implements TuringMachineController {
        private final boolean isStepByStep;
        private boolean restart = false;
        private final long sleep;
        private final TuringGrid grid;
        private final Pommel pommel;
        private final Ribbon ribbon;

        DefaultTuringMachineController(final TuringGrid grid, final gui.components.Pommel pommel, final Ribbon ribbon, final long sleep, final boolean isStepByStep) {
            this.grid = grid;
            this.pommel = pommel;
            this.ribbon = ribbon;
            this.sleep = sleep;
            this.isStepByStep = isStepByStep;
        }

        @Override
        public void onMakeDecision(State actualState, Character actualCharacter) throws InterruptedException {
            if (Thread.currentThread().isInterrupted())
                return;

            System.out.println(String.format("onMakeDecision %s %c", actualState, actualCharacter));

            final List<TuringGridCell> stateColumn = grid.getStateColumn(actualState);
            final List<TuringGridCell> characterRow = grid.getCharacterRow(actualCharacter);
            final TuringGridCell cell = grid.getCell(actualState, actualCharacter);

            grid.addStateHighlight(stateColumn);

            sleep();

            grid.addCharacterHighlight(characterRow);
            grid.addCellHighlight(cell);

            sleep();
        }

        @Override
        public void onChangeState(State actualState, Character actualCharacter, State nextState) throws InterruptedException {
            if (Thread.currentThread().isInterrupted())
                return;

            System.out.println(String.format("onChangeState %s %s %s", actualState, actualCharacter, nextState));

            final List<TuringGridCell> nextStateColumn = grid.getStateColumn(nextState);
            final List<TuringGridCell> stateColumn = grid.getStateColumn(actualState);
            final List<TuringGridCell> characterRow = grid.getCharacterRow(actualCharacter);
            final TuringGridCell cell = grid.getCell(actualState, actualCharacter);

            Platform.runLater(() -> {
                grid.removeStateHighlight(stateColumn);
                grid.removeCharacterHighlight(characterRow);
                grid.removeCellHighlight(cell);

                grid.addStateHighlight(nextStateColumn);
            });
        }

        @Override
        public void onProcessOperation(Operation operation) throws InterruptedException {
            if (Thread.currentThread().isInterrupted())
                return;

            System.out.println(String.format("onProcessOperation %s %s", operation.getNewChar(), operation.getMovement()));

            Platform.runLater(() -> ribbon.setCharacter(pommel.getActualPosition(), operation.getNewChar()));

            sleep();

            pommel.move(ribbon, operation.getMovement());

            sleep();
        }

        private void sleep() throws InterruptedException {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException interruptedException) {
                if (!isStepByStep || restart) {
                    throw interruptedException;
                }
            }
        }

        public void setRestart(boolean restart) {
            this.restart = restart;
        }
    }
}
