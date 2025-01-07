package mastermind;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mastermind.core.Code;

public class MastermindUtils {

    public static <T> T loadScene(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(MastermindUtils.class.getResource(fxmlPath));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.show();

        return loader.getController();
    }

    public static Color getColorFromCode(Code.Color color) {
        switch (color) {
            case GREEN:
                return Color.SEAGREEN;
            case RED:
                return Color.CRIMSON;
            case BLUE:
                return Color.ROYALBLUE;
            case YELLOW:
                return Color.GOLD;
            case ORANGE:
                return Color.DARKORANGE;
            case PURPLE:
                return Color.MEDIUMPURPLE;
            default:
                return Color.TRANSPARENT;
        }
    }

    // display as 0:00
    public static String formatTime(final long timeTaken) {
        long minutes = timeTaken / 60000;
        long seconds = (timeTaken % 60000) / 1000;
        return String.format("%d:%02d", minutes, seconds);
    }
}
