package mastermind.controllers;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mastermind.MastermindUtils;
import mastermind.core.GameState;

public class EnterUsername {
    @FXML
    private TextField usernameField;
    @FXML
    private Button submit;
    
    private String username;

    @FXML
    public void initialize() {
        submit.setDisable(true);
        usernameField.textProperty().addListener((_, _, newValue) -> {
            if (newValue.isEmpty() || newValue.length() < 6 || newValue.length() > 16) {
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
        GameState.getInstance().setUsername(username);

        //check if user has an unfinished game
        if (MastermindUtils.hasUnfinishedGame(username)) {
            showPopup(event);
        } else {
            MastermindUtils.loadScene(event, "/mastermind/gui/fxml/SelectGameMode.fxml");
        }
    }

    private void showPopup(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastermind/gui/fxml/PopupWindow.fxml"));
        Parent popupContent = loader.load();
        Stage popupStage = new Stage();

        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(((Button) event.getSource()).getScene().getWindow());
        popupStage.setScene(new Scene(popupContent));
        popupStage.setTitle("Mastermind");
        popupStage.getIcons().add(new Image(getClass().getResourceAsStream("/mastermind/gui/img/icons/logo.png")));
        popupStage.setResizable(false);

        // prevent user from closing the popup
        // popupStage.setOnCloseRequest(closeEvent -> {
        //     closeEvent.consume(); 
        // });

        popupStage.showAndWait();
    }

}
