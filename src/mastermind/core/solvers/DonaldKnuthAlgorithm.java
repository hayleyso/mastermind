package mastermind.core.solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeMap;
import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.Utils;
import mastermind.core.Code;

public class DonaldKnuthAlgorithm extends MastermindAlgorithm {
    private Code previousGuess = null;
    private HashSet<Code> permutations;
    String level;

    public DonaldKnuthAlgorithm(String level) {
        generatePermutations();
    }

    private void generatePermutations() {
        final int possibilities = (int) Math.pow(Mastermind.NUM_COLORS, Mastermind.CODE_LENGTH);
        permutations = new HashSet<>(possibilities);

        for (int i = 0; i < possibilities; ++i) {
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
        } else {
            reducePermutations(response);
            final Code nextGuess = findNextGuess();
            previousGuess = nextGuess;
            return nextGuess;
        }
    }

    private void reducePermutations(Pair<Integer, Integer> response) {
        this.permutations.removeIf(permutation -> {
            Pair<Integer, Integer> testResponse = evaluateGuess(permutation, this.previousGuess);
            return !testResponse.equals(response);
        });
    }

    private Code findNextGuess() {
        TreeMap<Integer, Code> guessScores = new TreeMap<>();

        for (final Code guess : this.permutations) {
            int maxGroupSize = 0;
            for (final Code assumedCode : this.permutations) {
                Pair<Integer, Integer> response = evaluateGuess(assumedCode, guess);
                int groupSize = (int) this.permutations.stream()
                    .filter(code -> evaluateGuess(code, guess).equals(response))
                    .count();
                maxGroupSize = Math.max(maxGroupSize, groupSize);
            }
            guessScores.put(maxGroupSize, guess);
        }

        return guessScores.firstEntry().getValue();
    }

    private Pair<Integer, Integer> evaluateGuess(Code code, Code guess) {
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
