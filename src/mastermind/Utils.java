/*
    Author: Hayley So
    Title: Utils.java
    Date: 2025-01-15
 */
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

/**
 * Utility class for game-related functionalities such as scene loading,
 * color mapping, leaderboard updates, and game state management.
 */
public class Utils {

    // Directory path for storing user game states
    public static final String DIRECTORY_PATH = "src/mastermind/data/users/";
    // Directory path for storing the guess leaderboard entries
    private static final String GUESS_LEADERBOARD_FILE = "src/mastermind/data/leaderboard.txt";

    /**
     * Loads a new scene (FXML file) into the current window.
     * 
     * @param event The event that triggered the scene load.
     * @param fxmlPath The path to the FXML file to load.
     * @param <T> The type of the controller.
     * @return The controller of the loaded scene.
     * @throws IOException If there is an issue loading the FXML file.
     */
    public static <T> T loadScene(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxmlPath));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.show();

        return loader.getController();
    }

    /**
     * Maps a game's color to a corresponding JavaFX color.
     * 
     * @param color The game's color to map.
     * @return The corresponding JavaFX color.
     */
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

    /**
     * Updates the leaderboard file with the latest game result.
     * 
     * @param username The player's username.
     * @param level The difficulty level of the game.
     * @param attempts The number of attempts made by the player.
     * @param timeInMillis The time taken for the game in milliseconds.
     */
    public static void updateLeaderBoard(String username, String level, int attempts, long timeInMillis) {
        List<String[]> entries = new ArrayList<>();
        
        // Read existing entries from the leaderboard file
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
        
        // Add new entry with formatted time
        String formattedTime = String.format("%d:%02d", timeInMillis / 60000, (timeInMillis % 60000) / 1000);
        entries.add(new String[]{username, level, String.valueOf(attempts), formattedTime});
        
        // Sort leaderboard entries
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
        
        // Write sorted leaderboard to file
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
    
    /**
     * Compares two difficulty levels.
     * 
     * @param a The first difficulty level.
     * @param b The second difficulty level.
     * @return A negative integer, zero, or a positive integer if the first difficulty level is less than,
     *         equal to, or greater than the second.
     */
    private static int compareLevels(String a, String b) {
        String[] levels = {"hard", "medium", "easy"};
        return Integer.compare(Arrays.asList(levels).indexOf(a.toLowerCase()),
                            Arrays.asList(levels).indexOf(b.toLowerCase()));
    }
    
    /**
     * Compares two times formatted as "MM:SS".
     * 
     * @param a The first time.
     * @param b The second time.
     * @return A negative integer, zero, or a positive integer if the first time is earlier than,
     *         equal to, or later than the second.
     */
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
    
    /**
     * Checks if the user has an unfinished game.
     * 
     * @param username The player's username.
     * @return True if the user has an unfinished game, otherwise false.
     * @throws IOException If there is an issue reading the game state file.
     */
    public static boolean hasUnfinishedGame(String username) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH + username + ".txt");
        return path.toFile().exists();
    }

    /**
     * Saves the initial game state (mode, level, and code) to a file for a given user.
     * 
     * @param username The player's username.
     * @param mode The mode of the game.
     * @param level The difficulty level of the game.
     * @param code The initial code for the game.
     * @throws IOException If there is an issue writing to the file.
     */
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

    /**
     * Saves the mode and level of a game to a file for a given user.
     * 
     * @param username The player's username.
     * @param mode The mode of the game.
     * @param level The difficulty level of the game.
     * @throws IOException If there is an issue writing to the file.
     */
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

    /**
     * Saves the state of the game (guesses and responses) for a given user.
     * 
     * @param username The player's username.
     * @param row The current row of the game.
     * @param guesses The list of guesses made by the player.
     * @param responses The list of responses corresponding to each guess.
     * @throws IOException If there is an issue writing to the file.
     */
    public static void saveGuessGameState(String username, int row, List<String> guesses, List<String> responses) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(guesses.get(row) + "|" + responses.get(row) + "\n");
        }        
    }
    
    /**
     * Saves the state of a create game (guesses and responses) for a given user.
     * 
     * @param username The player's username.
     * @param row The current row of the game.
     * @param guesses The list of guesses made by the player.
     * @param responses The list of responses corresponding to each guess.
     * @throws IOException If there is an issue writing to the file.
     */
    public static void saveCreateGameState(String username, int row, List<String> guesses, List<String> responses) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) { 
            writer.write(State.getInstance().getGameMode() + "\n");
            writer.write(State.getInstance().getCreateDifficultyLevel() + "\n");
            
            // Write all guesses and responses up to the current row
            for (int i = 0; i <= row && i < guesses.size(); i++) {
                writer.write(guesses.get(i) + "|");
                if (i < responses.size()) {
                    writer.write(responses.get(i));
                }
                writer.newLine();
            }
        }
    }    

    /**
     * Saves the initial guess to a user's game state.
     * 
     * @param username The player's username.
     * @param initialGuess The initial guess for the game.
     * @throws IOException If there is an issue writing to the file.
     */
    public static void saveInitialGuess(String username, String initialGuess) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(initialGuess + "\n");
        }
    }    

    /**
     * Saves the next guess to a user's game state.
     * 
     * @param username The player's username.
     * @param guess The next guess for the game.
     * @throws IOException If there is an issue writing to the file.
     */
    public static void saveNextGuess(String username, String guess) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(guess + "\n");
        }
    }    

    /**
     * Deletes a user's game state file.
     * 
     * @param username The player's username.
     * @throws IOException If there is an issue deleting the file.
     */
    public static void deleteGameState(String username) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        file.delete();
    }

    /**
     * Loads a user's game state from the file.
     * 
     * @param username The player's username.
     * @return An array containing the mode, level, and code from the game state.
     * @throws IOException If there is an issue reading the file.
     */
    public static String[] loadGameState(String username) throws IOException {
        File file = new File(DIRECTORY_PATH + username + ".txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String mode = reader.readLine();
            String secondLine = reader.readLine();
            String thirdLine = reader.readLine();
            return new String[]{mode, secondLine, thirdLine};
        }
    }
    
    /**
     * Loads a list of guesses and responses for a create game.
     * 
     * @param username The player's username.
     * @return A list of pairs where each pair contains a guess and its corresponding response.
     */
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

    /**
     * Loads the guesses and responses from a file for a given username.
     * The file is expected to contain the mode, secret code, and the list of guesses with corresponding responses.
     * 
     * @param username The player's username.
     * @return A list of pairs, where each pair contains a guess and its corresponding response (black and white pegs).
     */
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

    /**
     * Formats a time value in milliseconds into a string of the format "minutes:seconds".
     * 
     * @param timeTaken The time taken, in milliseconds.
     * @return A formatted string representing the time in "minutes:seconds" format.
     */
    public static String formatTime(final long timeTaken) {
        long minutes = timeTaken / 60000;
        long seconds = (timeTaken % 60000) / 1000;
        return String.format("%d:%02d", minutes, seconds);
    }

    /**
     * Counts the number of black and white response pegs in the response grid for a given response row.
     * 
     * @param responseGrid The GridPane containing the response pegs.
     * @param currentResponseRow The row where the current response is located.
     * @return A Pair containing the number of black and white pegs in the response.
     */
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

    /**
     * Retrieves a node from a GridPane at a specific column and row index.
     * 
     * @param gridPane The GridPane from which to retrieve the node.
     * @param col The column index of the node.
     * @param row The row index of the node.
     * @return The node at the specified column and row, or null if not found.
     */
    private static Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * Converts a number to an array of digits in a specified base.
     * The resulting array will have a fixed length, padding with zeros if necessary.
     * 
     * @param number The number to be converted.
     * @param base The base to which the number should be converted.
     * @param arrLength The length of the resulting array of digits.
     * @return A list of digits representing the number in the specified base.
     */
    public static ArrayList<Integer> digitsFromBase(int number, int base, int arrLength) {
        ArrayList<Integer> digits = new ArrayList<>(arrLength);

        while (number > 0) {
            int leastSignificantDigit = number % base;
            digits.add(leastSignificantDigit);
            number /= base;
        }

        while (digits.size() < arrLength) {
             // Pad with zeros if necessary
            digits.add(0);
        }

        // Reverse the list to match the correct order
        Collections.reverse(digits); 

        return digits;
    }

    /**
     * Verifies the responses provided by the user against the expected responses from the computer's guesses.
     * 
     * @param userCode The code that the user is trying to guess.
     * @param computerGuesses A list of guesses made by the computer.
     * @param userResponses A list of the user's responses to the computer's guesses.
     * @return A list of row numbers where the user's responses do not match the expected responses.
     */
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

    /**
     * Calculates the response for a given guess based on the correct code.
     * The response consists of black pegs (correct color in the correct position)
     * and white pegs (correct color in the wrong position).
     * 
     * @param code The correct code.
     * @param guess The guessed code.
     * @return A Pair containing the number of black and white pegs.
     */
    public static Pair<Integer, Integer> calculateResponse(Code code, Code guess) {
        int blackPegs = 0;
        int whitePegs = 0;
        boolean[] usedInCode = new boolean[Mastermind.CODE_LENGTH];
        boolean[] usedInGuess = new boolean[Mastermind.CODE_LENGTH];

        // Count black pegs
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            if (code.getColor(i) == guess.getColor(i)) {
                blackPegs++;
                usedInCode[i] = true;
                usedInGuess[i] = true;
            }
        }

        // Count white pegs
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
