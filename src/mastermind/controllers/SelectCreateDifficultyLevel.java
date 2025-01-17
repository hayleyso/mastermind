/*
    Author: Hayley So
    Title: SelectCreateDifficulty.java
    Date: 2025-01-15
 */
package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;
import mastermind.core.State;

/**
 * Controller class for the select create difficulty level screen.
 */
public class SelectCreateDifficultyLevel {

    /**
     * Handles the event where the user clicks the easy level button.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onCreateEasyLevelClick(ActionEvent event) throws IOException {
        handleCreateDifficulty(event, "easy");
    }

    /**
     * Handles the event where the user clicks the medium level button.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onCreateMediumLevelClick(ActionEvent event) throws IOException {
        handleCreateDifficulty(event, "medium");
    }

    /**
     * Handles the event where the user selects the hard level button.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onCreateHardLevelClick(ActionEvent event) throws IOException {
        handleCreateDifficulty(event, "hard");
    }

    /**
     * Loads the game board scene with the selected create difficulty level.
     * 
     * @param event
     * @param createLevel
     * @throws IOException
     */
    private void handleCreateDifficulty(ActionEvent event, String createLevel) throws IOException {
        State.getInstance().setCreateDifficultyLevel(createLevel);  
        Utils.loadScene(event, "/mastermind/gui/fxml/GameBoard.fxml");
    }
}
