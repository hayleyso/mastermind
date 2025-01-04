package mastermind.core;

import java.util.*;

import javafx.util.Pair;
import mastermind.*;
public class Response {
    private final Pair<Integer, Integer> response;

    public Response(final Code code, final Code guess) {
        int correctCount = 0;
        int misplacedCount = 0;

        HashMap<Code.Color, Integer> codeOccurrences = code.getOccurrences();

        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            Code.Color guessColor = guess.getColor(i);
            Code.Color codeColor = code.getColor(i);

            if (guessColor == codeColor) {
                correctCount++;
                codeOccurrences.put(codeColor, codeOccurrences.get(codeColor) - 1);
            }
        }

        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            Code.Color guessColor = guess.getColor(i);
            Code.Color codeColor = code.getColor(i);

            if (guessColor != codeColor && codeOccurrences.get(guessColor) > 0) {
                misplacedCount++;
                codeOccurrences.put(guessColor, codeOccurrences.get(guessColor) - 1);
            }
        }

        response = new Pair<>(correctCount, misplacedCount);
    }

    
}