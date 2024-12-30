package codebreaker.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HTP2 {

    @FXML
    void onNextBtnClick(ActionEvent event) throws IOException {
        Parent selectGameModeParent = FXMLLoader.load(getClass().getResource("/codebreaker/gui/fxml/SelectGameMode.fxml"));
        Scene selectGameModeScene = new Scene(selectGameModeParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(selectGameModeScene);
        window.show();
    }

}
