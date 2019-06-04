package gui.animations;

import gui.components.Pommel;
import gui.components.Ribbon;
import gui.components.TuringGrid;
import gui.components.TuringGridCell;
import javafx.application.Platform;
import javafx.concurrent.Task;
import model.Operation;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class TuringAnimation extends Task<Void> {

    private final Queue<MakeDecision> onMakeDecisionQueue;
    private final Queue<ChangeState> onChangeStateQueue;
    private final Queue<Operation> onProcessOperationQueue;
    private final TuringGrid grid;
    private final Pommel pommel;
    private final Ribbon ribbon;
    private final Runnable afterFinish;
    private boolean running;
    private long timeoutSeconds = 1;

    public TuringAnimation(TuringGrid grid, Pommel pommel, Ribbon ribbon, Runnable afterFinish) {
        this.grid = grid;
        this.pommel = pommel;
        this.ribbon = ribbon;
        this.running = false;
        this.afterFinish = afterFinish;
        onMakeDecisionQueue = new ConcurrentLinkedQueue<>();
        onChangeStateQueue = new ConcurrentLinkedQueue<>();
        onProcessOperationQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    protected Void call() throws Exception {
        TuringGridCell cell;
        List<TuringGridCell> stateColumn;
        List<TuringGridCell> characterRow;
        running = true;

        while (running && !Thread.interrupted() && (onMakeDecisionQueue.size() > 0 || onChangeStateQueue.size() > 0 || onProcessOperationQueue.size() > 0)) {
            synchronized (this) {
                MakeDecision decision = onMakeDecisionQueue.poll();
                if (decision != null) {
                    stateColumn = grid.getStateColumn(decision.actualState);
                    characterRow = grid.getCharacterRow(decision.actualCharacter);
                    cell = grid.getCell(decision.actualState, decision.actualCharacter);

                    grid.addStateHighlight(stateColumn);

                    sleep();

                    grid.addCharacterHighlight(characterRow);
                    grid.addCellHighlight(cell);

                    sleep();

                    grid.removeStateHighlight(stateColumn);
                    grid.removeCharacterHighlight(characterRow);
                    grid.removeCellHighlight(cell);
                }

                ChangeState stateChange = onChangeStateQueue.poll();
                if (stateChange != null) {
                    stateColumn = grid.getStateColumn(stateChange.nextState);

                    grid.addStateHighlight(stateColumn);

                    sleep();

                    grid.removeStateHighlight(stateColumn);
                }

                Operation operation = onProcessOperationQueue.poll();
                if (operation != null) {
                    Platform.runLater(() -> ribbon.setCharacter(pommel.getActualPosition(), operation.getNewChar()));

                    sleep();
                    pommel.move(ribbon, operation.getMovement());
                }

                if (!running) {
                    grid.clearHighlights();
                }
            }
        }

        running = false;

        this.afterFinish.run();

        return null;
    }

    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(timeoutSeconds);
        } catch (InterruptedException ignored) {
        }
    }

    public boolean isTaskRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public TuringGrid getGrid() {
        return grid;
    }

    public void setTimeoutSeconds(long timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public Queue<MakeDecision> getOnMakeDecisionQueue() {
        return onMakeDecisionQueue;
    }

    public Queue<ChangeState> getOnChangeStateQueue() {
        return onChangeStateQueue;
    }

    public Queue<Operation> getOnProcessOperationQueue() {
        return onProcessOperationQueue;
    }

    public static class MakeDecision {
        final model.State actualState;
        final Character actualCharacter;

        public MakeDecision(final model.State actualState, final Character actualCharacter) {
            this.actualState = actualState;
            this.actualCharacter = actualCharacter;
        }
    }

    public static class ChangeState {
        final model.State actualState;
        final model.State nextState;

        public ChangeState(final model.State actualState, final model.State nextState) {
            this.actualState = actualState;
            this.nextState = nextState;
        }
    }
}
