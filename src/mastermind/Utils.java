package mastermind;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    
    public static ArrayList<Integer> digitsFromBase(int number, final int base, final int arrLength) {
        ArrayList<Integer> digits = new ArrayList<>(arrLength);

        while (number > 0) {
            final int leastSignificantDigit = number % base;
            digits.add(leastSignificantDigit);
            number /= base;
        }

        while (digits.size() < arrLength) {
            digits.add(0);
        }

        Collections.reverse(digits);

        return digits;
    }

    public static List<Pair<Integer, Pair<Integer, Integer>>> verifyUserResponses(Code userCode, List<Code> computerGuesses, List<Pair<Integer, Integer>> userResponses) {
        List<Pair<Integer, Pair<Integer, Integer>>> mistakes = new ArrayList<>();
        
        for (int i = 0; i < computerGuesses.size(); i++) {
            Code computerGuess = computerGuesses.get(i);
            Pair<Integer, Integer> correctResponse = calculateResponse(userCode, computerGuess);
            Pair<Integer, Integer> userResponse = userResponses.get(i);
            
            if (!correctResponse.equals(userResponse)) {
                mistakes.add(new Pair<>(i + 1, correctResponse));
            }
        }
        
        return mistakes;
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
