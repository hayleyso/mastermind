package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;
import mastermind.core.State;

public class SelectGameMode {

    @FXML
    void onGuessModeClick(ActionEvent event) throws IOException {
        State.getInstance().setGameMode("guess");
        Utils.loadScene(event, "/mastermind/gui/fxml/GameBoard.fxml");
    }
    
    @FXML
    void onCreateModeClick(ActionEvent event) throws IOException {
        State.getInstance().setGameMode("create");
        Utils.loadScene(event, "/mastermind/gui/fxml/SelectDifficultyLevel.fxml");
    }
}
