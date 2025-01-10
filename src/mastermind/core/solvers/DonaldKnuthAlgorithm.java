package mastermind.core.solvers;

import java.util.*;
import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.core.*;

public class DonaldKnuthAlgorithm extends MastermindAlgorithm {
    private ArrayList<Code> possibleCodes;
    private Code currentGuess;

    public DonaldKnuthAlgorithm() {
       initializePossibleCodes();
    }

    // 1296 possible codes
    public void initializePossibleCodes() {
        final int possibilities = (int) Math.pow(Mastermind.NUM_COLORS, Mastermind.CODE_LENGTH);
        possibleCodes = new ArrayList<>(possibilities);

        for (int i = 0; i < possibilities; i++) {
            ArrayList<Integer> codeInDigits = new ArrayList<>(Mastermind.CODE_LENGTH);
            int temp = i;
            for (int j = 0; j < Mastermind.CODE_LENGTH; j++) {
                codeInDigits.add(temp % Mastermind.NUM_COLORS);
                temp /= Mastermind.NUM_COLORS;
            }
            possibleCodes.add(new Code(codeInDigits));
        }
    }

    /**
     * {1, 1, 2, 2}
     * @return
     */
    public Code guess(Response res) {
        if (possibleCodes.size() == 1) {
            currentGuess = possibleCodes.get(0);
        } else if (possibleCodes.size() == 1296) {
            currentGuess = new Code(Arrays.asList(0, 0, 1, 1)); 
        } else {
            currentGuess = possibleCodes.get(0);
        }
        return currentGuess;
    }

    public Pair<Integer, Integer> getMatch(final Code code, final Code guess) {
        final Pair<Integer, Integer> response; 
        final List<String> pegColors = new ArrayList<>(Mastermind.CODE_LENGTH); 

        int correctCount = 0;
        int misplacedCount = 0;

        HashMap<Code.Color, Integer> codeOccurrences = code.getOccurrences();
        boolean[] guessedCorrectly = new boolean[Mastermind.CODE_LENGTH];

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
        response = new Pair<>(correctCount, misplacedCount);
        return response;
    }

    public void removeImpossibleCodes(Response response) {
        Pair<Integer, Integer> code = response.getResponse();
        Pair<Integer, Integer> check;
        int offset = 0;

        for (int i = 0; i < possibleCodes.size(); i++) {
            check = getMatch(currentGuess, possibleCodes.get(i - offset));

            if (!(code.getKey() == check.getKey()) || !(code.getValue() == check.getValue())) {
                possibleCodes.remove(i - offset);
                offset++;
            }
        }
    }

    @Override
    public Code guess() {
        throw new UnsupportedOperationException("Unimplemented method 'guess'");
    }
    
}
