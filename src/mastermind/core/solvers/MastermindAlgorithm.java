package mastermind.core.solvers;

import javafx.util.Pair;
import mastermind.core.Code;
import mastermind.core.Response;

public abstract class MastermindAlgorithm {
   
    public abstract Code guess();
    
    //public abstract Pair<Status, Code> guess(final Response response);

    protected String difficultyLevel;

    public void setDifficultyLevel(String level) {
        this.difficultyLevel = level;
    }

    public String getDifficultyLevel() {
        return this.difficultyLevel;
    }
}
