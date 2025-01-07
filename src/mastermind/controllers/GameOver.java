package mastermind.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.MastermindUtils;

public class GameOver {

    @FXML 
    void onPlayAgainBtnClick(ActionEvent event) throws IOException {
        MastermindUtils.loadScene(event, "/mastermind/gui/fxml/SelectGameMode.fxml");
    }

    @FXML
    void onExitBtnClick(ActionEvent event) throws IOException {
        MastermindUtils.loadScene(event, "/mastermind/gui/fxml/StartMenu.fxml");
    }

}
