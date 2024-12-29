import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EnterUsernameController {
    private String username;

    @FXML
    void onOKBtnClick(ActionEvent event) throws IOException {
        Parent gameOverviewparent = FXMLLoader.load(getClass().getResource("GameOverview.fxml"));
        Scene gameOverviewScene = new Scene(gameOverviewparent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(gameOverviewScene);
        window.show();

        // TODO: Get the username from the text field
        // and only allow the user to proceed if the username is not empty
        // change file path/names
        // fix HTP2 next btn issue
    }

}
