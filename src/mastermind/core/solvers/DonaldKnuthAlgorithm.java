package mastermind.core.solvers;

import java.util.*;
import mastermind.Mastermind;
import mastermind.core.*;

public class DonaldKnuthAlgorithm extends MastermindAlgorithm {
    private HashSet<Code> possibleCodes;
    private Code currentGuess;

    public DonaldKnuthAlgorithm() {
       initializePossibleCodes();
    }

    public void initializePossibleCodes() {
        final int possibilities = (int) Math.pow(Mastermind.NUM_COLORS, Mastermind.CODE_LENGTH);
        possibleCodes = new HashSet<>(possibilities);

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
    public Code guess() {
        if (possibleCodes.size() == 1) {
            currentGuess = possibleCodes.iterator().next();
        } else {
            currentGuess = new Code(Arrays.asList(0, 0, 1, 1)); 
        }
        return currentGuess;
    }

    

    
}
