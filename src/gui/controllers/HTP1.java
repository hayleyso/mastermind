package gui.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HTP1 {

    @FXML
    void onNextBtnClick(ActionEvent event) throws IOException {
        Parent gameOverviewParent = FXMLLoader.load(getClass().getResource("/gui/scenes/HTP2.fxml"));
        Scene gameOverviewScene = new Scene(gameOverviewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(gameOverviewScene);
        window.show();
    }

}
