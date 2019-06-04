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
    private long timeoutSeconds = 1;

    public TuringGridAnimation(TuringGrid grid) {
        this.grid = grid;
        onMakeDecisionQueue = new ConcurrentLinkedQueue<>();
        onChangeStateQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {
        TuringGridCell cell;
        List<TuringGridCell> stateColumn;
        List<TuringGridCell> characterRow;

        while (onMakeDecisionQueue.size() > 0 || onChangeStateQueue.size() > 0) {
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

    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(timeoutSeconds);
        } catch (InterruptedException ignored) {
        }
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
