package mastermind.core.solvers;

import javafx.util.Pair;
import mastermind.core.Code;
import mastermind.core.Response;

public abstract class MastermindAlgorithm {
    public enum Status {
        WIN, LOSE, CONTINUE
    }

    public abstract Code guess();
    
    public abstract Pair<Status, Code> guess(final Response response);
}
