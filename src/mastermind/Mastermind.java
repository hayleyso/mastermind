package mastermind;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Mastermind extends Application {
    public final static int NUM_COLORS = 6;
    public final static int CODE_LENGTH = 4;
    public final static int NUM_GUESSES = 10;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("gui/fxml/StartMenu.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Mastermind - Online Edition");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/mastermind/gui/img/icons/logo.png")));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
