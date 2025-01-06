package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.utils.SceneLoader;

public class SelectDifficultyLevel {
    private String difficultyLevel;
  
    @FXML
    void onEasyLevelClick(ActionEvent event) throws IOException {
        GameBoard gameBoard = SceneLoader.loadPage(event, "/mastermind/gui/fxml/GameBoard.fxml");
        gameBoard.setSolver(difficultyLevel);
    }

    @FXML
    void onMediumLevelClick(ActionEvent event) throws IOException {
        difficultyLevel = "medium";
        SceneLoader.loadPage(event, "/mastermind/gui/fxml/GameBoard.fxml");

    }

    @FXML
    void onHardLevelClick(ActionEvent event) throws IOException {
        difficultyLevel = "hard";
        SceneLoader.loadPage(event, "/mastermind/gui/fxml/GameBoard.fxml");
    }
}
