package mastermind.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import mastermind.Mastermind;

public class Code {
    public enum Color {
        Green, Red, Blue, Yellow, Orange, Purple;

        public static Color fromIndex(final int index) {
            return Color.values()[index];
        }

        public static String toString(final int index) {
            return fromIndex(index).name();
        }
    }

    private final ArrayList<Color> code;

    public Code(final List<Integer> code) {
        ArrayList<Color> codeBuilder = new ArrayList<>(code.size());
        for (final int color : code) {
            codeBuilder.add(Color.fromIndex(color));
        }
        this.code = codeBuilder;
    }
    /**
     * 
     * @param index
     * @return
     */
    public Color getColor(final int index) {
        return code.get(index);
    }
    
    /**
     * 
     * @return
     */
    public ArrayList<Color> getColors() {
        return new ArrayList<>(code); 
    }


    /**
     * 
     * @param filters
     * @return
     */
    public static Code generateRandomCode(List<Function<Code, Boolean>> filters) {
        final Random random = new Random();
        while (true) {
            ArrayList<Integer> codeBuilder = new ArrayList<>(Mastermind.CODE_LENGTH);
            for (int i = 0; i < Mastermind.CODE_LENGTH; ++i) {
                final int randomColor = random.nextInt(Mastermind.NUM_COLORS);
                codeBuilder.add(randomColor);
            }
            final Code generatedCode = new Code(codeBuilder);
            boolean satisfiesAllFilters = filters.stream().allMatch(filter -> filter.apply(generatedCode));
            if (satisfiesAllFilters) {
                return generatedCode;
            }
        }
    }

    public HashMap<Color, Integer> getOccurrences() {
        HashMap<Color, Integer> occurrences = new HashMap<>();
        for (Color color : Color.values()) {
            occurrences.put(color, 0);
        }
        for (Color color : code) {
            occurrences.put(color, occurrences.get(color) + 1);
        }
        return occurrences;
    }

    public static boolean isValidColorIndex(int index) {
        return index >= 0 && index < Color.values().length;
    }

    public boolean equals(Code other) {
        return this.code.equals(other.getColors());
    }

    @Override
    public String toString() {
        return code.toString(); 
    }
}

