package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.utils.SceneLoader;

public class GameOverview {

    @FXML
    void onNextBtnClick(ActionEvent event) throws IOException {
        SceneLoader.loadPage(event, "/mastermind/gui/fxml/HTP1.fxml");
    }

}
