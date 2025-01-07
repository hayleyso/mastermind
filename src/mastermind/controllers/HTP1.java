package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.MastermindUtils;


public class HTP1 {

    @FXML
    void onNextBtnClick(ActionEvent event) throws IOException {
        MastermindUtils.loadScene(event, "/mastermind/gui/fxml/HTP2.fxml");
    }

}
