package mastermind.core.solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeMap;
import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.Utils;
import mastermind.core.Code;
import mastermind.core.Response;

public class DonaldKnuthAlgorithm extends MastermindAlgorithm {
    private Code previousGuess = null;
    private HashSet<Code> permutations;
    private HashSet<Code> originalPermutations;
    private boolean isRetry = false;
    String level;

    public DonaldKnuthAlgorithm(String level) {
        generateAllPossibleCodes();
        originalPermutations = new HashSet<>(permutations);
    }

    private void generateAllPossibleCodes() {
        final int possibilities = (int) Math.pow(Mastermind.NUM_COLORS, Mastermind.CODE_LENGTH);
        permutations = new HashSet<>(possibilities);

        for (int i = 0; i < possibilities; i++) {
            final ArrayList<Integer> codeInDigits = Utils.digitsFromBase(i, Mastermind.NUM_COLORS,
                Mastermind.CODE_LENGTH);
            final Code code = new Code(codeInDigits);
            permutations.add(code);
        }
    }

    @Override
    public Code guess() {
        final Code nextGuess;
        nextGuess = new Code(Arrays.asList(0, 0, 1, 1));
        previousGuess = nextGuess;
        return nextGuess;
    }

    @Override
    public Code guess(Pair<Integer, Integer> response) {
        int blackPegs = response.getKey();

        if (blackPegs == Mastermind.CODE_LENGTH) {
            return previousGuess;
        }

        if (!isRetry) {
            reducePermutations(response);
        }

        if (permutations.isEmpty()) {
            permutations = new HashSet<>(originalPermutations);
            isRetry = true;
            return previousGuess;
        }

        isRetry = false;
        final Code nextGuess = findNextGuess();
        
        if (nextGuess == null) {
            isRetry = true;
            return previousGuess;
        }
        
        previousGuess = nextGuess;
        return nextGuess;
    }

    private void reducePermutations(Pair<Integer, Integer> response) {
        HashSet<Code> newPermutations = new HashSet<>();
        
        for (Code permutation : this.permutations) {
            Pair<Integer, Integer> testResponse = evaluateGuess(permutation, this.previousGuess);
            if (testResponse.equals(response)) {
                newPermutations.add(permutation);
            }
        }
        
        if (!newPermutations.isEmpty()) {
            this.permutations = newPermutations;
        }
    }

    private Code findNextGuess() {
        TreeMap<Integer, ArrayList<Code>> guessScores = new TreeMap<>();
        
        for (final Code guess : this.permutations) {
            int maxGroupSize = 0;
            for (final Code assumedCode : this.permutations) {
                Pair<Integer, Integer> response = evaluateGuess(assumedCode, guess);
                int groupSize = (int) this.permutations.stream()
                    .filter(code -> evaluateGuess(code, guess).equals(response))
                    .count();
                maxGroupSize = Math.max(maxGroupSize, groupSize);
            }
            
            guessScores.computeIfAbsent(maxGroupSize, k -> new ArrayList<>()).add(guess);
        }
        
        if (guessScores.isEmpty()) {
            return null;
        }
        
        ArrayList<Code> bestGuesses = guessScores.firstEntry().getValue();
        return bestGuesses.get(0);
    }

    private Pair<Integer, Integer> evaluateGuess(Code code, Code guess) {
        Response response = new Response(code, guess);
        return response.getResponse();
    }
}
