/*
    Author: Hayley So
    Title: EnterUsername.java
    Date: 2025-01-15
 */
package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mastermind.Mastermind;
import mastermind.Utils;
import mastermind.core.State;

/**
 * Controller class for handling username input and determines whether the user has an unfinished game
 * and wants to resume or start a new game.
 */
public class EnterUsername {

    @FXML
    private TextField usernameField; 
    @FXML
    private Button submit;
    private String username; 

    /**
     * Initializes the EnterUsername screen, setting up the submit button's state based on the username field input.
     * The submit button is only enabled if the username is between 6 and 16 characters long.
     */
    @FXML
    void initialize() {
        submit.setDisable(true); // Initially, disable the submit button.
        
        // Listener for changes in the username field.
        usernameField.textProperty().addListener((_, _, newValue) -> {
            // Enable or disable the submit button based on the length of the username.
            if (newValue.isEmpty() || newValue.length() < 6 || newValue.length() > 16) {
                submit.setDisable(true); 
            } else {
                submit.setDisable(false); 
            }
        });
    }

    /**
     * Handles the submit action when the user clicks the submit button.
     * If an unfinished game is found for the entered username, a popup window is displayed to choose
     * whether to resume the old game or start a new one.
     * If no unfinished game exists, the user is redirected to the game overview screen.
     * 
     * @param event The ActionEvent triggered by the submit button.
     * @throws IOException If there is an issue loading the next scene or popup.
     */
    @FXML
    void submit(ActionEvent event) throws IOException {
        submit.setDisable(true); 

        // Retrieve the entered username
        username = usernameField.getText(); 

        // Set the username in the global game state
        State.getInstance().setUsername(username); 

        // Check if there's an unfinished game for the given username
        if (Utils.hasUnfinishedGame(username)) {
            showPopup(event); 
        } else {
            // If no unfinished game exists, load the game overview scene
            Utils.loadScene(event, "/mastermind/gui/fxml/GameOverview.fxml");
        }
    }

    /**
     * Displays a popup window that allows the user to choose between resuming their unfinished game or starting a new one.
     * 
     * @param event The ActionEvent triggered by the submit button.
     * @throws IOException If there is an issue loading the popup scene.
     */
    private void showPopup(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastermind/gui/fxml/PopupWindow.fxml"));
        Parent popupContent = loader.load();
        Stage popupStage = new Stage(); 

        // Configure the popup stage
        popupStage.initModality(Modality.APPLICATION_MODAL); 
        popupStage.initOwner(((Button) event.getSource()).getScene().getWindow()); 
        popupStage.setScene(new Scene(popupContent)); 
        popupStage.setTitle("Mastermind"); 
        popupStage.getIcons().add(new Image(getClass().getResourceAsStream(Mastermind.ICON_PATH))); 
        popupStage.setResizable(false); 
        popupStage.showAndWait();
    }
}
