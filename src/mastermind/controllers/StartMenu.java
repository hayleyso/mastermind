package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.utils.SceneLoader;

public class StartMenu {
    @FXML
    void onPlayBtnClick(ActionEvent event) throws IOException {
        SceneLoader.loadPage(event, "/mastermind/gui/fxml/EnterUsername.fxml");
    }
}
