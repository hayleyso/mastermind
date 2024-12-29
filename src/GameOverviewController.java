import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameOverviewController {

    @FXML
    void onNextBtnClick(ActionEvent event) throws IOException {
        Parent gameOverviewparent = FXMLLoader.load(getClass().getResource("HTP1.fxml"));
        Scene gameOverviewScene = new Scene(gameOverviewparent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(gameOverviewScene);
        window.show();
    }

}
