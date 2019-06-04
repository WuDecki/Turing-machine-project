import gui.configuration.Config;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

public class Main extends Application {

    public static void main(final String[] args) {
        Application.launch(args);
    }

    private static void applyTheme(final Parent root) {
        new JMetro(JMetro.Style.LIGHT).applyTheme(root);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Parent root = FXMLLoader.load(Main.class.getResource(Config.Views.MAIN));
        configurePrimaryStageAndRoot(primaryStage, root);
        applyTheme(root);

        Platform.setImplicitExit(false);
        primaryStage.show();
    }

    private void configurePrimaryStageAndRoot(final Stage primaryStage, final Parent root) {
        primaryStage.setTitle(Config.App.NAME);
        final Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource(Config.Styles.MAIN).toExternalForm());

        primaryStage.setScene(scene);
    }
}
