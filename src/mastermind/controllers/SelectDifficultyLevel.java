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
       loadGameBoard(event);
    }

    @FXML
    void onHardLevelClick(ActionEvent event) throws IOException {
        loadGameBoard(event);
    }

    @FXML
    void onMediumLevelClick(ActionEvent event) throws IOException {
        loadGameBoard(event);

    }

    private void loadGameBoard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastermind/gui/fxml/GameBoard.fxml"));
        Parent gameBoardParent = loader.load();
        Scene gameBoardScene = new Scene(gameBoardParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(gameBoardScene);
        window.show();
    }

}
