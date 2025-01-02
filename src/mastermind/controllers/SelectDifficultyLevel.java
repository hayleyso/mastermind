package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SelectDifficultyLevel {

    @FXML
    void onEasyLevelClick(ActionEvent event) throws IOException {
        Parent gameBoardParent = FXMLLoader.load(getClass().getResource("/codebreaker/gui/fxml/GameBoard.fxml"));
        Scene gameBoardScene = new Scene(gameBoardParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(gameBoardScene);
        window.show();
    }

    @FXML
    void onHardLevelClick(ActionEvent event) {

    }

    @FXML
    void onMediumLevelClick(ActionEvent event) {

    }

}
