/*
    Author: Hayley So
    Title: DonaldKnuthAlgorithm.java
    Date: 2025-01-15
 */
package mastermind.core.solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.Utils;
import mastermind.core.Code;
import mastermind.core.Response;

/**
 * This algorithm follows Donald Knuth's Five-Guess Algorithm. It reduces the
 * possible code permutations based on previous responses and selects the next
 * guess by minimizing the worst-case number of remaining possibilities.
 */
public class DonaldKnuthAlgorithm extends MastermindAlgorithm {
    // Stores the previous guess made by the algorithm
    private Code previousGuess = null;

    // Holds the set of all possible code permutations
    private HashSet<Code> permutations;

    // Holds the original set of permutations for retries
    private HashSet<Code> originalPermutations;

    // Indicates if the algorithm is retrying the search after exhausting
    // permutations
    private boolean isRetry = false;

    // List of previous guesses made by the algorithm
    private List<Code> previousGuesses;

    // List of responses received for the previous guesses
    private List<Pair<Integer, Integer>> previousResponses;

    /**
     * Constructor initializes the possible permutations, previous guesses, and
     * responses.
     */
    public DonaldKnuthAlgorithm() {
        // Generate all possible codes based on the Mastermind game settings
        generateAllPossibleCodes();

        // Store the original permutations for retry purposes
        originalPermutations = new HashSet<>(permutations);

        // Initialize lists for tracking previous guesses and responses
        previousGuesses = new ArrayList<>();
        previousResponses = new ArrayList<>();
    }

    /**
     * Generates all possible code permutations based on the number of colors and
     * code length.
     */
    private void generateAllPossibleCodes() {
        // Calculate the total number of possible code combinations
        final int possibilities = (int) Math.pow(Mastermind.NUM_COLORS, Mastermind.CODE_LENGTH);

        // Initialize the set to hold all possible permutations
        permutations = new HashSet<>(possibilities);

        // Loop to generate each possible code and add it to the set
        for (int i = 0; i < possibilities; i++) {
            // Convert the current integer to a list of digits representing a code
            final ArrayList<Integer> codeInDigits = Utils.digitsFromBase(i, Mastermind.NUM_COLORS,
                    Mastermind.CODE_LENGTH);
            final Code code = new Code(codeInDigits);

            // Add the generated code to the set of permutations
            permutations.add(code);
        }
    }

    /**
     * Makes the first guess, which is the fixed code [0, 0, 1, 1].
     * 
     * @return the first guess as a Code object
     */
    @Override
    public Code guess() {
        // First guess is fixed as [0, 0, 1, 1]
        final Code nextGuess = new Code(Arrays.asList(0, 0, 1, 1));

        // Set the previous guess to the new guess
        previousGuess = nextGuess;

        // Add this guess to the list of previous guesses
        previousGuesses.add(nextGuess);

        return nextGuess;
    }

    /**
     * Makes a guess based on the feedback received for the previous guess.
     * 
     * @param response the feedback received from the last guess (correct and
     *                 misplaced pegs)
     * @return the next guess as a Code object
     */
    @Override
    public Code guess(Pair<Integer, Integer> response) {
        // Extract the number of correct (black) pegs from the response
        int blackPegs = response.getKey();

        // If the guess is correct (all black pegs), return the current guess
        if (blackPegs == Mastermind.CODE_LENGTH) {
            return previousGuess;
        }

        // Add the response to the list of previous responses
        previousResponses.add(response);

        // If not retrying, reduce the possible permutations based on the response
        if (!isRetry) {
            reducePermutations(response);
        }

        // If no valid permutations remain, reset and retry
        if (permutations.isEmpty()) {
            permutations = new HashSet<>(originalPermutations);
            isRetry = true;
            return previousGuess;
        }

        // Reset the retry flag
        isRetry = false;

        // Find and return the next guess based on the remaining possibilities
        final Code nextGuess = findNextGuess();

        // If no valid guess can be found, retry with the previous guess
        if (nextGuess == null) {
            isRetry = true;
            return previousGuess;
        }

        // Update the previous guess with the new guess and add it to the list
        previousGuess = nextGuess;
        previousGuesses.add(nextGuess);

        return nextGuess;
    }

    /**
     * Reduces the set of possible permutations based on the feedback response.
     * 
     * @param response the feedback response to use for filtering possible
     *                 permutations
     */
    private void reducePermutations(Pair<Integer, Integer> response) {
        // Create a new set to hold valid permutations based on the response
        HashSet<Code> newPermutations = new HashSet<>();

        // Loop through each permutation and check if it matches the response
        for (Code permutation : this.permutations) {
            // Evaluate the guess for the current permutation
            Pair<Integer, Integer> testResponse = evaluateGuess(permutation, this.previousGuess);

            // If the response matches the feedback, keep this permutation
            if (testResponse.equals(response)) {
                newPermutations.add(permutation);
            }
        }

        // Update the permutations set with the valid permutations
        if (!newPermutations.isEmpty()) {
            this.permutations = newPermutations;
        }
    }

    /**
     * Finds the next guess by minimizing the worst-case number of remaining
     * possibilities.
     * 
     * @return the next guess as a Code object
     */
    private Code findNextGuess() {
        // Create a map to store guess scores based on group sizes
        TreeMap<Integer, ArrayList<Code>> guessScores = new TreeMap<>();

        // Evaluate each permutation as a possible guess
        for (final Code guess : this.permutations) {
            int maxGroupSize = 0;

            // Group all permutations by their response to the current guess
            for (final Code assumedCode : this.permutations) {
                // Get the response for the assumed code when compared to the guess
                Pair<Integer, Integer> response = evaluateGuess(assumedCode, guess);

                // Count the group size for this response
                int groupSize = (int) this.permutations.stream()
                        .filter(code -> evaluateGuess(code, guess).equals(response))
                        .count();

                // Keep track of the largest group size
                maxGroupSize = Math.max(maxGroupSize, groupSize);
            }

            // Store the guess in the map, categorized by the largest group size
            guessScores.computeIfAbsent(maxGroupSize, k -> new ArrayList<>()).add(guess);
        }

        // If no guesses are available, return null
        if (guessScores.isEmpty()) {
            return null;
        }

        // Get the best guesses based on the smallest largest group size
        ArrayList<Code> bestGuesses = guessScores.firstEntry().getValue();

        // Return the best guess from the best group
        return bestGuesses.get(0);
    }

    /**
     * Evaluates the response to a guess based on the actual code.
     * 
     * @param code  the actual code to compare against the guess
     * @param guess the guess to evaluate
     * @return the feedback response as a Pair of integers (black pegs, white pegs)
     */
    @Override
    protected Pair<Integer, Integer> evaluateGuess(Code code, Code guess) {
        Response response = new Response(code, guess);
        return response.getResponse();
    }

    /**
     * Restores the state of the algorithm with the last guess and previous
     * responses.
     * 
     * @param lastGuess the previous guess made by the algorithm
     * @param responses a list of feedback responses for previous guesses
     */
    public void restoreState(Code lastGuess, List<Pair<Integer, Integer>> responses) {
        // Regenerate all possible codes
        generateAllPossibleCodes();

        // Reset the lists for previous guesses and responses
        previousGuesses = new ArrayList<>();
        previousResponses = new ArrayList<>();

        // Apply all previous responses to reduce the possible permutations
        for (int i = 0; i < responses.size(); i++) {
            Pair<Integer, Integer> response = responses.get(i);
            previousResponses.add(response);
            reducePermutations(response);
        }

        // Restore the last guess and add it to the list of previous guesses
        this.previousGuess = lastGuess;
        previousGuesses.add(lastGuess);

        // Reset the retry flag
        this.isRetry = false;
    }
}
