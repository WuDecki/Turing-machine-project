package gui.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import model.Operation;
import model.State;
import model.TuringMachineProgram;
import org.controlsfx.control.PopOver;

public class TuringGridCell extends Label {
    private static final String STYLE_CLASS_TURING_GRID_CELL_EDITOR_ENABLED = "turing-grid-cell-editor-enabled";
    private static final Insets DEFAULT_CELL_PADDING = new Insets(0, 0, 0, 0);
    private final Operation operation;
    private State state;

    public TuringGridCell(final String text, final Operation operation, final FontWeight fontWeight, final double fontSize, final Insets padding) {
        super(text);

        setFont(Font.font(Font.getDefault().getFamily(), fontWeight, fontSize));
        setTextAlignment(TextAlignment.CENTER);
        setPadding(padding);
        this.operation = operation;
        state = null;
    }

    public TuringGridCell(final String text, final Operation operation, final FontWeight fontWeight, final double fontSize) {
        this(text, operation, fontWeight, fontSize, DEFAULT_CELL_PADDING);
    }

    public TuringGridCell(final String text, final Operation operation, final FontWeight fontWeight) {
        this(text, operation, fontWeight, 16, DEFAULT_CELL_PADDING);
    }

    public TuringGridCell(final String text, final Operation operation) {
        this(text, operation, FontWeight.NORMAL, 16, DEFAULT_CELL_PADDING);
    }

    public TuringGridCell(final String text) {
        this(text, null, FontWeight.NORMAL, 16, DEFAULT_CELL_PADDING);
    }

    public void initOperationEditor(final TuringMachineProgram program) {
        final PopOver popOver = new PopOver();
        final TuringGridCellEditor editor = new TuringGridCellEditor(program, operation);

        editor.saveButton.setOnAction(event -> popOver.hide());

        popOver.setContentNode(editor);
        popOver.setHeaderAlwaysVisible(true);
        popOver.setTitle("Edit");

        popOver.setOnHiding(event -> {
            operation.setNewChar(editor.characterComboBox.getValue());
            operation.setMovement(editor.pommelMovementComboBox.getValue());
            operation.setNextState(editor.nextStateComboBox.getValue());

            setText(operation.toString());
            toggleEditorEnabled();
        });

        setOnMouseClicked(event -> {
            popOver.show(this);
            toggleEditorEnabled();
        });
    }

    private void toggleEditorEnabled() {
        if (getStyleClass().contains(STYLE_CLASS_TURING_GRID_CELL_EDITOR_ENABLED)) {
            getStyleClass().remove(STYLE_CLASS_TURING_GRID_CELL_EDITOR_ENABLED);
        } else {
            getStyleClass().add(STYLE_CLASS_TURING_GRID_CELL_EDITOR_ENABLED);
        }
    }

    public Operation getOperation() {
        return operation;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }
}
