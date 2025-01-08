package mastermind.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;

public class GameOver {

    @FXML 
    void onPlayAgainBtnClick(ActionEvent event) throws IOException {
        Utils.loadScene(event, "/mastermind/gui/fxml/SelectGameMode.fxml");
    }

    @FXML
    void onExitBtnClick(ActionEvent event) throws IOException {
        Utils.loadScene(event, "/mastermind/gui/fxml/StartMenu.fxml");
    }

}
