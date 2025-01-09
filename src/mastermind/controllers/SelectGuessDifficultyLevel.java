package mastermind.controllers;

import java.io.IOException;
import mastermind.core.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;

public class SelectGuessDifficultyLevel {

    @FXML
    void onGuessEasyLevelClick(ActionEvent event) throws IOException {
        handleGuessDifficulty(event, "easy");
    }

    @FXML
    void onGuessMediumLevelClick(ActionEvent event) throws IOException {
        handleGuessDifficulty(event, "medium");
    }

    @FXML
    void onGuessHardLevelClick(ActionEvent event) throws IOException {
        handleGuessDifficulty(event, "hard");
    }

    private void handleGuessDifficulty(ActionEvent event, String guessLevel) throws IOException {
        State.getInstance().setGuessDifficultyLevel(guessLevel);  
        Utils.loadScene(event, "/mastermind/gui/fxml/GameBoard.fxml");
    }

}
