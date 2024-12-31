package codebreaker.core;

public class Code {
    public enum Color {
        GREEN, RED, BLUE, YELLOW, ORANGE, PURPLE
    }

    public static Color fromIndex(final int index) {
        return Color.values()[index];
    }

    
}
