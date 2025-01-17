/*
    Author: Hayley So
    Title: GameBoard.java
    Date: 2025-01-15
 */
package mastermind.controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;
import mastermind.Mastermind;
import mastermind.Utils;
import mastermind.core.Code;
import mastermind.core.State;
import mastermind.core.Response;
import mastermind.core.solvers.DonaldKnuthAlgorithm;
import mastermind.core.solvers.EasyAlgorithm;
import mastermind.core.solvers.MastermindAlgorithm;
import mastermind.core.solvers.MediumAlgorithm;
import java.io.IOException;
import javafx.util.Duration;
import java.util.*;

/**
 * Controller class for the Mastermind game board. 
 */
public class GameBoard {
    @FXML
    private Button greenButton;
    @FXML
    private Button redButton;
    @FXML
    private Button blueButton;
    @FXML
    private Button yellowButton;
    @FXML
    private Button orangeButton;
    @FXML
    private Button purpleButton;
    @FXML
    private Button checkButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button blackButton;
    @FXML
    private Button whiteButton;
    @FXML
    private Button nextButton;
    @FXML
    private GridPane guessGrid;
    @FXML
    private GridPane createGrid;
    @FXML
    private GridPane responseGrid;
    @FXML
    private GridPane responseButtons;
    @FXML
    private Text text;

    private String username;
    private String mode;
    private String createLevel;
    private String guessLevel;
    private Pair<Integer, Integer> pegCounts;
    private List<String> guesses = new ArrayList<>();
    private List<String> responses = new ArrayList<>();
    private List<Code> computerGuesses = new ArrayList<>();
    private List<Pair<Integer, Integer>> userResponses = new ArrayList<>();

    private Code generatedCode;
    private Code initialGuess;
    private State state = State.getInstance();
    private MastermindAlgorithm solver;

    private int currentGuessRow;
    private int currentGuessColumn;
    private int currentCreateColumn;
    private int currentResponseRow;
    private int tempResponseRow;

    private int numPegs;
    private long startTime;
    private long endTime;
    private boolean isFirstGuess = true;
    
    /**
     * Constructor
     */
    public GameBoard() {
        this.guesses = new ArrayList<>();
        this.responses = new ArrayList<>();
    }

    /**
     * Initializes the game board, setting up the initial state based on whether there's an unfinished game or a new game needs to be started.
     * 
     * @throws IOException if there's an error loading or saving game state
     */
    @FXML
    public void initialize() throws IOException {
        username = state.getUsername();
        mode = state.getGameMode();

        guessGrid.getChildren().clear();
        createGrid.getChildren().clear();
        responseGrid.getChildren().clear();

        currentGuessRow = 0;
        currentGuessColumn = 0;
        currentCreateColumn = 0;
        currentResponseRow = 0;

        if (Utils.hasUnfinishedGame(username)) {
            loadUnfinishedGame();
        } else {
            startNewGame();
        }

        startTime = System.currentTimeMillis();
        setUpButtons();
        handleButtonActions();
    }

    /**
     * Loads an unfinished game state from saved data
     * 
     * @throws IOException
     */
    private void loadUnfinishedGame() throws IOException {
        String[] gameState = Utils.loadGameState(username);
        mode = gameState[0];

        if ("create".equals(mode)) {
            loadUnfinishedCreateMode(gameState);
        } else if ("guess".equals(mode)) {
            loadUnfinishedGuessMode(gameState);
        }
    }

    /**
     * Set up initial state for a new game based on game mode.
     * 
     * @throws IOException
     */
    private void startNewGame() throws IOException {
        if ("guess".equals(mode)) {
            guessLevel = state.getGuessDifficultyLevel();
            generatedCode = Code.generateRandomCode();
            hideCode();
            text.setText("Please enter your guess.");
            Utils.saveToFile(username, mode, guessLevel, generatedCode);
            responseButtons.setVisible(false);
        } else if ("create".equals(mode)) {
            createLevel = state.getCreateDifficultyLevel();
            text.setText("Press check to start the game.");
            solver = setUpSolver(createLevel);
            initialGuess = solver.guess();
            disableResponseButtons();
            disableColorButtons();
        }
    }

    /**
     * Disables the response buttons including the black, white, and reset button.
     */
    private void disableResponseButtons() {
        blackButton.setDisable(true);
        whiteButton.setDisable(true);
        resetButton.setDisable(true);
    }

    /**
     * Enables guess buttons including the color, check, and reset button.
     */
    private void enableGuessButtons() {
        Button[] colorButtons = { greenButton, redButton, blueButton, yellowButton, orangeButton, purpleButton };
        for (Button button : colorButtons) {
            button.setDisable(false);
        }
        checkButton.setDisable(false);
        resetButton.setDisable(false);
    }

    /**
     * Enable buttons for code reveal in create mode after the game has ended and if the computer could not guess the user's code.
     */
    private void enableButtonsForCodeReveal() {
        checkButton.setDisable(false);
        resetButton.setDisable(false);
        whiteButton.setDisable(true);
        blackButton.setDisable(true);
        Button[] colorButtons = { greenButton, redButton, blueButton, yellowButton, orangeButton, purpleButton };
        for (Button button : colorButtons) {
            button.setDisable(false);
        }
        checkButton.setOnAction(event -> submitCode());
        resetButton.setOnAction(event -> resetCreate());
    }

    /**
     * Set up the solver algorithm based on the selected difficulty.
     * 
     * @param level The difficulty level of the game.
     * @return The initialized algorithm.
     */
    private MastermindAlgorithm setUpSolver(String level) {
        solver = switch (createLevel) {
            case "easy" -> new EasyAlgorithm();
            case "medium" -> new MediumAlgorithm();
            case "hard" -> new DonaldKnuthAlgorithm();
            default -> throw new IllegalStateException(createLevel);
        };
        return solver;
    }

    /**
     * Sets up visual properties of all the game buttons.
     */
    private void setUpButtons() {
        Button[] colorButtons = { greenButton, redButton, blueButton, yellowButton, orangeButton, purpleButton };
        for (Button button : colorButtons) {
            button.setShape(new Circle(15));
            button.setMinSize(30, 30);
            button.setMaxSize(30, 30);
        }
        Button[] responseButtons = { blackButton, whiteButton };
        for (Button button : responseButtons) {
            button.setShape(new Circle(10));
            button.setMinSize(20, 20);
            button.setMaxSize(20, 20);
        }
        resetButton.setShape(new Circle(20));
        resetButton.setMinSize(44, 44);
        resetButton.setMaxSize(44, 44);
        checkButton.setShape(new Circle(20));
        checkButton.setMinSize(44, 44);
        checkButton.setMaxSize(44, 44);
        nextButton.setVisible(false);
    }

    /**
     * Sets up the action handles for the game buttons based on the game mode.
     */
    private void handleButtonActions() {
        if ("guess".equals(mode)) {
            greenButton.setOnAction(event -> addColorToGuessGrid(Code.Color.GREEN));
            redButton.setOnAction(event -> addColorToGuessGrid(Code.Color.RED));
            blueButton.setOnAction(event -> addColorToGuessGrid(Code.Color.BLUE));
            yellowButton.setOnAction(event -> addColorToGuessGrid(Code.Color.YELLOW));
            orangeButton.setOnAction(event -> addColorToGuessGrid(Code.Color.ORANGE));
            purpleButton.setOnAction(event -> addColorToGuessGrid(Code.Color.PURPLE));
            checkButton.setOnAction(event -> submitGuess());
            resetButton.setOnAction(event -> resetGuess());
        }
        if ("create".equals(mode)) {
            checkButton.setOnAction(event -> onCheckButtonClick());
            blackButton.setOnAction(event -> addResponse(Color.BLACK));
            whiteButton.setOnAction(event -> addResponse(Color.WHITE));
            resetButton.setOnAction(event -> resetResponse());
            greenButton.setOnAction(event -> addColorToCreateGrid(Code.Color.GREEN));
            redButton.setOnAction(event -> addColorToCreateGrid(Code.Color.RED));
            blueButton.setOnAction(event -> addColorToCreateGrid(Code.Color.BLUE));
            yellowButton.setOnAction(event -> addColorToCreateGrid(Code.Color.YELLOW));
            orangeButton.setOnAction(event -> addColorToCreateGrid(Code.Color.ORANGE));
            purpleButton.setOnAction(event -> addColorToCreateGrid(Code.Color.PURPLE));
        }
    }

    /**
     * Handles the action when the check button ic clicked during create mode.
     */
    private void onCheckButtonClick() {
        if (isFirstGuess) {
            try {
                Utils.saveToFile(username, mode, createLevel);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            handleInitialGuess();
        } else {
            submitResponse();
        }
    }

    /**
     * Handles the iniital guess in create mode.
     */
    private void handleInitialGuess() {
        displayGuess(initialGuess);
        text.setText("Please add black and/or white pegs or press check again if no correct colors. ");
        if ("hard".equals(createLevel)) {
            text.setText(
                    "Please wait 3 seconds after your first response for the computer's guess. Don't click check again.");
        }
        whiteButton.setDisable(false);
        blackButton.setDisable(false);
        resetButton.setDisable(false);
        isFirstGuess = false;
        computerGuesses.add(initialGuess);
        guesses.add(initialGuess.toString());
        currentGuessRow++;
    }

    /**
     * Adds a color to the create grid.
     * 
     * @param color The color to add.
     */
    private void addColorToCreateGrid(Code.Color color) {
        if (currentCreateColumn < Mastermind.CODE_LENGTH) {
            displayColors(color, createGrid, currentCreateColumn, 0);
            currentCreateColumn++;
        }
    }

    /**
     * Adds a color to the guess grid
     * 
     * @param color The {@link Code.Color} color to add.
     */
    private void addColorToGuessGrid(Code.Color color) {
        if (currentGuessColumn < Mastermind.CODE_LENGTH && currentGuessRow <= Mastermind.NUM_GUESSES) {
            displayColors(color, guessGrid, currentGuessColumn, currentGuessRow);
            currentGuessColumn++;
        }
    }

    /**
     * Displays a color on the specified grid.
     * 
     * @param color the color to display
     * @param grid the grid to display the color on
     * @param col the column to place the color
     * @param row the row to place the color
     */
    private void displayColors(Code.Color color, GridPane grid, int col, int row) {
        Circle dot = new Circle(12);
        dot.setFill(Utils.getGUIColor(color));
        grid.add(dot, col, row);
    }

    /**
     * Resets the current guess row.
     */
    private void resetGuess() {
        guessGrid.getChildren()
                .removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == currentGuessRow);
        currentGuessColumn = 0;
    }

    /**
     * Resets the create grid.
     */
    private void resetCreate() {
        createGrid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) != null);
        currentCreateColumn = 0;
    }

    /**
     * Submits the current guess and processes the game logic.
     */
    private void submitGuess() {
        if (currentGuessColumn < Mastermind.CODE_LENGTH) {
            text.setText("Invalid. Please enter 4 colors.");
            return;
        }
        List<Integer> guessList = new ArrayList<>();

        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            Circle dot = (Circle) guessGrid.getChildren().get(currentGuessRow * Mastermind.CODE_LENGTH + i);
            guessList.add(dot.getFill().equals(Color.SEAGREEN) ? Code.Color.GREEN.ordinal()
                    : dot.getFill().equals(Color.CRIMSON) ? Code.Color.RED.ordinal()
                            : dot.getFill().equals(Color.ROYALBLUE) ? Code.Color.BLUE.ordinal()
                                    : dot.getFill().equals(Color.GOLD) ? Code.Color.YELLOW.ordinal()
                                            : dot.getFill().equals(Color.DARKORANGE) ? Code.Color.ORANGE.ordinal()
                                                    : dot.getFill().equals(Color.MEDIUMPURPLE)
                                                            ? Code.Color.PURPLE.ordinal()
                                                            : -1);
        }
        Code userGuess = new Code(guessList);
        Response resp = new Response(generatedCode, userGuess);
        displayResponse(resp);

        guesses.add(userGuess.toString());
        responses.add(resp.toString());

        try {
            Utils.saveGuessGameState(username, currentGuessRow, guesses, responses);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (resp.getResponse().getKey() == Mastermind.CODE_LENGTH) {
            if (currentGuessRow == 0) {
                text.setText("Congratulations! It took you " + (currentGuessRow + 1) + " guess.");
            } else {
                text.setText("Congratulations! It took you " + (currentGuessRow + 1) + " guesses.");
            }
            revealCode();
            nextButton.setVisible(true);

            endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;
            Utils.updateLeaderBoard(username, guessLevel, currentGuessRow, timeTaken);

            try {
                Utils.deleteGameState(username);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            currentGuessRow++;

            if (resp.getResponse().getKey() == 0 && resp.getResponse().getValue() == 0) {
                int attemptsLeft = Mastermind.NUM_GUESSES - currentGuessRow;
                text.setText("None correct. You have " + attemptsLeft + " attempt" + (attemptsLeft != 1 ? "s" : "") + " left.");
            } else {
                int attemptsLeft = Mastermind.NUM_GUESSES - currentGuessRow;
                text.setText("You have " + attemptsLeft + " attempt" + (attemptsLeft != 1 ? "s" : "") + " left.");
            }            

            if (currentGuessRow >= Mastermind.NUM_GUESSES) {
                text.setText("I'm sorry, you lose.");
                revealCode();
                nextButton.setVisible(true);
                disableAllButtons();

                try {
                    Utils.deleteGameState(username);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        currentGuessColumn = 0;
    }

    /**
     * Displays the response pegs for a guess.
     * 
     * @param resp The {@link Response} object containing the feedback.
     */
    private void displayResponse(Response resp) {
        List<String> pegColors = resp.getPegColors();
        List<Circle> pegs = new ArrayList<>();

        for (String color : pegColors) {
            if ("BLACK".equals(color) || "WHITE".equals(color)) {
                Circle peg = new Circle(4);
                peg.setFill("BLACK".equals(color) ? Color.BLACK : Color.WHITE);
                peg.setStroke(Color.BLACK);
                peg.setStrokeWidth(1.5);
                pegs.add(peg);
            }
        }
        Collections.shuffle(pegs);

        int pegIndex = 0;
        for (Circle peg : pegs) {
            int columnOffset = pegIndex % 2;
            int rowOffset = currentResponseRow + (pegIndex / 2);

            responseGrid.add(peg, columnOffset, rowOffset);
            pegIndex++;
        }
        currentResponseRow += 2;
    }

    /**
     * Submits the code created by the user in create mode.
     */
    private void submitCode() {
        if (currentCreateColumn < Mastermind.CODE_LENGTH) {
            text.setText("Invalid. Please enter 4 colours.");
            return;
        }
        List<Integer> codeList = new ArrayList<>();
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            Circle dot = (Circle) createGrid.getChildren().get(i);
            codeList.add(dot.getFill().equals(Color.SEAGREEN) ? Code.Color.GREEN.ordinal()
                    : dot.getFill().equals(Color.CRIMSON) ? Code.Color.RED.ordinal()
                            : dot.getFill().equals(Color.ROYALBLUE) ? Code.Color.BLUE.ordinal()
                                    : dot.getFill().equals(Color.GOLD) ? Code.Color.YELLOW.ordinal()
                                            : dot.getFill().equals(Color.DARKORANGE) ? Code.Color.ORANGE.ordinal()
                                                    : dot.getFill().equals(Color.MEDIUMPURPLE)
                                                            ? Code.Color.PURPLE.ordinal()
                                                            : -1);
        }
        disableAllButtons();
        Code userCode = new Code(codeList);
        validateResponses(userCode);
        disableAllButtons();
        nextButton.setVisible(true);

        validateResponses(userCode);
    }

    /**
     * Validates the users responses against the actual code.
     * 
     * @param userCode The {@link Code} created by the user.
     */
    private void validateResponses(Code userCode) {
        List<Integer> mistakeRows = new ArrayList<>();

        for (int i = 0; i < computerGuesses.size() && i < userResponses.size(); i++) {
            Code computerGuess = computerGuesses.get(i);
            Response correctResponse = new Response(userCode, computerGuess);
            Pair<Integer, Integer> userResponse = userResponses.get(i);

            int correctBlacks = correctResponse.getResponse().getKey();
            int correctWhites = correctResponse.getResponse().getValue();
            int userBlacks = userResponse.getKey();
            int userWhites = userResponse.getValue();

            if (correctBlacks != userBlacks || correctWhites != userWhites) {
                mistakeRows.add(i + 1);
                System.out.println("Row " + (i + 1) + ": Expected B" + correctBlacks + "W" + correctWhites
                        + " but got B" + userBlacks + "W" + userWhites + " instead.");
            }
        }

        if (mistakeRows.isEmpty()) {
            text.setText("All your responses were correct!");
        } else {
            text.setText("Incorrect responses were entered. Please see the terminal for more details.");
        }

        nextButton.setVisible(true);
    }

    /**
     * Add response pegs to the response grid.
     * 
     * @param color The color {@link Color} of the response peg.
     */
    private void addResponse(Color color) {
        if (numPegs < Mastermind.CODE_LENGTH) {
            int column = numPegs % 2;
            int row = tempResponseRow + (numPegs / 2);

            Circle peg = new Circle(4);
            peg.setFill(color);
            peg.setStroke(Color.BLACK);
            peg.setStrokeWidth(1.5);

            responseGrid.add(peg, column, row);
            numPegs++;
        }
    }

    /**
     * Resets the current response row.
     */
    private void resetResponse() {
        responseGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null &&
                (GridPane.getRowIndex(node) == tempResponseRow ||
                        GridPane.getRowIndex(node) == tempResponseRow + 1));
        numPegs = 0;
        tempResponseRow = currentResponseRow;
    }

    /**
     * Submits the user's response and processes the computer's next guess.
     */
    private void submitResponse() {
        numPegs = 0;
        pegCounts = Utils.countResponsePegs(responseGrid, currentResponseRow);
        Code nextGuess = solver.guess(pegCounts);
        computerGuesses.add(nextGuess);
        userResponses.add(pegCounts);
        responses.add("B" + pegCounts.getKey() + "W" + pegCounts.getValue());
        guesses.add(nextGuess.toString());
    
        try {
            Utils.saveCreateGameState(username, currentGuessRow - 1, guesses, responses);
            Utils.saveNextGuess(username, nextGuess.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        if (pegCounts.getKey() == Mastermind.CODE_LENGTH) {
            text.setText("The computer successfully guessed your code!");
            for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
                displayColors(nextGuess.getColor(i), createGrid, i, 0);
            }
            disableAllButtons();
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> {
                validateResponses(nextGuess);
            });
            pause.play();
        
            try {
                Utils.deleteGameState(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
    
        if (currentGuessRow == Mastermind.NUM_GUESSES) {
            text.setText("The computer couldn't guess your code. Enter your correct code.");
            try {
                Utils.deleteGameState(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
            enableButtonsForCodeReveal();
            return;
        }
    
        if (currentGuessRow < Mastermind.NUM_GUESSES) {
            displayGuess(nextGuess);
        }        
    
        currentResponseRow += 2;
        tempResponseRow = currentResponseRow;
        currentGuessRow++;
        
        text.setText("The computer has made " + currentGuessRow + 
                    " guesses. Please provide feedback, when done press check.");
    }    
    
    /**
     * Displays a guess on the guess grid from the solver.
     * 
     * @param guess The {@link Code} object representing the guess.
     */
    private void displayGuess(Code guess) {
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            displayColors(guess.getColor(i), guessGrid, i, currentGuessRow);
        }
    }

    /**
     * Hides the code in guess mode.
     */
    private void hideCode() {
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            Circle grayCircle = new Circle(12);
            grayCircle.setFill(Color.GRAY);
            createGrid.add(grayCircle, i, 0);
        }
    }

    /**
     * Reveals the secret hode.
     */
    private void revealCode() {
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            Circle dot = new Circle(12);
            dot.setFill(Utils.getGUIColor(generatedCode.getColor(i)));
            createGrid.add(dot, i, 0);
        }
    }

    /**
     * Disables all buttons.
     */
    private void disableAllButtons() {
        checkButton.setDisable(true);
        resetButton.setDisable(true);
        whiteButton.setDisable(true);
        blackButton.setDisable(true);
        disableColorButtons();
    }

    /**
     * Handles the action when the next button is clicked.
     * 
     * @param event the ActionEvent triggered by clicking the next button
     * @throws IOException if there's an error loading the next scene
     */
    @FXML
    void onNextBtnClick(ActionEvent event) throws IOException {
        Utils.loadScene(event, "/mastermind/gui/fxml/GameOver.fxml");
    }

    /**
     * Loads an unfinished game in create mode. 
     * 
     * @param gameState The saved game state
     * @throws IOException
     */
    private void loadUnfinishedCreateMode(String[] gameState) throws IOException {
        createLevel = gameState[1];
        solver = setUpSolver(createLevel);
        List<Code> previousGuesses = new ArrayList<>();
        List<Pair<Integer, Integer>> previousResponses = new ArrayList<>();
        
        List<Pair<String, String>> guessesAndResponses = Utils.loadCreateGuessesAndResponses(username);
        
        for (int i = 0; i < guessesAndResponses.size(); i++) {
            Pair<String, String> pair = guessesAndResponses.get(i);
            Code guess = new Code(pair.getKey());
            computerGuesses.add(guess);
            previousGuesses.add(guess);
            guesses.add(pair.getKey());
            
            if (i < guessesAndResponses.size() - 1) {
                Response response = new Response(pair.getValue());
                Pair<Integer, Integer> responsePair = response.getResponse();
                userResponses.add(responsePair);
                previousResponses.add(responsePair);
                responses.add(pair.getValue());
                displayResponse(response);
                
                if (computerGuesses.size() == 1) {
                    initialGuess = guess;
                    solver.guess();
                } else {
                    solver.guess(responsePair);
                }
            }
            
            displayGuess(guess);
            currentGuessRow++;
            currentResponseRow = currentGuessRow * 2;
        }

        
        currentResponseRow -= 2;
        tempResponseRow = currentResponseRow;
        
        currentGuessColumn = 0;
        isFirstGuess = false;
        numPegs = 0;
        
        text.setText("Welcome back, " + username + ". You are on row " + (currentGuessRow + 1) +  ". Please provide feedback for the computer's guess.");
        disableColorButtons();

        if (solver instanceof DonaldKnuthAlgorithm) {
            Code lastGuess = previousGuesses.get(previousGuesses.size() - 1);
            ((DonaldKnuthAlgorithm) solver).restoreState(lastGuess, previousResponses);
        }
    }
    
    /**
     * Loads an unfinished game in guess mode. 
     * 
     * @param gameState The saved game state
     * @throws IOException
     */
    private void loadUnfinishedGuessMode(String[] gameState) throws IOException {
        guessLevel = gameState[1];
        generatedCode = new Code(gameState[2]);
        hideCode();
    
        List<Pair<String, String>> guessesAndResponses = Utils.loadGuessGuessesAndResponses(username);
        
        for (Pair<String, String> pair : guessesAndResponses) {
            String guessString = pair.getKey();
            String responseString = pair.getValue();
            
            if (guessString != null && !guessString.isEmpty()) {
                Code guess = new Code(guessString);
                displayGuess(guess);
                guesses.add(guessString);
                
                if (responseString != null && !responseString.isEmpty()) {
                    Response response = new Response(responseString);
                    displayResponse(response);
                    responses.add(responseString);
                }
                
                currentGuessRow++;
                currentResponseRow = currentGuessRow * 2;
            }
        }
        
        currentGuessColumn = 0;
        tempResponseRow = currentResponseRow;
        
        text.setText("Welcome back, " + username + ". You are on row " + (currentGuessRow + 1) + ". Please enter your next guess.");
        enableGuessButtons();
        blackButton.setVisible(false);
        whiteButton.setVisible(false);
    }
    
    /**
     * Disables all color buttons.
     */
    public void disableColorButtons() {
        Button[] colorButtons = { greenButton, redButton, blueButton, yellowButton, orangeButton, purpleButton };
        for (Button button : colorButtons) {
            button.setDisable(true);
        }
    }
    
}
