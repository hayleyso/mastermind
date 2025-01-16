package mastermind.core;

public class State {
    private static volatile State instance;
    private String username;
    private String mode;
    private String createLevel;
    private String guessLevel;
    private boolean isGameFinished;

    private State() {}

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGuessDifficultyLevel() {
        return guessLevel;
    }

    public void setGuessDifficultyLevel(String guessLevel) {
        this.guessLevel = guessLevel;
    }

    public String getCreateDifficultyLevel() {
        return createLevel;
    }

    public void setCreateDifficultyLevel(String createLevel) {
        this.createLevel = createLevel;
    }

    public String getGameMode() {
        return mode;
    }

    public void setGameMode(String gameMode) {
        this.mode = gameMode;
    }

    public void setGameFinished(boolean isFinished) {
        this.isGameFinished = isFinished;
    }    

    public boolean isGameFinished() {
        return isGameFinished;
    }
    
    
}
