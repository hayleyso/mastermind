/*
    Author: Hayley So
    Title: SelectGameMode.java
    Date: 2025-01-15
 */
package mastermind.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;
import mastermind.core.State;

/**
 * Controller class for selecting the game modes: guess or create.
 */
public class SelectGameMode {

    /**
     * Handles the event where the user clicks the guess mode button.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onGuessModeClick(ActionEvent event) throws IOException {
        State.getInstance().setGameMode("guess");
        Utils.loadScene(event, "/mastermind/gui/fxml/SelectGuessDifficultyLevel.fxml");
    }
    
    /**
     * Handles the event where the user clicks the create mode button.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onCreateModeClick(ActionEvent event) throws IOException {
        State.getInstance().setGameMode("create");
        Utils.loadScene(event, "/mastermind/gui/fxml/SelectCreateDifficultyLevel.fxml");
    }
}
