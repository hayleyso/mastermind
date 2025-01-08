package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.MastermindUtils;
import mastermind.core.State;

public class SelectGameMode {

    @FXML
    void onGuessModeClick(ActionEvent event) throws IOException {
        State.getInstance().setGameMode("guess");
        MastermindUtils.loadScene(event, "/mastermind/gui/fxml/GameBoard.fxml");
    }
    
    @FXML
    void onCreateModeClick(ActionEvent event) throws IOException {
        State.getInstance().setGameMode("create");
        MastermindUtils.loadScene(event, "/mastermind/gui/fxml/SelectDifficultyLevel.fxml");
    }
}
