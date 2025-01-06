package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mastermind.utils.SceneLoader;

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
        SceneLoader.loadPage(event, "/mastermind/gui/fxml/GameOverview.fxml");
    }
}
