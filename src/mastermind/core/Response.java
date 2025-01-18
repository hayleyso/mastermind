/*
    Author: Hayley So
    Title: Response.java
    Date: 2025-01-15
 */
package mastermind.core;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;
import mastermind.Mastermind;

/**
 * The Response class is responsible for handling the result of a guess compared
 * to a given code in the Mastermind game.
 */
public class Response {
    // Pair to hold correct and misplaced counts
    private final Pair<Integer, Integer> response;

    // List to store the colors of the pegs (BLACK, WHITE, NONE)
    private final List<String> pegColors;

    /**
     * Constructor to generate the response and peg colors based on the comparison
     * of the guess and the code.
     * 
     * @param code  the secret code
     * @param guess the guessed code
     */
    public Response(final Code code, final Code guess) {
        // To count the number of correct guesses
        int correctCount = 0;
        // To count the number of misplaced guesses
        int misplacedCount = 0;

        // Getting occurrences of colors in the code
        HashMap<Code.Color, Integer> codeOccurrences = code.getOccurrences();

        // To track correctly guessed positions
        boolean[] guessedCorrectly = new boolean[Mastermind.CODE_LENGTH];

        // List to store peg colors (BLACK, WHITE, NONE)
        pegColors = new ArrayList<>(Mastermind.CODE_LENGTH);

        // Check for correct guesses
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            Code.Color guessColor = guess.getColor(i);
            Code.Color codeColor = code.getColor(i);

            if (guessColor == codeColor) {
                correctCount++;
                guessedCorrectly[i] = true;
                pegColors.add("BLACK");
                codeOccurrences.put(codeColor, codeOccurrences.get(codeColor) - 1);
            } else {
                pegColors.add(null);
            }
        }

        // Check for misplaced guesses
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            if (!guessedCorrectly[i]) {
                Code.Color guessColor = guess.getColor(i);
                if (codeOccurrences.get(guessColor) > 0) {
                    misplacedCount++;
                    pegColors.set(i, "WHITE");
                    codeOccurrences.put(guessColor, codeOccurrences.get(guessColor) - 1);
                } else if (pegColors.get(i) == null) {
                    pegColors.set(i, "NONE");
                }
            }
        }

        // Store the response as a pair of correct and misplaced counts
        response = new Pair<>(correctCount, misplacedCount);
    }

    /**
     * Constructor to initialize the response based on a string format.
     * 
     * @param responseString the response in string format
     */
    public Response(String responseString) {
        if (responseString == null || responseString.isEmpty()) {
            // Default response: no correct or misplaced guesses
            this.response = new Pair<>(0, 0);
        } else {
            // Split the response into B and W counts
            String[] parts = responseString.split("B|W");
            // Correct guesses (BLACK)
            int blacks = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
            // Misplaced guesses (WHITE)
            int whites = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
            this.response = new Pair<>(blacks, whites);
        }

        // Initialize pegColors list
        this.pegColors = new ArrayList<>();

        // Add BLACK for correct guesses
        for (int i = 0; i < response.getKey(); i++) {
            pegColors.add("BLACK");
        }
        // Add WHITE for misplaced guesses
        for (int i = 0; i < response.getValue(); i++) {
            pegColors.add("WHITE");
        }
        // Fill remaining positions with NONE for incorrect guesses
        while (pegColors.size() < Mastermind.CODE_LENGTH) {
            pegColors.add("NONE");
        }
    }

    /**
     * Constructor to initialize the response directly with a pair of correct and
     * misplaced counts.
     * 
     * @param response the response pair (correct, misplaced)
     */
    public Response(Pair<Integer, Integer> response) {
        this.response = response;

        // Initialize pegColors list
        this.pegColors = new ArrayList<>();

        // Add BLACK pegs for correct guesses
        for (int i = 0; i < response.getKey(); i++) {
            pegColors.add("BLACK");
        }
        // Add WHITE pegs for misplaced guesses
        for (int i = 0; i < response.getValue(); i++) {
            pegColors.add("WHITE");
        }
        // Fill remaining positions with NONE for incorrect guesses
        while (pegColors.size() < Mastermind.CODE_LENGTH) {
            pegColors.add("NONE");
        }
    }

    /**
     * Get the response as a pair of correct and misplaced counts.
     * 
     * @return the response as a pair (correctCount, misplacedCount)
     */
    public Pair<Integer, Integer> getResponse() {
        return this.response;
    }

    /**
     * Get the list of peg colors corresponding to the response (BLACK, WHITE,
     * NONE).
     * 
     * @return the list of peg colors
     */
    public List<String> getPegColors() {
        return pegColors;
    }

    /**
     * Get the string representation of the response.
     * 
     * @return the string representation of the response
     */
    @Override
    public String toString() {
        // Return response in the format BxWy
        return "B" + response.getKey() + "W" + response.getValue();
    }
}
