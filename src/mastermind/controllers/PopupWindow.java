package mastermind.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mastermind.Utils;
import mastermind.core.State;

public class PopupWindow {
    @FXML
    private Button newGameButton;
    @FXML
    private Button returnButton;

    @FXML
    private Text text;

    @FXML
    void initialize() {
        text.setText("Welcome back, " + State.getInstance().getUsername() + "!");
    }

    @FXML
    void onNewGameBtnClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) newGameButton.getScene().getWindow();
        stage.close();
    
        Utils.loadScene(event, "/mastermind/gui/fxml/SelectGameMode.fxml");
      

        Utils.deleteGameState(State.getInstance().getUsername());
    }

    @FXML
    void onReturnBtnClick(ActionEvent event) {
        State.getInstance().setGameFinished(false);
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }

}
