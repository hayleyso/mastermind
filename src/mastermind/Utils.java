package mastermind;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mastermind.core.Code;

public class Utils {
    private static final String GAME_STATE_FILE = "src/mastermind/data/users/game states.txt";
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
        File file = new File(GUESS_LEADERBOARD_FILE);
        file.setWritable(true);
    }

    public static void udpateCreateLeaderBoard(String username, int attempts, long timeTaken) {
        File file = new File(CREATE_LEADERBOARD_FILE);
        file.setWritable(true);
    }

    public static String formatTime(final long timeTaken) {
        long minutes = timeTaken / 60000;
        long seconds = (timeTaken % 60000) / 1000;
        return String.format("%d:%02d", minutes, seconds);
    }
    
    public static boolean hasUnfinishedGame(String username) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(GAME_STATE_FILE));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void addNameStateAndCode(String name, String mode, Code code) throws IOException {
        String entry = '\n' + name + "|" + mode + "|" +  code.toString() + "|";
        BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_STATE_FILE, true));
        writer.write(entry);
        writer.close();   
    }

    public static void addNameStateAndLevel(String name, String mode, String level) throws IOException {
        String entry = '\n' + name + "|" + mode + "|" + level + "|";
        BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_STATE_FILE, true));
        writer.write(entry);
        writer.close();
    }

    public static void saveGameState(List<String> guesses, List<String> responses) throws IOException {
        String gameState = formatGameState(guesses, responses);
        updateGameStateFile(gameState);
    }

    private static String formatGameState(List<String> guesses, List<String> responses) {
        StringBuilder sb = new StringBuilder();
    
        for (int i = 0; i < guesses.size(); i++) {
            sb.append(guesses.get(i)); 
            sb.append(", ").append(responses.get(i)); 
    
            if (i < guesses.size() - 1) {
                sb.append(" "); 
            }
        }
        return sb.toString();
    }
    
    /**
     * Updates the game state file if the user already has an existing game state.
     */
    private static void updateGameStateFile(String gameState) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean updated = false;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(GAME_STATE_FILE))) {
            String line;
    
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts[0].equals(gameState.split("\\|")[0])) { // check if username matches
                    lines.add(line + gameState.substring(parts[0].length())); // append game state to existing line
                    updated = true; // mark as updated
                } else {
                    lines.add(line); // keep existing entry
                }
            }
        }
    
        if (!updated) { 
            lines.add(gameState); 
        }
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_STATE_FILE))) {
            for (String l : lines) 
                writer.write(l); // write each line back to the file   
                writer.newLine();         
        }
    }

    public static String loadGameState(String username) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(GAME_STATE_FILE));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(username)) {
                return line;
            }
        }
        return null;
    }

    public static void deleteGameState(String username) throws IOException {
        List<String> lines = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(GAME_STATE_FILE));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (!parts[0].equals(username)) {
                lines.add(line);
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_STATE_FILE));
        for (String l : lines) {
            writer.write(l);
        }
        writer.close();
        reader.close();
    }
}
