package gui.components;

import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;
import model.State;
import model.TuringMachineProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TuringGrid extends GridPane {

    private static final int STATE_TYPE_FONT_SIZE = 22;

    private TuringMachineProgram program;

    public TuringGrid() {
        super();
    }

    public void initialize(final TuringMachineProgram program) {
        this.program = program;
        getChildren().clear();
        setGridLinesVisible(true);

        addRowsHeaders(program.getSymbols());

        final List<State> states = program.getStates();
        for (int i = 0, statesSize = states.size(); i < statesSize; i++) {
            final State state = states.get(i);
            addStateColumn(i, state, program.getSymbols().size());
        }

        getChildren().stream().map(TuringGridCell.class::cast).forEach(cell -> cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE));
    }

    private void addStateColumn(final int columnIndex, final State state, final int columnSize) {
        List<TuringGridCell> cells = new ArrayList<>();

        if (state.getOperations().size() > 0) {
            cells = new ArrayList<>(state.getOperations().values()).stream()
                    .map(operation -> {
                        TuringGridCell turingGridCell = new TuringGridCell(operation.toString(), operation);
                        turingGridCell.initOperationEditor(program);

                        return turingGridCell;
                    })
                    .collect(Collectors.toList());
        } else {
            for (int i = 0; i < columnSize; i++) {
                cells.add(new TuringGridCell(state.getType().name().substring(0, 1), null, FontWeight.BOLD, STATE_TYPE_FONT_SIZE));
            }
        }

        cells.add(0, new TuringGridCell(state.getIdn(), null, FontWeight.BOLD));
        addColumn(columnIndex + 1, cells.toArray(new TuringGridCell[0]));
    }

    private void addRowsHeaders(final List<Character> symbols) {
        final List<TuringGridCell> headers = symbols.stream()
                .map(character -> new TuringGridCell(character.toString(), null, FontWeight.BOLD))
                .collect(Collectors.toList());

        headers.add(0, new TuringGridCell(""));

        addColumn(0, headers.toArray(new TuringGridCell[0]));
    }

}
