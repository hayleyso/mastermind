/*
    Author: Hayley So
    Title: PopupWindow.java
    Date: 2025-01-15
 */
package mastermind.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mastermind.Utils;
import mastermind.core.State;

/**
 * Controller class for the popup window for returning players with unfinished games. 
 * Allows the option to return or start a new game.
 */
public class PopupWindow {
    @FXML
    private Button newGameButton;
    @FXML
    private Button returnButton;
    @FXML
    private Text text;

    /**
     * Displays welcome message
     */
    @FXML
    void initialize() {
        text.setText("Welcome back, " + State.getInstance().getUsername() + "!");
    }

    /**
     * Handles the event where the user clicks the new game button. 
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onNewGameBtnClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) newGameButton.getScene().getWindow();
        stage.close();
        Utils.loadScene(event, "/mastermind/gui/fxml/SelectGameMode.fxml");
        Utils.deleteGameState(State.getInstance().getUsername());
    }

    /**
     * Hanldes the event where the user clicks the return to game button.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onReturnBtnClick(ActionEvent event) throws IOException {
        State.getInstance().setGameFinished(false);
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    
        String username = State.getInstance().getUsername();
        String[] gameState = Utils.loadGameState(username);
        String mode = gameState[0];
        State.getInstance().setGameMode(mode);
            
        if ("create".equals(mode)) {
            State.getInstance().setCreateDifficultyLevel(gameState[1]);
        }
        
        State.getInstance().setGameFinished(false);
        Utils.loadScene(event, "/mastermind/gui/fxml/GameBoard.fxml");
    }    
}
