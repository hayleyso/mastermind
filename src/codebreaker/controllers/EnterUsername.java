package codebreaker.controllers;

import java.io.*;
import java.lang.classfile.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EnterUsername {
    private String username;
    
    @FXML
    private TextField usernameField;

    @FXML
    private Button submit;

    @FXML
    public void initialize() {
        submit.setDisable(true);
        usernameField.textProperty().addListener((_, _, newValue) -> {
            if (newValue.isEmpty() || newValue.length() < 4) {
                submit.setDisable(true);
            } else {
                submit.setDisable(false);
            }
        });
    }

    @FXML
    public void submit(ActionEvent event) throws IOException {
        submit.setDisable(true);
        username = usernameField.getText();
        System.out.println(username);
        Parent gameOverviewParent = FXMLLoader.load(getClass().getResource("/codebreaker/gui/fxml/GameOverview.fxml"));
        Scene gameOverviewScene = new Scene(gameOverviewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            
        window.setScene(gameOverviewScene);
        window.show();
    }
}
