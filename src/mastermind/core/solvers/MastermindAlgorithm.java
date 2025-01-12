package mastermind.core.solvers;

import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.core.Code;

public abstract class MastermindAlgorithm {
    private int attempts;

    public int getAttempts() {
        return attempts;
    }

    public boolean hasReachedMaxGuesses() {
         if (attempts == Mastermind.NUM_GUESSES) {
            return true;
        } else {
            attempts++;
            return false;
        }
    }

    protected boolean isInitialGuess() {
        return attempts == 0;
    }
   
    public abstract Code guess(Pair<Integer, Integer> response);

}
