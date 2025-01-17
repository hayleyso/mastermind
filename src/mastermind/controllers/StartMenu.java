/*
    Author: Hayley So
    Title: StartMenu.java
    Date: 2025-01-15
 */
package mastermind.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mastermind.Utils;

/**
 * Controller class for the start menu.
 */
public class StartMenu {
    
    /**
     * Handles the event when the user clicks the play button.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void onPlayBtnClick(ActionEvent event) throws IOException {
        Utils.loadScene(event, "/mastermind/gui/fxml/EnterUsername.fxml");
    }
}
