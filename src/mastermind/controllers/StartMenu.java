package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;

public class StartMenu {
    @FXML
    void onPlayBtnClick(ActionEvent event) throws IOException {
        Utils.loadScene(event, "/mastermind/gui/fxml/EnterUsername.fxml");
    }
}
