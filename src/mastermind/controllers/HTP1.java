package mastermind.controllers;

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
        Parent HTP2Parent = FXMLLoader.load(getClass().getResource("/mastermind/gui/fxml/HTP2.fxml"));
        Scene HTP2Scene = new Scene(HTP2Parent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(HTP2Scene);
        window.show();
    }

}
