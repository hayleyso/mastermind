package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.utils.SceneLoader;

public class SelectGameMode {

    @FXML
    void onGuessModeClick(ActionEvent event) throws IOException {
        GameBoard gameBoard = SceneLoader.loadPage(event, "/mastermind/gui/fxml/GameBoard.fxml");
        gameBoard.setGameMode("guess");
    }
    
    @FXML
    void onCreateModeClick(ActionEvent event) throws IOException {
        GameBoard gameBoard = SceneLoader.loadPage(event, "/mastermind/gui/fxml/SelectDifficultyLevel.fxml");
        gameBoard.setGameMode("create");
    }

}
