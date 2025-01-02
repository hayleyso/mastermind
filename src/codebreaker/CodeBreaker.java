package codebreaker;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class CodeBreaker extends Application {
    public final static int NUM_COLORS = 6;
    public final static int CODE_LENGTH = 4;
    public final static int MAX_GUESSES = 10;

    @Override
    public void start(Stage primaryStage) throws IOException {
        
        // Parent root = FXMLLoader.load(getClass().getResource("gui/fxml/StartMenu.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("gui/fxml/GameBoard.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Code Breaker");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
