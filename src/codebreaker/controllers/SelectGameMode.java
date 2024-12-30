package codebreaker.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SelectGameMode {
    private String gameMode;

    @FXML
    void onGuessModeClick(ActionEvent event) {
        gameMode = "guess";

    }
    
    @FXML
    void onCreateModeClick(ActionEvent event) throws IOException {
       gameMode = "create";
       Parent selectDifficultyParent = FXMLLoader.load(getClass().getResource("/codebreaker/gui/fxml/SelectDifficultyLevel.fxml"));
       Scene selectDifficultyScene = new Scene(selectDifficultyParent);
       Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
       
       window.setScene(selectDifficultyScene);
       window.show();
    }


}

