package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mastermind.core.*;

public class SelectGameMode {

    private String gameMode;

    @FXML
    void onGuessModeClick(ActionEvent event) throws IOException {
        loadGameBoard(event);
    }
    
    @FXML
    void onCreateModeClick(ActionEvent event) throws IOException {
        loadSelectDifficultyLevel(event);
    }

    private void loadGameBoard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastermind/gui/fxml/GameBoard.fxml"));
        Parent gameBoardParent = loader.load();
        Scene gameBoardScene = new Scene(gameBoardParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(gameBoardScene);
        window.show();
    }
    
    private void loadSelectDifficultyLevel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastermind/gui/fxml/SelectDifficultyLevel.fxml"));
        Parent selectDifficultyParent = loader.load();
        Scene selectDifficultyScene = new Scene(selectDifficultyParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(selectDifficultyScene);
        window.show();
    }


}

