package mastermind;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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

    public static Color getColor(Code.Color color) {
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
        File file = new File("guess leaderboard.txt");
        file.setWritable(true);
    }

    public static void udpateCreateLeaderBoard(String username, int attempts, long timeTaken) {
        File file = new File("create leaderboard.txt");
        file.setWritable(true);
    }

    public static String formatTime(final long timeTaken) {
        long minutes = timeTaken / 60000;
        long seconds = (timeTaken % 60000) / 1000;
        return String.format("%d:%02d", minutes, seconds);
    }

    public static void addName(String name) throws IOException {
        FileWriter writer = new FileWriter("src/mastermind/data/users/unfinished games.txt", true);
        writer.write(name + "\n");
        writer.close();
    }

    public static void removeName(String name) throws IOException {
        File file = new File("src/mastermind/data/users/unfinished games.txt"); 
        String currentLine;
        List<String> lines = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        while ((currentLine = reader.readLine()) != null) {
            if (!currentLine.trim().equals(name)) {
                lines.add(currentLine);
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (String line : lines) {
            writer.write(line + System.lineSeparator());
        }
        reader.close();
        writer.close();
    }

    public static boolean hasUnfinishedGame(String username) throws FileNotFoundException {
        File file = new File("src/mastermind/data/users/unfinished games.txt");

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void saveGameState(String mode, Code code, List<String> guesses, List<String> responses) {

    }

    public static void saveGameState(String mode, String level, Code code, List<String> guesses, List<String> responses) {
       
    }

    public static void deleteGameState(String username, String mode) {
        String filePath = "mastermind\\data\\users\\"
                + mode + "\\" + username + ".txt";
        File file = new File(filePath);
        file.delete();
    }
}
