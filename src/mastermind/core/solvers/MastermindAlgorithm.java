/*
    Author: Hayley So
    Title: MastermindAlgorithm.java
    Date: 2025-01-15
 */

 package mastermind.core.solvers;

 import javafx.util.Pair;
 import mastermind.core.Code;
 import mastermind.core.Response;
 
 /**
  * Provides the structure for making guesses and evaluating responses in the Mastermind game.
  */
 public abstract class MastermindAlgorithm {
 
     protected Code lastGuess;
     protected static final Code.Color[] COLORS = Code.Color.values();
 
     /**
      * Makes an initial guess for the Mastermind code. This method should be overridden by subclasses
      * to implement a specific guessing strategy.
      * <p>
      * The guess should be a valid code according to the Mastermind game rules. This is the first guess made
      * by the algorithm, and it should not be based on any previous responses.
      * </p>
      * 
      * @return A new guess as a {@link Code} object representing the algorithm's first guess.
      */
     public abstract Code guess();
 
     /**
      * Makes a subsequent guess based on the feedback response received from the previous guess.
      * This method should be overridden by subclasses to implement the specific strategy for generating
      * a new guess after receiving a response (i.e., number of correct and misplaced colors) for the last guess.
      * <p>
      * The response indicates how close the previous guess was to the actual code, and the new guess should
      * be made by narrowing down possibilities based on this feedback.
      * </p>
      *
      * @param response A {@link Pair} of integers representing the response from the last guess:
      *                 - The first integer (correct color and position).
      *                 - The second integer (correct color but incorrect position).
      * @return A new guess as a {@link Code} object representing the next guess to make based on the response.
      */
     public abstract Code guess(Pair<Integer, Integer> response);
 
     /**
      * Evaluates the correctness of a guess by comparing it to the actual code.
      * This method returns the feedback response, which consists of two integers:
      * - The first integer represents the number of pegs that are the correct color and in the correct position.
      * - The second integer represents the number of pegs that are the correct color but in the wrong position.
      * <p>
      * This method is used internally to check how well a guess matches the actual code.
      * </p>
      *
      * @param code  The actual {@link Code} representing the correct solution.
      * @param guess The guessed {@link Code} to compare with the actual code.
      * @return A {@link Pair} of integers representing the feedback response for the guess.
      */
     protected Pair<Integer, Integer> evaluateGuess(Code code, Code guess) {
         Response response = new Response(code, guess);
         return response.getResponse();
     }
 
     /**
      * Gets the last guess made by the algorithm.
      * <p>
      * This method returns the most recent guess generated by the algorithm, which may be used for
      * further evaluation or processing.
      * </p>
      * 
      * @return The last guess made by the algorithm as a {@link Code} object.
      */
     public Code getLastGuess() {
         return lastGuess;
     }
 }
 