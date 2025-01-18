/*
    Author: Hayley So
    Title: Code.java
    Date: 2025-01-15
 */

package mastermind.core;

import java.util.*;
import mastermind.Mastermind;

/**
 * Represents a Mastermind code consisting of a sequence of colors.
 */
public class Code {

    /**
     * Enum representing the colors that can be used in the code.
     */
    public enum Color {
        GREEN, RED, BLUE, YELLOW, ORANGE, PURPLE;

        /**
         * Converts an index to the corresponding Color enum.
         * 
         * @param index the index of the color
         * @return the corresponding Color enum
         */
        public static Color fromIndex(final int index) {
            return Color.values()[index];
        }

        /**
         * Converts an index to the corresponding Color name as a string.
         * 
         * @param index the index of the color
         * @return the name of the color as a string
         */
        public static String toString(final int index) {
            return fromIndex(index).name();
        }

        /**
         * Converts the color to a string for saving purposes.
         * 
         * @return the color name as a string
         */
        public String toSaveString() {
            return name();
        }
    }

    // The list of colors in the code
    private final ArrayList<Color> code; 

    /**
     * Constructs a Code instance from a list of integers representing color
     * indices.
     * 
     * @param code the list of integers representing the color indices
     */
    public Code(final List<Integer> code) {
        ArrayList<Color> codeBuilder = new ArrayList<>(code.size());
        for (final int color : code) {
            codeBuilder.add(Color.fromIndex(color));
        }
        this.code = codeBuilder;
    }

    /**
     * Constructs a Code instance from a string representation of the code.
     * The string should have space-separated color names (e.g., "RED BLUE GREEN").
     * 
     * @param codeString the string representation of the code
     */
    public Code(String codeString) {
        this.code = new ArrayList<>();
        String[] colors = codeString.split(" ");
        for (String colorString : colors) {
            this.code.add(Color.valueOf(colorString));
        }
    }

    /**
     * Retrieves the color at the specified index.
     * 
     * @param index the index of the color to retrieve
     * @return the color at the specified index
     */
    public Color getColor(final int index) {
        return code.get(index);
    }

    /**
     * Converts a list of integer indices to a list of corresponding colors.
     * 
     * @param codeList the list of integer indices
     * @return a list of corresponding Color enum values
     */
    public static List<Color> getColorFromCode(List<Integer> codeList) {
        List<Color> colorList = new ArrayList<>(codeList.size());
        for (int color : codeList) {
            colorList.add(Color.fromIndex(color));
        }
        return colorList;
    }

    /**
     * Converts the code to a list of integers representing the color indices.
     * 
     * @return a list of integers representing the color indices
     */
    public List<Integer> getCodeInDigits() {
        List<Integer> codeList = new ArrayList<>();
        for (Color color : code) {
            codeList.add(color.ordinal());
        }
        return codeList;
    }

    /**
     * Retrieves the list of colors representing the code.
     * 
     * @return a list of Color enum values representing the code
     */
    public ArrayList<Color> getColors() {
        return new ArrayList<>(code);
    }

    /**
     * Calculates the occurrences of each color in the code.
     * 
     * @return a map with each color and its respective occurrence count in the code
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
     * Validates whether a given index corresponds to a valid color in the Code
     * enum.
     * 
     * @param index the index to validate
     * @return true if the index is valid, false otherwise
     */
    public static boolean isValidColorIndex(int index) {
        return index >= 0 && index < Color.values().length;
    }

    /**
     * Compares this code to another code to check if they are equal.
     * 
     * @param other the other code to compare
     * @return true if the codes are equal, false otherwise
     */
    public boolean equals(Code other) {
        return this.code.equals(other.getColors());
    }

    /**
     * Generates a random code based on the game's difficulty level.
     * The difficulty levels (easy, medium, hard) determine the number of unique
     * colors
     * and the number of colors in the code.
     * 
     * @return a randomly generated Code
     */
    public static Code generateRandomCode() {
        List<Integer> codeList = new ArrayList<>();
        Set<Integer> uniqueColors = new HashSet<>();
        Random random = new Random();
        String level = State.getInstance().getGuessDifficultyLevel();
        int numColors;

        switch (level) {
            case "easy":
                numColors = random.nextInt(3) + 1;
                break;
            case "medium":
                numColors = random.nextInt(2) + 3;
                break;
            case "hard":
                numColors = 4;
                break;
            default:
                numColors = 3;
        }

        while (uniqueColors.size() < numColors) {
            uniqueColors.add(random.nextInt(Color.values().length));
        }

        List<Integer> colorList = new ArrayList<>(uniqueColors);

        if (level.equals("easy")) {
            while (codeList.size() < Mastermind.CODE_LENGTH) {
                codeList.add(colorList.get(random.nextInt(colorList.size())));
            }
        } else {
            Collections.shuffle(colorList);
            for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
                codeList.add(colorList.get(i % colorList.size()));
            }
        }
        return new Code(codeList);
    }

    /**
     * Converts the code to a string representation, where the colors are separated
     * by spaces.
     * 
     * @return the string representation of the code
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.size(); i++) {
            sb.append(code.get(i).toSaveString());
            if (i < code.size() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
