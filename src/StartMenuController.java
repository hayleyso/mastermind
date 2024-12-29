import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartMenuController {

    @FXML
    void onPlayBtnClick(ActionEvent event) throws IOException {
        Parent enterUsernameParent = FXMLLoader.load(getClass().getResource("EnterUsername.fxml"));
        Scene enterUsernameScene = new Scene(enterUsernameParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(enterUsernameScene);
        window.show();
    }
}
