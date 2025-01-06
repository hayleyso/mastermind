package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import mastermind.utils.SceneLoader;

public class HTP1 {

    @FXML
    void onNextBtnClick(ActionEvent event) throws IOException {
        SceneLoader.loadPage(event, "/mastermind/gui/fxml/HTP2.fxml");
    }

}
