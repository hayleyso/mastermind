package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.MastermindUtils;
import mastermind.core.GameState;

public class SelectDifficultyLevel {
    private GameState gameState = GameState.getInstance();

    @FXML
    void onEasyLevelClick(ActionEvent event) throws IOException {
        handleDifficulty(event, "easy");
    }

    @FXML
    void onMediumLevelClick(ActionEvent event) throws IOException {
        handleDifficulty(event, "medium");
    }

    @FXML
    void onHardLevelClick(ActionEvent event) throws IOException {
        handleDifficulty(event, "hard");
    }

    private void handleDifficulty(ActionEvent event, String level) throws IOException {
        MastermindUtils.loadScene(event, "/mastermind/gui/fxml/GameBoard.fxml");
        gameState.setDifficultyLevel(level);  
        gameState.setGameMode("create");
    }
}
