package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.MastermindUtils;

public class StartMenu {
    @FXML
    void onPlayBtnClick(ActionEvent event) throws IOException {
        MastermindUtils.loadScene(event, "/mastermind/gui/fxml/EnterUsername.fxml");
    }
}
