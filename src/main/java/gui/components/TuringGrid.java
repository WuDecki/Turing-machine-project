package gui.components;

import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;
import model.Operation;
import model.State;
import model.StateType;
import model.TuringMachineProgram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TuringGrid extends GridPane {

    private static final int STATE_TYPE_FONT_SIZE = 22;
    private static final String HIGHLIGHT_STATE_CLASS = "highlight-state";
    private static final String HIGHLIGHT_CHARACTER_CLASS = "highlight-character";
    private static final String HIGHLIGHT_CELL_CLASS = "highlight-cell";

    private TuringMachineProgram program;
    private TuringGridCell[][] cells;

    public TuringGrid() {
        super();
    }

    public void initialize(final TuringMachineProgram program) {
        this.program = program;
        getChildren().clear();
        setGridLinesVisible(true);
        initializeCells(program);

        for (int i = 0; i < program.getStates().size() + 1; i++) {
            addColumn(i, cells[i]);
        }

        getChildren().stream()
                .map(TuringGridCell.class::cast)
                .forEach(cell -> cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE));
    }

    private void initializeCells(final TuringMachineProgram program) {
        List<State> states = program.getStates();
        List<Character> symbols = program.getSymbols();

        cells = new TuringGridCell[states.size() + 1][symbols.size() + 1];

        // Corner empty cell
        cells[0][0] = new TuringGridCell("");

        // Columns headers
        for (int i = 1; i < states.size() + 1; i++) {
            cells[i][0] = new TuringGridCell(states.get(i - 1).getIdn(), null, FontWeight.BOLD);
        }

        // Row headers
        for (int i = 1; i < symbols.size() + 1; i++) {
            cells[0][i] = new TuringGridCell(symbols.get(i - 1).toString(), null, FontWeight.BOLD);
        }

        // Cells
        State state;
        Operation operation;
        for (int i = 1; i < states.size() + 1; i++) {
            state = states.get(i - 1);
            for (int j = 1; j < symbols.size() + 1; j++) {
                operation = state.getOperation(symbols.get(j - 1));

                if (state.getType().equals(StateType.NORMAL)) {
                    cells[i][j] = new TuringGridCell(operation.toString(), operation);
                    cells[i][j].initOperationEditor(program);
                } else {
                    cells[i][j] = new TuringGridCell(state.getType().name().substring(0, 1), null, FontWeight.BOLD, STATE_TYPE_FONT_SIZE);
                }

                cells[i][j].setState(state);
            }
        }
    }

    synchronized public void addCellHighlight(TuringGridCell cell) {
        cell.getStyleClass().add(HIGHLIGHT_CELL_CLASS);
    }

    synchronized public void removeCellHighlight(TuringGridCell cell) {
        cell.getStyleClass().remove(HIGHLIGHT_CELL_CLASS);
    }

    synchronized public TuringGridCell getCell(State wantedState, Character character) {
        for (int i = 0; i < cells[0].length; i++) {
            if (cells[0][i].getText().equals(character.toString())) {
                for (TuringGridCell[] cell : cells) {
                    State state = cell[i].getState();
                    if (state != null && state.equals(wantedState)) {
                        return cell[i];
                    }
                }

                break;
            }
        }

        return null;
    }

    public void addStateHighlight(final List<TuringGridCell> cells) {
        addHighlight(cells, HIGHLIGHT_STATE_CLASS);
    }

    public void removeStateHighlight(final List<TuringGridCell> cells) {
        removeHighlight(cells, HIGHLIGHT_STATE_CLASS);
    }

    synchronized public void addCharacterHighlight(final List<TuringGridCell> cells) {
        addHighlight(cells, HIGHLIGHT_CHARACTER_CLASS);
    }

    public void removeCharacterHighlight(final List<TuringGridCell> cells) {
        removeHighlight(cells, HIGHLIGHT_CHARACTER_CLASS);
    }

    synchronized private void addHighlight(final List<TuringGridCell> cells, String styleClass) {
        cells.stream()
                .map(TuringGridCell::getStyleClass)
                .forEach(styles -> styles.add(styleClass));
    }

    synchronized private void removeHighlight(final List<TuringGridCell> cells, String styleClass) {
        cells.stream()
                .map(TuringGridCell::getStyleClass)
                .forEach(styles -> styles.remove(styleClass));
    }

    synchronized public List<TuringGridCell> getStateColumn(final State state) {
        final ArrayList<TuringGridCell> stateColumn = new ArrayList<>();

        TuringGridCell[][] cells = this.cells;
        for (int i = 1; i < cells.length; i++) {
            TuringGridCell[] column = cells[i];

            if (column[0].getText().equals(state.getIdn())) {
                stateColumn.addAll(Arrays.asList(column));

                break;
            }
        }

        return stateColumn;
    }

    synchronized public List<TuringGridCell> getCharacterRow(final Character character) {
        final ArrayList<TuringGridCell> row = new ArrayList<>();

        TuringGridCell[][] cells = this.cells;
        for (int i = 0; i < cells[0].length; i++) {
            TuringGridCell cell = cells[0][i];

            if (cell.getText().equals(character.toString())) {
                for (TuringGridCell[] turingGridCells : cells) {
                    row.add(turingGridCells[i]);
                }

                break;
            }
        }

        return row;
    }

    synchronized public void clearHighlights() {
        for (TuringGridCell[] column : cells) {
            for (TuringGridCell cell : column) {
                cell.getStyleClass().removeAll(HIGHLIGHT_CELL_CLASS, HIGHLIGHT_CHARACTER_CLASS, HIGHLIGHT_STATE_CLASS);
            }
        }
    }
}
