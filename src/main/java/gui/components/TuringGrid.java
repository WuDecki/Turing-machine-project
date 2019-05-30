package gui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;
import model.State;
import model.TuringMachineProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TuringGrid extends GridPane {

    private static final int STATE_TYPE_FONT_SIZE = 22;
    private static final String STATE_ID_PREFIX = "S";

    private TuringMachineProgram program;

    public TuringGrid() {
        super();

        setGridLinesVisible(true);
    }

    public void initialize(final TuringMachineProgram program) {
        this.program = program;

        addColumn(0, getRowsHeaders(program.getSymbols()));

        for (final State state : program.getStates()) {
            addStateColumn(state, program.getSymbols().size());
        }
    }

    private void addStateColumn(final State state, final int columnSize) {
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

        cells.add(0, new TuringGridCell(STATE_ID_PREFIX + state.getId(), null, FontWeight.BOLD));
        addColumn(state.getId() + 1, cells.toArray(new TuringGridCell[0]));
    }

    private Label[] getRowsHeaders(final List<Character> symbols) {
        final List<TuringGridCell> headers = symbols.stream()
                .map(character -> new TuringGridCell(character.toString(), null, FontWeight.BOLD))
                .collect(Collectors.toList());

        headers.add(0, new TuringGridCell(""));

        return headers.toArray(new TuringGridCell[0]);
    }

}
