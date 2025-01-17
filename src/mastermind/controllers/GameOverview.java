/*
    Author: Hayley So
    Title: GameOverview.java
    Date: 2025-01-15
 */
package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;

/**
 * Controller class for game overview information.
 */
public class GameOverview {

    /**
     * Handles the event where the next button is clicked.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onNextBtnClick(ActionEvent event) throws IOException {
        Utils.loadScene(event, "/mastermind/gui/fxml/HTP1.fxml");
    }

}
