/*
    Author: Hayley So
    Title: HTP1.java
    Date: 2025-01-15
 */
package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;

/**
 * Controller class for the game instructions (1/2)
 */
public class HTP1 {

    /**
     * Handles the event where the usr clicks the next button.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onNextBtnClick(ActionEvent event) throws IOException {
        Utils.loadScene(event, "/mastermind/gui/fxml/HTP2.fxml");
    }

}
