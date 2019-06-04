package gui.animations;

import gui.components.TuringGrid;
import gui.components.TuringGridCell;
import model.State;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class TuringGridAnimation implements Runnable {

    private final Queue<MakeDecision> onMakeDecisionQueue;
    private final Queue<ChangeState> onChangeStateQueue;
    private final TuringGrid grid;
    private final Runnable afterFinish;
    private boolean running;
    private long timeoutSeconds = 1;

    public TuringGridAnimation(TuringGrid grid, Runnable afterFinish) {
        this.grid = grid;
        this.running = false;
        this.afterFinish = afterFinish;
        onMakeDecisionQueue = new ConcurrentLinkedQueue<>();
        onChangeStateQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {
        TuringGridCell cell;
        List<TuringGridCell> stateColumn;
        List<TuringGridCell> characterRow;
        running = true;

        while (running && !Thread.interrupted() && (onMakeDecisionQueue.size() > 0 || onChangeStateQueue.size() > 0)) {
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
            }
        }

        this.afterFinish.run();
    }

    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(timeoutSeconds);
        } catch (InterruptedException ignored) {
        }
    }

    public boolean isRunning() {
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

    public static class MakeDecision {
        final State actualState;
        final Character actualCharacter;

        public MakeDecision(final State actualState, final Character actualCharacter) {
            this.actualState = actualState;
            this.actualCharacter = actualCharacter;
        }
    }

    public static class ChangeState {
        final State actualState;
        final State nextState;

        public ChangeState(final State actualState, final State nextState) {
            this.actualState = actualState;
            this.nextState = nextState;
        }
    }
}
