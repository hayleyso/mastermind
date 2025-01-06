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

    private String difficultyLevel;
  
    @FXML
    void onEasyLevelClick(ActionEvent event) throws IOException {
        difficultyLevel = "easy";
        loadGameBoard(event);
    }

    @FXML
    void onMediumLevelClick(ActionEvent event) throws IOException {
        difficultyLevel = "medium";
        loadGameBoard(event);

    }

    @FXML
    void onHardLevelClick(ActionEvent event) throws IOException {
        difficultyLevel = "hard";
        loadGameBoard(event);
    }

    private void loadGameBoard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastermind/gui/fxml/GameBoard.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }

}
