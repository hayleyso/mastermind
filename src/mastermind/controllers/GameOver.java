/*
    Author: Hayley So
    Title: GameOver.java
    Date: 2025-01-15
 */
package mastermind.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;

/**
 * Controller class that is displayed after a game is finished.
 */
public class GameOver {

    /**
     * Handles the event where the user clicks the play again button.
     * 
     * @param event
     * @throws IOException
     */
    @FXML 
    void onPlayAgainBtnClick(ActionEvent event) throws IOException {
        Utils.loadScene(event, "/mastermind/gui/fxml/SelectGameMode.fxml");
    }

    /**
     * Handles the event where the user clicks the exit button.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onExitBtnClick(ActionEvent event) throws IOException {
        Utils.loadScene(event, "/mastermind/gui/fxml/StartMenu.fxml");
    }

}
