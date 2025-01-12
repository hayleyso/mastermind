package mastermind.core.solvers;

import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.core.Code;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MediumAlgorithm extends MastermindAlgorithm {
    private static final Code.Color[] COLORS = Code.Color.values();
    private List<Code> possibleCodes;
    private Code lastGuess;
    private Random random;

    public MediumAlgorithm() {
        this.random = new Random();
        generateAllPossibleCodes();
    }

    private void generateAllPossibleCodes() {
        possibleCodes = new ArrayList<>();
        int totalCombinations = (int) Math.pow(COLORS.length, Mastermind.CODE_LENGTH);
        for (int i = 0; i < totalCombinations; i++) {
            List<Integer> codeColors = new ArrayList<>();
            int temp = i;
            for (int j = 0; j < Mastermind.CODE_LENGTH; j++) {
                codeColors.add(temp % COLORS.length);
                temp /= COLORS.length;
            }
            possibleCodes.add(new Code(codeColors));
        }
    }

    @Override
    public Code guess() {
        lastGuess = new Code(Arrays.asList(0, 0, 1, 1)); // start with 0011 as per Knuth's algorithm
        return lastGuess;
    }

    @Override
    public Code guess(Pair<Integer, Integer> response) {
        if (response.getKey() == Mastermind.CODE_LENGTH) {
            return lastGuess;
        }

        // remove codes that would not give the same response
        possibleCodes.removeIf(code -> !evaluateGuess(code, lastGuess).equals(response));

        // if all codes have been eliminated, regenerate all possible codes
        if (possibleCodes.isEmpty()) {
            generateAllPossibleCodes();
        }

        // choose a random code from the remaining possibilities
        lastGuess = possibleCodes.get(random.nextInt(possibleCodes.size()));
        return lastGuess;
    }

    private Pair<Integer, Integer> evaluateGuess(Code code, Code guess) {
        int blackPegs = 0;
        int whitePegs = 0;
        boolean[] usedInCode = new boolean[Mastermind.CODE_LENGTH];
        boolean[] usedInGuess = new boolean[Mastermind.CODE_LENGTH];

        // count black pegs
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            if (code.getColor(i) == guess.getColor(i)) {
                blackPegs++;
                usedInCode[i] = true;
                usedInGuess[i] = true;
            }
        }

        // count white pegs
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
