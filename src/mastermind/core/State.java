/*
    Author: Hayley So
    Title: State.java
    Date: 2025-01-15
 */

package mastermind.core;

/**
 * Singleton class representing the state of the Mastermind game.
 */
public class State {
    private static volatile State instance;
    private String username;
    private String mode;
    private String createLevel;
    private String guessLevel;
    private boolean isGameFinished;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private State() {}

    /**
     * Returns the single instance of the State class.
     * This method uses double-checked locking to ensure thread safety of the instance.
     *
     * @return the single instance of the State class
     */
    public static State getInstance() {
        if (instance == null) {
            synchronized (State.class) {
                if (instance == null) {
                    instance = new State();
                }
            }
        }
        return instance;
    }

    /**
     * Gets the username of the current player.
     *
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the current player.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the difficulty level for guessing.
     *
     * @return the guess difficulty level
     */
    public String getGuessDifficultyLevel() {
        return guessLevel;
    }

    /**
     * Sets the difficulty level for guessing.
     *
     * @param guessLevel the difficulty level to set
     */
    public void setGuessDifficultyLevel(String guessLevel) {
        this.guessLevel = guessLevel;
    }

    /**
     * Gets the difficulty level for creating the game.
     *
     * @return the create difficulty level
     */
    public String getCreateDifficultyLevel() {
        return createLevel;
    }

    /**
     * Sets the difficulty level for creating the game.
     *
     * @param createLevel the difficulty level to set
     */
    public void setCreateDifficultyLevel(String createLevel) {
        this.createLevel = createLevel;
    }

    /**
     * Gets the current game mode.
     *
     * @return the game mode
     */
    public String getGameMode() {
        return mode;
    }

    /**
     * Sets the current game mode.
     *
     * @param gameMode the game mode to set
     */
    public void setGameMode(String gameMode) {
        this.mode = gameMode;
    }

    /**
     * Sets whether the game is finished or not.
     *
     * @param isFinished true if the game is finished, false otherwise
     */
    public void setGameFinished(boolean isFinished) {
        this.isGameFinished = isFinished;
    }

    /**
     * Checks if the game is finished.
     *
     * @return true if the game is finished, false otherwise
     */
    public boolean isGameFinished() {
        return isGameFinished;
    }
}