package mastermind.core.solvers;

import mastermind.core.Code;

public class HumanSolver extends MastermindSolver {
    private final Code secretCode;
    
    public HumanSolver(final Code secretCode) {
        this.secretCode = secretCode;
    }
    
}
