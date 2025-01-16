package mastermind;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import mastermind.core.State;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Pair;
import mastermind.core.Code;

public class Utils {
    public static final String DIRECTORY_PATH = "src/mastermind/data/users/";
    private static final String GUESS_LEADERBOARD_FILE = "src/mastermind/data/leaderboard.txt";

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

    public static void updateLeaderBoard(String username, String level, String mode, int attempts, long timeInMillis) {
        List<String[]> entries = new ArrayList<>();
        
        // Read existing entries
        try (BufferedReader reader = new BufferedReader(new FileReader(GUESS_LEADERBOARD_FILE))) {
            String line;
            reader.readLine(); // Skip header
            reader.readLine(); // Skip separator
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    entries.add(new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()});
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading leaderboard file: " + e.getMessage());
        }
        
        // Add new entry
        String formattedTime = String.format("%d:%02d", timeInMillis / 60000, (timeInMillis % 60000) / 1000);
        entries.add(new String[]{username, level, String.valueOf(attempts), formattedTime});
        
        // Sort entries
        Collections.sort(entries, (a, b) -> {
            // Sort by level (hard > medium > easy)
            int levelCompare = compareLevels(a[1], b[1]);
            if (levelCompare != 0) return levelCompare;
            
            // Sort by attempts
            int attemptCompare = Integer.compare(Integer.parseInt(a[2]), Integer.parseInt(b[2]));
            if (attemptCompare != 0) return attemptCompare;
            
            // Sort by time
            return compareTime(a[3], b[3]);
        });
        
        // Write sorted entries to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(GUESS_LEADERBOARD_FILE))) {
            writer.println("Username        | Level  | Attempts | Time (0:00)");
            writer.println("-".repeat(50));
            
            for (String[] entry : entries) {
                String displayName = entry[0].length() > 15 ? entry[0].substring(0, 15) : entry[0];
                writer.printf("%-15s | %-6s | %-8s | %s%n", displayName, entry[1], entry[2], entry[3]);
            }
        } catch (IOException e) {
            System.err.println("Error writing to leaderboard file: " + e.getMessage());
        }
    }
    
    private static int compareLevels(String a, String b) {
        String[] levels = {"hard", "medium", "easy"};
        return Integer.compare(Arrays.asList(levels).indexOf(a.toLowerCase()),
                            Arrays.asList(levels).indexOf(b.toLowerCase()));
    }
    
    private static int compareTime(String a, String b) {
        String[] timeParts1 = a.split(":");
        String[] timeParts2 = b.split(":");
        int minutes1 = Integer.parseInt(timeParts1[0]);
        int seconds1 = Integer.parseInt(timeParts1[1]);
        int minutes2 = Integer.parseInt(timeParts2[0]);
        int seconds2 = Integer.parseInt(timeParts2[1]);
        
        if (minutes1 != minutes2) {
            return Integer.compare(minutes1, minutes2);
        }
        return Integer.compare(seconds1, seconds2);
    }
    
    public static boolean hasUnfinishedGame(String username) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH + username + ".txt");
        return path.toFile().exists();

    }

    public static void saveToFile(String username, String mode, String level, Code code) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        file.setWritable(true);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file, true);
        writer.write(mode + "\n" + level + "\n" + code.toString() + "\n");
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

    public static void saveGuessGameState(String username, int row, List<String> guesses, List<String> responses) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(guesses.get(row) + "|" + responses.get(row) + "\n");
        }        
    }
    
    public static void saveCreateGameState(String username, int row, List<String> guesses, List<String> responses) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) { 
            writer.write(State.getInstance().getGameMode() + "\n");
            writer.write(State.getInstance().getCreateDifficultyLevel() + "\n");
            
            for (int i = 0; i <= row && i < guesses.size(); i++) {
                writer.write(guesses.get(i) + "|");
                if (i < responses.size()) {
                    writer.write(responses.get(i));
                }
                writer.newLine();
            }
        }
    }    
    
    public static void saveInitialGuess(String username, String initialGuess) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(initialGuess + "\n");
        }
    }    

    public static void saveNextGuess(String username, String guess) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(guess + "\n");
        }
    }    

    public static void deleteGameState(String username) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        file.delete();
    }

    public static String[] loadGameState(String username) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String mode = reader.readLine();
            String secondLine = reader.readLine();
            String thirdLine = reader.readLine();
            return new String[]{mode, secondLine, thirdLine};
        }
    }
    
    public static List<Pair<String, String>> loadCreateGuessesAndResponses(String username) {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        List<Pair<String, String>> guessesAndResponses = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            reader.readLine();
    
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    guessesAndResponses.add(new Pair<>(parts[0], parts[1]));
                } else {
                    guessesAndResponses.add(new Pair<>(line, ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return guessesAndResponses;
    }    

    public static List<Pair<String, String>> loadGuessGuessesAndResponses(String username) {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        List<Pair<String, String>> guessesAndResponses = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // Skip mode
            reader.readLine(); // Skip secret code
    
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    guessesAndResponses.add(new Pair<>(parts[0].trim(), parts[1].trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return guessesAndResponses;
    }

    public static String formatTime(final long timeTaken) {
        long minutes = timeTaken / 60000;
        long seconds = (timeTaken % 60000) / 1000;
        return String.format("%d:%02d", minutes, seconds);
    }

    public static Pair<Integer, Integer> countResponsePegs(GridPane responseGrid, int currentResponseRow) {
        int numBlack = 0;
        int numWhite = 0;
        int startRow = currentResponseRow;

        for (int col = 0; col < 2; col++) {
            for (int row = startRow; row <= startRow + 1; row++) {
                Node node = getNodeFromGridPane(responseGrid, col, row);
                if (node instanceof Circle) {
                    Circle peg = (Circle) node;
                    if (peg.getFill().equals(Color.BLACK)) {
                        numBlack++;
                    } else if (peg.getFill().equals(Color.WHITE)) {
                        numWhite++;
                    }
                }
            }
        }

        return new Pair<>(numBlack, numWhite);
    }

    private static Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public static ArrayList<Integer> digitsFromBase(int number, int base, int arrLength) {
        ArrayList<Integer> digits = new ArrayList<>(arrLength);

        while (number > 0) {
            int leastSignificantDigit = number % base;
            digits.add(leastSignificantDigit);
            number /= base;
        }

        while (digits.size() < arrLength) {
            digits.add(0);
        }

        Collections.reverse(digits);

        return digits;
    }

    public static List<Integer> verifyUserResponses(Code userCode, List<Code> computerGuesses,
            List<Pair<Integer, Integer>> userResponses) {
        List<Integer> mistakeRows = new ArrayList<>();

        for (int i = 0; i < computerGuesses.size(); i++) {
            Code computerGuess = computerGuesses.get(i);
            Pair<Integer, Integer> correctResponse = calculateResponse(userCode, computerGuess);
            Pair<Integer, Integer> userResponse = userResponses.get(i);

            if (!correctResponse.equals(userResponse)) {
                mistakeRows.add(i + 1);
            }
        }

        return mistakeRows;
    }

    public static Pair<Integer, Integer> calculateResponse(Code code, Code guess) {
        int blackPegs = 0;
        int whitePegs = 0;
        boolean[] usedInCode = new boolean[Mastermind.CODE_LENGTH];
        boolean[] usedInGuess = new boolean[Mastermind.CODE_LENGTH];

        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            if (code.getColor(i) == guess.getColor(i)) {
                blackPegs++;
                usedInCode[i] = true;
                usedInGuess[i] = true;
            }
        }

        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            if (!usedInGuess[i]) {
                for (int j = 0; j < Mastermind.CODE_LENGTH; j++) {
                    if (!usedInCode[j] && code.getColor(j) == guess.getColor(i)) {
                        whitePegs++;
                        usedInCode[j] = true;
                        break;
                    }
                }
            }
        }

        return new Pair<>(blackPegs, whitePegs);
    }

}
