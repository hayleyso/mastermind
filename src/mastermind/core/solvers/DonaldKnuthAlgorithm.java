package mastermind.core.solvers;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.core.Code;
import mastermind.core.Response;

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

    @Override
    public Code guess(Code guess, Pair<Integer, Integer> respones) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'guess'");
    }

}    