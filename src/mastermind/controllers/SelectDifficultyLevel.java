package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.MastermindUtils;

public class SelectDifficultyLevel {
    private String difficultyLevel;

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
        difficultyLevel = level;
        GameBoard gameBoard = MastermindUtils.loadScene(event, "/mastermind/gui/fxml/GameBoard.fxml");
        gameBoard.setSolver(difficultyLevel);
        gameBoard.setGameMode("create");
    }
}
