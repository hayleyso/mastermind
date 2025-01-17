/*
    Author: Hayley So
    Title: SelectGuessDifficultyLevel.java
    Date: 2025-01-15
 */
package mastermind.controllers;

import java.io.IOException;
import mastermind.core.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;

/**
 * Controller class for the select guess difficulty level screen.
 */
public class SelectGuessDifficultyLevel {

    /**
     * Handles the event when the user clicks the easy level button.
     * 
     * @param event 
     * @throws IOException
     */
    @FXML
    void onGuessEasyLevelClick(ActionEvent event) throws IOException {
        handleGuessDifficulty(event, "easy");
    }

    /**
     * Handles the event when the user clicks the medium level button.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onGuessMediumLevelClick(ActionEvent event) throws IOException {
        handleGuessDifficulty(event, "medium");
    }

    /**
     * Handles the event when the user clicks the hard level button.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onGuessHardLevelClick(ActionEvent event) throws IOException {
        handleGuessDifficulty(event, "hard");
    }

    /**
     * Loads the game board scene with the selected guess difficulty level.
     * 
     * @param event
     * @param guessLevel
     * @throws IOException
     */
    private void handleGuessDifficulty(ActionEvent event, String guessLevel) throws IOException {
        State.getInstance().setGuessDifficultyLevel(guessLevel);  
        Utils.loadScene(event, "/mastermind/gui/fxml/GameBoard.fxml");
    }

}
