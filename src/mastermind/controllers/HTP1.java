package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;

public class HTP1 {

    @FXML
    void onNextBtnClick(ActionEvent event) throws IOException {
        Utils.loadScene(event, "/mastermind/gui/fxml/HTP2.fxml");
    }

}
