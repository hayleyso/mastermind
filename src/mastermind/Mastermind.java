/*
    Author: Hayley So
    Title: Mastermind.java
    Date: 2025-01-15
 */
package mastermind;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/** 
 * Main class for the Mastermind game.
 * Sets up the initial GUI scene and game window.
 */
public class Mastermind extends Application {

    /**
     * The number of colors used in the game.*/
    public final static int NUM_COLORS = 6;

    /** The length of the secret code. */
    public final static int CODE_LENGTH = 4;

    /** The maximum number of guesses allowed. */
    public final static int NUM_GUESSES = 10;

    /** Path to the application icon image. */
    public final static String ICON_PATH = "/mastermind/gui/img/icons/logo.png";

    /**
     * Sets up the Mastermind game by loading the start menu and initializing the main window.
     * 
     * @param primaryStage The primary stage for the game.
     * @throws Exception If there's an issue loading the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("gui/fxml/StartMenu.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Mastermind");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_PATH)));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    /**
     * Launches the game.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
