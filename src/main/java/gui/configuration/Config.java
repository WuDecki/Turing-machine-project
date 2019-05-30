package gui.configuration;

public interface Config {
    interface App {
        String NAME = "Turing Machine Visualization";
    }

    interface Views {
        String MAIN = "/views/main.fxml";
    }

    interface Nodes {
        String TURING_GRID_CELL_EDITOR = "/nodes/turing-grid-cell-editor.fxml";
    }

    interface Styles {
        String MAIN = "/style.css";
    }
}
