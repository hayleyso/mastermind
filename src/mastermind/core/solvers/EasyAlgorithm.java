package mastermind.core.solvers;

import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.core.Code;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EasyAlgorithm extends MastermindAlgorithm {
    private static final Code.Color[] COLORS = Code.Color.values();
    private int currentColorIndex = 0;
    private Code lastGuess;
    private boolean shufflingPhase = false;

    @Override
    public Code guess() {
        lastGuess = new Code(Collections.nCopies(Mastermind.CODE_LENGTH, COLORS[0].ordinal()));
        return lastGuess;
    }

    @Override
    public Code guess(Pair<Integer, Integer> response) {
        int totalPegs = response.getKey() + response.getValue();

        if (!shufflingPhase) {
            // Keep the correct number of pegs from the previous guess
            List<Integer> newGuess = new ArrayList<>();
            for (Code.Color color : lastGuess.getColors().subList(0, totalPegs)) {
                newGuess.add(color.ordinal());
            }
            
            // If we haven't found all colors yet, move to the next color for the remaining pegs
            if (totalPegs < Mastermind.CODE_LENGTH) {
                currentColorIndex++;
                if (currentColorIndex >= COLORS.length) {
                    currentColorIndex = 0;
                }
                
                // Fill the remaining positions with the new color
                while (newGuess.size() < Mastermind.CODE_LENGTH) {
                    newGuess.add(COLORS[currentColorIndex].ordinal());
                }
            } else {
                // If we have found all colors, start shuffling
                shufflingPhase = true;
            }
            
            lastGuess = new Code(newGuess);
        } else {
            // If in shuffling phase, rotate the colors
            List<Integer> newGuess = new ArrayList<>();
            for (Code.Color color : lastGuess.getColors()) {
                newGuess.add(color.ordinal());
            }
            Collections.rotate(newGuess, 1);
            lastGuess = new Code(newGuess);
        }

        return lastGuess;
    }
}
