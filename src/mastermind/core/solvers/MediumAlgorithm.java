/*
    Author: Hayley So
    Title: MediumAlgorithm.java
    Date: 2025-01-15
 */
package mastermind.core.solvers;

import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.core.Code;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Medium algorithm is an improved Mastermind solver that narrows down possible
 * code guesses based on previous feedback. It generates all possible code
 * combinations initially, filters out invalid guesses based on the feedback,
 * and then randomly selects the next guess.
 */
public class MediumAlgorithm extends MastermindAlgorithm {
    // List to store all possible code combinations
    private List<Code> possibleCodes;

    // Variable to store the most recent guess
    private Code lastGuess;

    // Random number generator for selecting guesses
    private Random random;

    /**
     * Constructor initializes the random number generator and generates all
     * possible codes.
     */
    public MediumAlgorithm() {
        // Initialize the random number generator
        this.random = new Random();
        // Generate all possible code combinations
        generateAllPossibleCodes();
    }

    /**
     * Generates all possible code combinations based on the number of colors and
     * code length.
     */
    private void generateAllPossibleCodes() {
        // Initialize the list to store possible codes
        possibleCodes = new ArrayList<>();

        // Calculate the total number of possible combinations
        int totalCombinations = (int) Math.pow(COLORS.length, Mastermind.CODE_LENGTH);

        // Loop through each combination
        for (int i = 0; i < totalCombinations; i++) {
            // Temporary list to hold colors for the current code
            List<Integer> codeColors = new ArrayList<>();
            int temp = i;

            // Loop to extract each color for the code
            for (int j = 0; j < Mastermind.CODE_LENGTH; j++) {
                codeColors.add(temp % COLORS.length);
                temp /= COLORS.length;
            }

            // Add the generated code to the list of possible codes
            possibleCodes.add(new Code(codeColors));
        }
    }

    /**
     * Makes the first guess, which is a simple code with all pegs of color 0.
     * 
     * @return the first guess as a Code object
     */
    @Override
    public Code guess() {
        // First guess is a code with all pegs set to the first color (0)
        lastGuess = new Code(Arrays.asList(0, 0, 0, 0));
        return lastGuess;
    }

    /**
     * Makes a guess based on the response to the previous guess.
     * Filters out impossible codes based on the response and selects a random valid
     * guess.
     * 
     * @param response the feedback from the last guess (correct, misplaced)
     * @return the next guess as a Code object
     */
    @Override
    public Code guess(Pair<Integer, Integer> response) {
        // If the last guess is correct, return it
        if (response.getKey() == Mastermind.CODE_LENGTH) {
            return lastGuess;
        }

        // Remove all impossible codes from the list based on the response
        possibleCodes.removeIf(code -> !evaluateGuess(code, lastGuess).equals(response));

        // If no valid codes are left, regenerate all possible codes
        if (possibleCodes.isEmpty()) {
            generateAllPossibleCodes();
        }

        // Select a random code from the remaining possible codes
        lastGuess = possibleCodes.get(random.nextInt(possibleCodes.size()));
        return lastGuess;
    }

    /**
     * Restores the state of the algorithm, including the last guess and previously
     * received responses.
     * 
     * @param lastGuess         the previous guess made by the algorithm
     * @param previousResponses a list of feedback received for previous guesses
     */
    public void restoreState(Code lastGuess, List<Pair<Integer, Integer>> previousResponses) {
        // Regenerate all possible codes
        generateAllPossibleCodes();

        // Filter out impossible codes based on previous responses
        for (Pair<Integer, Integer> response : previousResponses) {
            possibleCodes.removeIf(code -> !evaluateGuess(code, lastGuess).equals(response));
        }

        // Restore the last guess
        this.lastGuess = lastGuess;
    }
}
