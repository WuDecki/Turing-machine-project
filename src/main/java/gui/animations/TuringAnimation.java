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

    private TuringAnimation() {
    }

    public static Task<TuringMachineResponse> getTuringAnimation(
            final TuringMachineProgram program, final Character[] tape, final TuringGrid grid,
            final gui.components.Pommel pommel, final Ribbon ribbon, final long sleep) {
        final DefaultTuringMachineController controller = new DefaultTuringMachineController(grid, pommel, ribbon, sleep);
        final TuringMachine machine = new TuringMachine(controller);

        machine.loadProgram(program);

        return new Task<TuringMachineResponse>() {
            @Override
            protected TuringMachineResponse call() throws Exception {
                return machine.startProgram(tape);
            }
        };
    }

    static class DefaultTuringMachineController implements TuringMachineController {
        private final long sleep;
        private final TuringGrid grid;
        private final Pommel pommel;
        private final Ribbon ribbon;

        DefaultTuringMachineController(final TuringGrid grid, final gui.components.Pommel pommel, final Ribbon ribbon, final long sleep) {
            this.grid = grid;
            this.pommel = pommel;
            this.ribbon = ribbon;
            this.sleep = sleep;
        }

        @Override
        public void onMakeDecision(State actualState, Character actualCharacter) throws InterruptedException {
            System.out.println(String.format("onMakeDecision %s %c", actualState, actualCharacter));

            final List<TuringGridCell> stateColumn = grid.getStateColumn(actualState);
            final List<TuringGridCell> characterRow = grid.getCharacterRow(actualCharacter);
            final TuringGridCell cell = grid.getCell(actualState, actualCharacter);

            grid.addStateHighlight(stateColumn);

            Thread.sleep(sleep);

            grid.addCharacterHighlight(characterRow);
            grid.addCellHighlight(cell);

            Thread.sleep(sleep);

            grid.removeStateHighlight(stateColumn);
            grid.removeCharacterHighlight(characterRow);
            grid.removeCellHighlight(cell);
        }

        @Override
        public void onChangeState(State actualState, State nextState) throws InterruptedException {
            System.out.println(String.format("onChangeState %s %s", actualState, nextState));

            final List<TuringGridCell> stateColumn = grid.getStateColumn(nextState);

            grid.addStateHighlight(stateColumn);

            Thread.sleep(sleep);

            grid.removeStateHighlight(stateColumn);
        }

        @Override
        public void onProcessOperation(Operation operation) throws InterruptedException {
            System.out.println(String.format("onProcessOperation %s %s", operation.getNewChar(), operation.getMovement()));
//
            Platform.runLater(() -> ribbon.setCharacter(pommel.getActualPosition(), operation.getNewChar()));

            Thread.sleep(sleep);

            pommel.move(ribbon, operation.getMovement());
        }
    }
}
