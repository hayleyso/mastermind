package mastermind.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mastermind.MastermindUtils;
import mastermind.core.GameState;

public class PopupWindow {
    @FXML
    private Button button;

    @FXML
    private Button returnButton;

    @FXML
    private Text text;

    @FXML
    void initialize() {
        text.setText("Welcome back, " + GameState.getInstance().getUsername() + "!");
    }

    @FXML
    void onNewGameBtnClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
        MastermindUtils.loadScene(event, "/mastermind/gui/fxml/SelectGameMode.fxml");
    }

    @FXML
    void onReturnBtnClick(ActionEvent event) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
        
        // TODO implement file input and set up gameboard
        // get file
        // read and set up gameboard


        // close popup scene
    }
    
}
