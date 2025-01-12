package mastermind.controllers;

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
import java.util.*;

public class GameBoard {
    @FXML protected Button greenButton;
    @FXML protected Button redButton;
    @FXML protected Button blueButton;
    @FXML protected Button yellowButton;
    @FXML protected Button orangeButton;
    @FXML protected Button purpleButton;
    @FXML protected Button checkButton;
    @FXML protected Button resetButton;
    @FXML protected Button blackButton;
    @FXML protected Button whiteButton;
    @FXML protected Button nextButton;
    @FXML protected Button questionButton;
    @FXML protected GridPane guessGrid;
    @FXML protected GridPane createGrid;
    @FXML protected GridPane responseGrid;
    @FXML protected GridPane responseButtons;
    @FXML protected Text text;

    private String username;
    private String mode;
    private String guessLevel; // for file input
    private String createLevel;
    private Pair<Integer, Integer> pegCounts;
    private List<String> guesses = new ArrayList<>();
    private List<String> responses = new ArrayList<>();
    private List<Code> computerGuesses = new ArrayList<>();
    private List<Pair<Integer, Integer>> userResponses = new ArrayList<>();

    private Code generatedCode;
    private State state = State.getInstance();
    private MastermindAlgorithm solver;

    private int currentGuessRow;
    private int currentGuessColumn;
    private int currentCreateColumn;
    private int currentResponseRow;
    private int tempResponseRow;

    private int numPegs;
    private int numBlack;
    private int numWhite;

    private long startTime;
    private long endTime;
    private boolean isGameFinished;

    public GameBoard() {
        this.guesses = new ArrayList<>();
        this.responses = new ArrayList<>();
    }

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
        // numPegs = 0;

        if ("guess".equals(mode)) {
            generatedCode = Code.generateRandomCode();
            hideCode();
            text.setText("Please enter your guess.");
            Utils.saveToFile(username, mode, generatedCode);
            responseButtons.setVisible(false);
        }
        if ("create".equals(mode)) {
            createLevel = state.getCreateDifficultyLevel();
            text.setText("Press check to start the game.");
            createLevel = state.getCreateDifficultyLevel();

            solver = switch (createLevel) {
                case "easy" -> new EasyAlgorithm();
                case "medium" -> new MediumAlgorithm();
                case "hard" -> new DonaldKnuthAlgorithm(createLevel);
                default -> throw new IllegalStateException(createLevel);
            };
            
            blackButton.setDisable(true);
            whiteButton.setDisable(true);
            resetButton.setDisable(true);
            Button[] colorButtons = { greenButton, redButton, blueButton, yellowButton, orangeButton, purpleButton };
            for (Button button : colorButtons) {
                button.setDisable(true);
            }
            Utils.saveToFile(username, mode, createLevel);


        }

        startTime = System.currentTimeMillis();

        setUpButtons();
        handleButtonActions();
    }

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
        questionButton.setShape(new Circle(10));
        nextButton.setVisible(false);
    }

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

    private void onCheckButtonClick() {
        if (isFirstGuess) {
            handleInitialGuess();
        } else {
            submitResponse();
        }
    }
    
    private void handleInitialGuess() {
        Code initialGuess = solver.guess();
        displayGuess(initialGuess);
        text.setText("Please add black and/or white pegs or press check again if no correct colors.");
        whiteButton.setDisable(false);
        blackButton.setDisable(false);
        resetButton.setDisable(false);
        isFirstGuess = false;
        currentGuessRow++;
    }

    private void addColorToCreateGrid(Code.Color color) {
        if (currentCreateColumn < Mastermind.CODE_LENGTH) {
            displayColors(color, createGrid, currentCreateColumn, 0);
            currentCreateColumn++;
        }
    }

    private void addColorToGuessGrid(Code.Color color) {
        if (currentGuessColumn < Mastermind.CODE_LENGTH && currentGuessRow <= Mastermind.NUM_GUESSES) {
            displayColors(color, guessGrid, currentGuessColumn, currentGuessRow);
            currentGuessColumn++;
        }
    }

    private void displayColors(Code.Color color, GridPane grid, int col, int row) {
        Circle dot = new Circle(12);
        dot.setFill(Utils.getGUIColor(color));
        grid.add(dot, col, row);
    }

    private void resetGuess() {
        guessGrid.getChildren()
                .removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == currentGuessRow);
        currentGuessColumn = 0;
    }

    private void resetCreate() {
        createGrid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) != null);
        currentCreateColumn = 0;
    }

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
            Utils.saveGameState(username, currentGuessRow, guesses, responses);
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
            disableAllButtons();
            nextButton.setVisible(true);

            endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;
            isGameFinished = true;

            try {
                Utils.deleteGameState(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Utils.updateGuessLeaderBoard(username, currentGuessRow + 1, timeTaken);

        } else {
            currentGuessRow++;

            if (resp.getResponse().getKey() == 0 && resp.getResponse().getValue() == 0) {
                text.setText("None correct. " + (Mastermind.NUM_GUESSES - currentGuessRow) + " attempt" +
                        ((Mastermind.NUM_GUESSES - currentGuessRow) != 1 ? "s" : "") + " left.");
            } else {
                int attemptsLeft = Mastermind.NUM_GUESSES - currentGuessRow;
                text.setText(attemptsLeft + " attempt" + (attemptsLeft != 1 ? "s" : "") + " left.");
            }

            if (currentGuessRow >= Mastermind.NUM_GUESSES) {
                text.setText("I'm sorry, you lose.");
                revealCode();
                nextButton.setVisible(true);
                disableAllButtons();
                isGameFinished = true;
                try {
                    Utils.deleteGameState(username);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        currentGuessColumn = 0;
    }

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

        List<Pair<Integer, Pair<Integer, Integer>>> mistakes = Utils.verifyUserResponses(userCode, computerGuesses, userResponses);
    
        if (mistakes.isEmpty()) {
            text.setText("All your responses were correct! The computer couldn't guess your code.");
        } else {
            StringBuilder errorMessage = new StringBuilder("Incorrect responses were entered.\n");
            for (Pair<Integer, Pair<Integer, Integer>> mistake : mistakes) {
                int row = mistake.getKey();
                int black = mistake.getValue().getKey();
                int white = mistake.getValue().getValue();
                errorMessage.append(String.format("Row %d: Correct response should be %d black, %d white\n.", row, black, white));
            }
            text.setText(errorMessage.toString());
        }
        
        disableAllButtons();
        nextButton.setVisible(true);

    }

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
    
    private void resetResponse() {
        responseGrid.getChildren().removeIf(node -> 
            GridPane.getRowIndex(node) != null && 
            (GridPane.getRowIndex(node) == tempResponseRow || 
            GridPane.getRowIndex(node) == tempResponseRow + 1)
        );
        numPegs = 0;
        tempResponseRow = currentResponseRow;
    }
    private boolean isFirstGuess = true;
    
    private void submitResponse() {
        numPegs = numBlack = numWhite = 0;
        pegCounts = Utils.countResponsePegs(responseGrid, currentResponseRow);
        Code nextGuess = solver.guess(pegCounts);
    
        if (pegCounts.getKey() == Mastermind.CODE_LENGTH) {
            text.setText("The computer successfully guessed your code!");
            for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
                displayColors(nextGuess.getColor(i), createGrid, i, 0);
            }
            isGameFinished = true;
            disableAllButtons();
            nextButton.setVisible(true);
            return;
        }
    
        if (currentGuessRow == Mastermind.NUM_GUESSES) {
            text.setText("The computer couldn't guess your code. Enter your correct code.");
            isGameFinished = true;
            enableButtonsForCodeReveal();
            return;
        }
    
        if (currentGuessRow < Mastermind.NUM_GUESSES) {
            displayGuess(nextGuess);
        }        
        currentResponseRow += 2;
        tempResponseRow = currentResponseRow;
        currentGuessRow++;
        
        text.setText("The computer has made " + currentGuessRow + " guesses. Please provide feedback, when done press check.");

        computerGuesses.add(nextGuess);
        userResponses.add(pegCounts);
    }
    
    // computer's guess
    private void displayGuess(Code guess) {
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            displayColors(guess.getColor(i), guessGrid, i, currentGuessRow);
        }
    }

    private void hideCode() {
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            Circle grayCircle = new Circle(14);
            grayCircle.setFill(Color.GRAY);
            createGrid.add(grayCircle, i, 0);
        }
    }

    private void revealCode() {
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            Circle dot = new Circle(14);
            dot.setFill(Utils.getGUIColor(generatedCode.getColor(i)));
            createGrid.add(dot, i, 0);
        }
    }

    
    private void disableAllButtons() {
        checkButton.setDisable(true);
        resetButton.setDisable(true);
        whiteButton.setDisable(true);
        blackButton.setDisable(true);
        Button[] colorButtons = { greenButton, redButton, blueButton, yellowButton, orangeButton, purpleButton };
        for (Button button : colorButtons) {
            button.setDisable(true);
        }
    }

    @FXML
    void onNextBtnClick(ActionEvent event) throws IOException {
        Utils.loadScene(event, "/mastermind/gui/fxml/GameOver.fxml");
    }

    @FXML
    void onQuestionBtnClick(ActionEvent event) throws IOException {
        // TODO add help window popup
        // add one for guess
        // one for create
    }

    // public void loadGameState() throws IOException {
    //     username = state.getUsername();
    //     File file = new File(Utils.DIRECTORY_PATH + username + ".txt");

    //     try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
    //         String line;
    //         currentGuessRow = 0;

    //         mode = reader.readLine(); // first line

    //         if ("guess".equals(mode)) {
    //             // second line for guess
    //             generatedCode = new Code(Arrays.stream(reader.readLine().split(""))
    //                     .map(s -> Code.Color.valueOf(s).ordinal())
    //                     .collect(Collectors.toList()));

    //         }
    //         if ("create".equals(mode)) {
    //             // second line for create
    //             createLevel = reader.readLine();

    //         }

    //         // read the current guess and response
    //         // for create mode also check if user has not responded yet
    //         while ((line = reader.readLine()) != null) {
    //             String[] parts = line.split("\\|");
    //             if (parts.length < 2)
    //                 continue;

    //             String guess = parts[0];
    //             String response = parts[1];

    //             for (int j = 0; j < guess.length(); j++) {
    //                 Code.Color color = Code.Color.valueOf(String.valueOf(guess.charAt(j)));
    //                 displayColors(color, guessGrid, j, currentGuessRow);
    //             }

    //             List<Integer> colorIndices = Arrays.stream(guess.split(""))
    //                     .map(s -> Code.Color.valueOf(s).ordinal())
    //                     .collect(Collectors.toList());

    //             Response resp = new Response(generatedCode, new Code(colorIndices));
    //             displayResponse(resp);

    //             guesses.add(guess);
    //             responses.add(response);
    //             currentGuessRow++;
    //         }
    //         currentGuessColumn = 0;
    //     }

    // }

}
