package mastermind;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mastermind.controllers.GameBoard;
import mastermind.core.Code;

public class Utils {
    public static final String DIRECTORY_PATH = "src/mastermind/data/users/";
    private static final String CREATE_LEADERBOARD_FILE = "src/mastermind/data/create leaderboard.txt";
    private static final String GUESS_LEADERBOARD_FILE = "src/mastermind/data/guess leaderboard.txt";

    public static <T> T loadScene(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxmlPath));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
     
        window.setScene(scene);
        window.show();

        return loader.getController();
    }

    public static Color getGUIColor(Code.Color color) {
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

    public static void updateGuessLeaderBoard(String username, int attempts, long timeTaken) {
        File file = new File(GUESS_LEADERBOARD_FILE);
        file.setWritable(true);
    }

    public static void udpateCreateLeaderBoard(String username, int attempts, long timeTaken) {
        File file = new File(CREATE_LEADERBOARD_FILE + username + ".txt");
        file.setWritable(true);
    }

    public static boolean hasUnfinishedGame(String username) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH + username + ".txt");
        return path.toFile().exists();

    }

    public static void saveToFile(String username, String mode, Code code) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        file.setWritable(true);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file, true);
        writer.write(mode + "\n" + code.toString() + "\n");
        writer.close();
    }

    public static void saveToFile(String username, String mode, String level) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        file.setWritable(true);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file, true);
        writer.write(mode + "\n" + level + "\n");
        writer.close();
    }

    public static void saveGameState(String username, int row, List<String> guesses, List<String> responses)
            throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(guesses.get(row) + "|" + responses.get(row) + "\n");
        }
    }

    public static void deleteGameState(String username) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        file.delete();
    }

    public static String formatTime(final long timeTaken) {
        long minutes = timeTaken / 60000;
        long seconds = (timeTaken % 60000) / 1000;
        return String.format("%d:%02d", minutes, seconds);
    }

}
