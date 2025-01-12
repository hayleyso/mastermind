package mastermind.core.solvers;

import javafx.util.Pair;
import mastermind.core.Code;

public abstract class MastermindAlgorithm {
    public enum Status {
        WIN, LOSE, CONTINUE
    }

    public abstract Code guess();

    public abstract Code guess(Pair<Integer, Integer> response);


}

