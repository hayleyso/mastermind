package mastermind.core.solvers;

import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.core.Code;

public abstract class MastermindAlgorithm {
    private int attempts;

    public abstract Code guess();
    public abstract Code guess(Pair<Integer, Integer> response);

    public boolean isInitialGuess() {
        return attempts == 0;
    }

    public boolean hasReachedMaxGuesses() {
        return attempts >= Mastermind.NUM_GUESSES;
    }

}

