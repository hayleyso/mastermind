package mastermind.core.solvers;

import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.core.Code;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EasyAlgorithm extends MastermindAlgorithm {
    private static final Code.Color[] COLORS = Code.Color.values();
    private List<Integer> correctColors = new ArrayList<>();
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
            List<Integer> newGuess = new ArrayList<>();
            for (Code.Color color : lastGuess.getColors().subList(0, totalPegs)) {
                newGuess.add(color.ordinal());
                if (!correctColors.contains(color.ordinal())) {
                    correctColors.add(color.ordinal());
                }
            }

            if (totalPegs < Mastermind.CODE_LENGTH) {
                currentColorIndex++;
                if (currentColorIndex >= COLORS.length) {
                    currentColorIndex = 0;
                }

                while (newGuess.size() < Mastermind.CODE_LENGTH) {
                    newGuess.add(COLORS[currentColorIndex].ordinal());
                }
                lastGuess = new Code(newGuess);
            } else {
                shufflingPhase = true;
                while (correctColors.size() < Mastermind.CODE_LENGTH) {
                    correctColors.add(lastGuess.getColors().get(correctColors.size()).ordinal());
                }
                List<Integer> shuffledGuess = new ArrayList<>(correctColors);
                Collections.shuffle(shuffledGuess);
                lastGuess = new Code(shuffledGuess);
            }
        } else {
            List<Integer> shuffledGuess = new ArrayList<>(correctColors);
            do {
                Collections.shuffle(shuffledGuess);
            } while (shuffledGuess.equals(lastGuess.getColors().stream()
                    .map(Code.Color::ordinal)
                    .toList()));
            lastGuess = new Code(shuffledGuess);
        }

        return lastGuess;
    }

}