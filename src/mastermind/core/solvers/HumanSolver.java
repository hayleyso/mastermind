package mastermind.core.solvers;

import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.core.Code;
import mastermind.core.Response;

public class HumanSolver extends MastermindSolver {
    private final Code secretCode;
    
    public HumanSolver(final Code secretCode) {
        this.secretCode = secretCode;
    }

    public Pair<Status, Response> guess(final Code guess) {
        if (isLosing()) {
            return new Pair<>(Status.LOSE, null);
        }
    
        final Response response = new Response(secretCode, guess);
        final int correctCount = response.getResponse().getKey();
    
        if (correctCount >= Mastermind.CODE_LENGTH) {
            return new Pair<>(Status.WIN, response);
        } else if (getAttempts() == Mastermind.NUM_GUESSES - 1) {
            return new Pair<>(Status.LOSE, null);
        } else {
            attempts++; 
            return new Pair<>(Status.CONTINUE, response);
        }
    }
    

    
}
