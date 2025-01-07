package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.MastermindUtils;

public class SelectDifficultyLevel {
    private String difficultyLevel;
  
    @FXML
    void onEasyLevelClick(ActionEvent event) throws IOException {
        GameBoard gameBoard = MastermindUtils.loadScene(event, "/mastermind/gui/fxml/GameBoard.fxml");
        gameBoard.setSolver(difficultyLevel);
    }

    @FXML
    void onMediumLevelClick(ActionEvent event) throws IOException {
        difficultyLevel = "medium";
        MastermindUtils.loadScene(event, "/mastermind/gui/fxml/GameBoard.fxml");

    }

    @FXML
    void onHardLevelClick(ActionEvent event) throws IOException {
        difficultyLevel = "hard";
        MastermindUtils.loadScene(event, "/mastermind/gui/fxml/GameBoard.fxml");
    }
}
