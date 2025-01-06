package mastermind.core.solvers;

import mastermind.Mastermind;

public abstract class MastermindSolver {
    
    public enum Status {
        WIN, LOSE, CONTINUE
    }

    protected int attempts = 0;

    public int getAttempts() {
        return attempts;
    }

    protected boolean isLosing() {
        if (attempts > Mastermind.NUM_GUESSES) {
            return true;
        }
        else {
            attempts++;
            return false;
        }

    }


    
}
