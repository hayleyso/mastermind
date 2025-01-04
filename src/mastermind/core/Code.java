package mastermind.core;

import java.util.*;
import mastermind.Mastermind;

public class Code {
    public enum Color {
        GREEN, RED, BLUE, YELLOW, ORANGE, PURPLE;

        /**
         * 
         * @param index
         * @return
         */
        public static Color fromIndex(final int index) {
            return Color.values()[index];
        }

        /**
         * 
         * @param index
         * @return
         */
        public static String toString(final int index) {
            return fromIndex(index).name();
        }
    }

    private final ArrayList<Color> code;

    /**
     * 
     * @param code
     */
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
     * @return
     */
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

    /**
     * 
     * @param index
     * @return
     */
    public static boolean isValidColorIndex(int index) {
        return index >= 0 && index < Color.values().length;
    }

    /**
     * 
     * @param other
     * @return
     */
    public boolean equals(Code other) {
        return this.code.equals(other.getColors());
    }

    /**
     * 
     * @param other
     * @return
     */    
    @Override
    public String toString() {
        return code.toString(); 
    }

    public Code generateRandomCode() {
        Random random = new Random();
        List<Integer> codeList = new ArrayList<>(Mastermind.CODE_LENGTH);
        for (int i = 0; i < codeList.size(); i++) {
            codeList.add(random.nextInt(Color.values().length));
        }
        return new Code(codeList);
    }

   
}

