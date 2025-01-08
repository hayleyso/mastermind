package mastermind.core;

public class State {
    private static State instance;
    
    private String username;
    private String difficultyLevel;
    private String gameMode;

    private State() {}

    public static State getInstance() {
        if (instance == null) {
            instance = new State();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }
}
