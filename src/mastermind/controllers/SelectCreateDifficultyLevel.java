package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;
import mastermind.core.State;

public class SelectCreateDifficultyLevel {

    @FXML
    void onCreateEasyLevelClick(ActionEvent event) throws IOException {
        handleCreateDifficulty(event, "easy");
    }

    @FXML
    void onCreateMediumLevelClick(ActionEvent event) throws IOException {
        handleCreateDifficulty(event, "medium");
    }

    @FXML
    void onCreateHardLevelClick(ActionEvent event) throws IOException {
        handleCreateDifficulty(event, "hard");
    }

    private void handleCreateDifficulty(ActionEvent event, String createLevel) throws IOException {
        State.getInstance().setCreateDifficultyLevel(createLevel);  
        Utils.loadScene(event, "/mastermind/gui/fxml/GameBoard.fxml");
    }
}
