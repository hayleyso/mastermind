package mastermind.core;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;
import mastermind.Mastermind;

public class Response {
    private final Pair<Integer, Integer> response; 
    private final List<String> pegColors; 

    public Response(final Code code, final Code guess) {
        int correctCount = 0;
        int misplacedCount = 0;

        HashMap<Code.Color, Integer> codeOccurrences = code.getOccurrences();
        boolean[] guessedCorrectly = new boolean[Mastermind.CODE_LENGTH];
        pegColors = new ArrayList<>(Mastermind.CODE_LENGTH);

        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            Code.Color guessColor = guess.getColor(i);
            Code.Color codeColor = code.getColor(i);

            if (guessColor == codeColor) {
                correctCount++;
                guessedCorrectly[i] = true; 
                pegColors.add("BLACK");
                codeOccurrences.put(codeColor, codeOccurrences.get(codeColor) - 1);
            } else {
                pegColors.add(null); 
            }
        }

        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            if (!guessedCorrectly[i]) { 
                Code.Color guessColor = guess.getColor(i);
                if (codeOccurrences.get(guessColor) > 0) {
                    misplacedCount++;
                    pegColors.set(i, "WHITE"); 
                    codeOccurrences.put(guessColor, codeOccurrences.get(guessColor) - 1);
                } else if (pegColors.get(i) == null) {
                    pegColors.set(i, "NONE");
                }
            }
        }
        response = new Pair<>(correctCount, misplacedCount);
    }

    public Pair<Integer, Integer> getResponse() {
        return this.response;
    }

    public List<String> getPegColors() {
        return pegColors;
    }

    @Override
    public String toString() {
        return "B" + response.getKey() + "B" + "W" + response.getValue();
    }

}
