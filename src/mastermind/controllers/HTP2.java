/*
    Author: Hayley So
    Title: HTP2.java
    Date: 2025-01-15
 */
package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;

/**
 * Controller class for the game instructions (2/2)
 */
public class HTP2 {

    /**
     * Handles the event where the next button is clicked.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onNextBtnClick(ActionEvent event) throws IOException {
        Utils.loadScene(event, "/mastermind/gui/fxml/SelectGameMode.fxml");
    }

}
