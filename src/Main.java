import java.util.Scanner;

class Main{
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        Game game = new Game();

        game.setRandomCodeEasy();

        game.printCode();

        int[] guess = new int[4];
        for (int i = 0; i < 4; i++) {
            System.out.print("Guess " + (i + 1) + ": ");
            guess[i] = scanner.nextInt();
        }

        char[] result = game.checkCode(guess);

        System.out.println(result);
    }    
}
