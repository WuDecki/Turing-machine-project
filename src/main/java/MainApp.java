import gui.StaticContext;
import gui.configuration.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

public class MainApp extends Application {

    public static void main(final String[] args) {
        Application.launch(args);
    }

    private static void applyTheme(final Parent root) {
        new JMetro(JMetro.Style.LIGHT).applyTheme(root);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Parent root = FXMLLoader.load(MainApp.class.getResource(Config.Views.MAIN));
        configurePrimaryStageAndRoot(primaryStage, root);
        applyTheme(root);

        StaticContext.stage = primaryStage;
        primaryStage.show();
    }

    private void configurePrimaryStageAndRoot(final Stage primaryStage, final Parent root) {
        primaryStage.setTitle(Config.App.NAME);
        final Scene scene = new Scene(root);
        scene.getStylesheets().add(MainApp.class.getResource(Config.Styles.MAIN).toExternalForm());

        primaryStage.setScene(scene);
    }
}
