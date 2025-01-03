// Importing Needed Libraries
import java.util.Random;
import java.util.ArrayList;

/**
 * Game class that handles the mastermind game with all the needed variables and methods
 */
public class Game {
    // Constants
    private final int NUM_COLORS = 6;
    private final int CODE_LENGTH = 4;
    private final int MAX_GUESSES = 10;

    // Global Variables
    private ArrayList<int[]> allPossibleCodes = new ArrayList<int[]>(1296); //list of all 1296 possible codes (1111 to 6666)
    private int[] code = new int[CODE_LENGTH]; //empty list
    private int[] currentGuess = {1, 1, 2, 2};  // Initial guess
    private boolean won = false;
    private int turn = 1;

    // Global Objects
    private Random randInt = new Random();

    // Constructor Method
    public Game() {
        // Setting All Possible Codes
        for (int i = 0; i < 1296; i++) {
            int[] num = {1, 1, 1, 1};

            num[3] += i;

            for (int j = CODE_LENGTH - 1; j >= 0; j--) {
                while (num[j] > 6) {
                    num[j - 1]++;
                    num[j] -= 6;
                }
            }

            allPossibleCodes.add(num);
        }
    }

    // Debugging Methods
    public void printCode() {
        System.out.println("Code is: ");
        for (int i = 0; i < code.length; i++) {
            System.out.print(code[i] + " ");
        }
        System.out.println();
    }

    // Methods
    public void setRandomCodeEasy() {
        for (int i = 0; i < CODE_LENGTH; i++) {
            code[i] = randInt.nextInt(NUM_COLORS) + 1;
        }
    }

    public void setRandomCodeHard() {

    }

    public char[] checkCode(int[] guess) {
        // Final Result:
        // Black = 'B' (Correct Color and Position)
        // White = 'W' (Correct Color in Wrong Position))
        char[] result = {' ', ' ', ' ', ' '};

        // Amount of white answers and the numbers corresponding to them
        int amountOfWhite = 0;
        int[] whiteNums = {0, 0, 0, 0};
        int[] blackNums = {0, 0, 0, 0};
        boolean alreadyAccounted = false;

        // Adding black results and keeping track of white results
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guess[i] == code[i]) {
                result[i] = 'B';
                blackNums[i] = guess[i];

                for (int j = 0; j < amountOfWhite; j++) {
                    if (guess[i] == whiteNums[j]) {
                        whiteNums[j] = 0;
                        amountOfWhite--;
                    }
                }
            } else {
                for (int j = 0; j < CODE_LENGTH; j++) {
                    if (guess[i] == blackNums[j]) {
                        alreadyAccounted = true;
                        break;
                    }
                }

                if (alreadyAccounted) {
                    alreadyAccounted = false;
                } else {
                    for (int j = 0; j < CODE_LENGTH; j++) {
                        if (guess[i] == code[j]) {
                            whiteNums[amountOfWhite] = guess[i];
                            amountOfWhite++;
                            break;
                        }
                    }
                }
            }
        }

        // Adding white results
        for (int i = 0; i < amountOfWhite; i++) {
            for (int j = 0; j < result.length; j++) {
                if (String.valueOf(result[j]).equals(" ")) {
                    result[j] = 'W';
                    break;
                }
            }
        }

        return result;
    }
}
