/*
    Author: Hayley So
    Title: EasyAlgorithm.java
    Date: 2025-01-15
 */
package mastermind.core.solvers;

import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.core.Code;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * EasyAlgorithm is a solver for the Mastermind game. It generates guesses based on previous responses,
 * starting with a simple guess and gradually shuffling correct colors as more feedback is received.
 */
public class EasyAlgorithm extends MastermindAlgorithm {
    // Array to hold all possible colors for pegs
    private static final Code.Color[] COLORS = Code.Color.values();

    // List to store colors that have been correctly guessed
    private List<Integer> correctColors = new ArrayList<>();

    // Index to track the current color being guessed
    private int currentColorIndex = 0;

    // Flag to indicate if the algorithm is in the shuffling phase
    private boolean shufflingPhase = false;

    /**
     * Makes the first guess, which is the color represented by the first value in COLORS repeated for the length of the code.
     * 
     * @return the first guess as a Code object
     */
    @Override
    public Code guess() {
        // Generate a guess with the first color repeated to match the code length
        lastGuess = new Code(Collections.nCopies(Mastermind.CODE_LENGTH, COLORS[0].ordinal()));
        return lastGuess;
    }

    /**
     * Makes a guess based on the response to the previous guess.
     * The guess will either be an extension of the current guess or a shuffle of correct colors.
     * 
     * @param response the feedback from the last guess (correct, misplaced)
     * @return the new guess as a Code object
     */
    @Override
    public Code guess(Pair<Integer, Integer> response) {
        // If there is no previous guess, make the first guess
        if (lastGuess == null) {
            return guess();
        }

        // Calculate the total number of correct and misplaced pegs
        int totalPegs = response.getKey() + response.getValue();

        // If not in the shuffling phase
        if (!shufflingPhase) {
            // List to store the new guess
            List<Integer> newGuess = new ArrayList<>();

            // Add the correctly guessed colors to the new guess
            for (Code.Color color : lastGuess.getColors().subList(0, totalPegs)) {
                newGuess.add(color.ordinal());
                if (!correctColors.contains(color.ordinal())) {
                    correctColors.add(color.ordinal());
                }
            }

            // If not all code positions are filled, fill the remaining positions with the next color
            if (totalPegs < Mastermind.CODE_LENGTH) {
                currentColorIndex++;
                if (currentColorIndex >= COLORS.length) {
                    currentColorIndex = 0;
                }

                // Fill remaining positions with the current color
                while (newGuess.size() < Mastermind.CODE_LENGTH) {
                    newGuess.add(COLORS[currentColorIndex].ordinal());
                }
                lastGuess = new Code(newGuess);
            } else {
                // Enter shuffling phase once all colors are correctly guessed
                shufflingPhase = true;

                // Ensure all positions are filled with correct colors
                while (correctColors.size() < Mastermind.CODE_LENGTH) {
                    correctColors.add(lastGuess.getColors().get(correctColors.size()).ordinal());
                }

                // Shuffle the correct colors for the next guess
                List<Integer> shuffledGuess = new ArrayList<>(correctColors);
                Collections.shuffle(shuffledGuess);
                lastGuess = new Code(shuffledGuess);
            }
        } else {
            // Shuffle the correct colors while ensuring the new guess isn't the same as the last guess
            List<Integer> shuffledGuess = new ArrayList<>(correctColors);
            do {
                Collections.shuffle(shuffledGuess);
            } while (shuffledGuess.equals(lastGuess.getColors().stream()
                    .map(Code.Color::ordinal)
                    .toList()));
            lastGuess = new Code(shuffledGuess);
        }

        return lastGuess;
    }
}
