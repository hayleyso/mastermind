package mastermind.core.solvers;

import javafx.scene.paint.Color;
import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.core.Code;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EasyAlgorithm extends MastermindAlgorithm {
    private static final Code.Color[] COLORS = Code.Color.values();
    private int currentColorIndex = 0;
    private Code lastGuess;

    @Override
    public Code guess(Pair<Integer, Integer> response) {
        if (isInitialGuess()) {
            lastGuess = new Code(Arrays.asList(
                COLORS[0].ordinal(),
                COLORS[0].ordinal(),
                COLORS[0].ordinal(),
                COLORS[0].ordinal()
            ));
            return lastGuess;
        }

        // process feedback and generate next guess
        currentColorIndex = (currentColorIndex + 1) % COLORS.length;
        List<Integer> newGuess = new ArrayList<>();
        
        for (Code.Color color : lastGuess.getColors()) {
            newGuess.add(color.ordinal());
        }

        // change one position to next color
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            if (newGuess.get(i) != COLORS[currentColorIndex].ordinal()) {
                newGuess.set(i, COLORS[currentColorIndex].ordinal());
                break;
            }
        }
        
        lastGuess = new Code(newGuess);
        return lastGuess;
    }
}
