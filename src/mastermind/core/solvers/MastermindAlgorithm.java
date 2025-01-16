package mastermind.core.solvers;

import javafx.util.Pair;
import mastermind.core.Code;

public abstract class MastermindAlgorithm {
    
    /**
     * 
     * @return
     */
    public abstract Code guess();

    /**
     * 
     * @param response
     * @return
     */
    public abstract Code guess(Pair<Integer, Integer> response);
}

