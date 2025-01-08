package mastermind.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import mastermind.Mastermind;
import mastermind.MastermindUtils;
import mastermind.core.Code;
import mastermind.core.GameState;
import mastermind.core.Response;
import mastermind.core.solvers.MastermindAlgorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GameBoard {
    @FXML
    protected Button greenButton;
    @FXML
    protected Button redButton;
    @FXML
    protected Button blueButton;
    @FXML
    protected Button yellowButton;
    @FXML
    protected Button orangeButton;
    @FXML
    protected Button purpleButton;
    @FXML
    protected Button checkButton;
    @FXML
    protected Button resetButton;
    @FXML
    protected Button questionButton;
    @FXML
    protected GridPane guessGrid;
    @FXML
    protected GridPane createGrid;
    @FXML
    protected GridPane responseGrid;
    @FXML
    protected Text text;
    @FXML
    protected Button nextButton;

    private String username;
    private String mode;
    private String code;
    private String difficultyLevel;
    private List<String> guesses = new ArrayList<>();
    private List<String> responses = new ArrayList<>();

    private Code generatedCode;
    private GameState gameState = GameState.getInstance();
    private MastermindAlgorithm solver;

    private int currentGuessRow;
    private int currentGuessColumn;
    private int currentCreateColumn;
    private int currentResponseRow;

    private long startTime;
    private long endTime;
    private boolean isGameFinished;

    public void initialize() throws IOException {
        guessGrid.getChildren().clear();
        createGrid.getChildren().clear();
        responseGrid.getChildren().clear();

        for (int i = 0; i < responseGrid.getRowCount(); i++) {
            for (int j = 0; j < responseGrid.getColumnCount(); j++) {
                Circle peg = new Circle(4);
                peg.setFill(Color.PALETURQUOISE);
                responseGrid.add(peg, j, i);
            }
        }

        currentGuessRow = 0;
        currentGuessColumn = 0;
        currentCreateColumn = 0;
        currentResponseRow = 0;

        username = gameState.getUsername();
        mode = gameState.getGameMode();
        difficultyLevel = gameState.getDifficultyLevel();

        MastermindUtils.addName(username);

        setUpButtons();

        if ("guess".equals(mode)) {
            generatedCode = Code.generateRandomCode();
            hideCode();
            text.setText("Please enter your guess.");
        }
        if ("create".equals(mode)) {
            text.setText("Please create your code.");
        }
        startTime = System.currentTimeMillis();

        String basePath = "C:\\Users\\hayle\\Documents\\GitHub\\ICS4U1\\Mastermind\\src\\mastermind\\data\\users\\";
        String modePath = basePath + mode;
        String filename = modePath + "\\" + username + ".txt";

        try {
            FileWriter f = new FileWriter(filename);
            f.write(mode);
            if (mode.equals("guess")) {
                // generated code
            }
            if (mode.equals("create")) {
                f.write(difficultyLevel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setUpButtons() {
        Button[] colorButtons = { greenButton, redButton, blueButton, yellowButton, orangeButton, purpleButton };
        for (Button button : colorButtons) {
            button.setShape(new Circle(15));
            button.setMinSize(30, 30);
            button.setMaxSize(30, 30);
        }
        resetButton.setShape(new Circle(20));
        resetButton.setMinSize(44, 44);
        resetButton.setMaxSize(44, 44);
        checkButton.setShape(new Circle(20));
        checkButton.setMinSize(44, 44);
        checkButton.setMaxSize(44, 44);
        questionButton.setShape(new Circle(10));
        nextButton.setVisible(false);

        handleButtonActions();
    }

    private void handleButtonActions() {
        if ("create".equals(mode)) {
            greenButton.setOnAction(event -> addColorToCreateGrid(Code.Color.GREEN));
            redButton.setOnAction(event -> addColorToCreateGrid(Code.Color.RED));
            blueButton.setOnAction(event -> addColorToCreateGrid(Code.Color.BLUE));
            yellowButton.setOnAction(event -> addColorToCreateGrid(Code.Color.YELLOW));
            orangeButton.setOnAction(event -> addColorToCreateGrid(Code.Color.ORANGE));
            purpleButton.setOnAction(event -> addColorToCreateGrid(Code.Color.PURPLE));
            checkButton.setOnAction(event -> submitCode());
            resetButton.setOnAction(event -> resetCreate());
        } else if ("guess".equals(mode)) {
            greenButton.setOnAction(event -> addColorToGuessGrid(Code.Color.GREEN));
            redButton.setOnAction(event -> addColorToGuessGrid(Code.Color.RED));
            blueButton.setOnAction(event -> addColorToGuessGrid(Code.Color.BLUE));
            yellowButton.setOnAction(event -> addColorToGuessGrid(Code.Color.YELLOW));
            orangeButton.setOnAction(event -> addColorToGuessGrid(Code.Color.ORANGE));
            purpleButton.setOnAction(event -> addColorToGuessGrid(Code.Color.PURPLE));
            checkButton.setOnAction(event -> submitGuess());
            resetButton.setOnAction(event -> resetGuess());
        }
    }

    protected void addColorToCreateGrid(Code.Color color) {
        if (currentCreateColumn < Mastermind.CODE_LENGTH) {
            displayColors(color, createGrid, currentCreateColumn, 0);
            currentCreateColumn++;
        }
    }

    private void addColorToGuessGrid(Code.Color color) {
        if (currentGuessColumn >= Mastermind.CODE_LENGTH && currentGuessRow <= Mastermind.NUM_GUESSES) {
            displayColors(color, guessGrid, currentGuessColumn, currentGuessRow);
            currentGuessColumn++;
        }
    }

    private void displayColors(Code.Color color, GridPane grid, int col, int row) {
        Circle dot = new Circle(12);
        dot.setFill(MastermindUtils.getColor(color));
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
        Response response = new Response(generatedCode, userGuess);
        displayResponse(response);

        if (response.getResponse().getKey() == Mastermind.CODE_LENGTH) {
            text.setText("Congratulations! It took you " + (currentGuessRow + 1) + " guesses.");
            revealCode();
            disableButtons();
            nextButton.setVisible(true);

            endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;
            isGameFinished = true;

            try {
                MastermindUtils.removeName(username);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            MastermindUtils.updateGuessLeaderBoard(username, currentGuessRow + 1, timeTaken);
            MastermindUtils.deleteGameState(username, mode);

        } else {
            currentGuessRow++;
            currentGuessColumn = 0;

            if (response.getResponse().getKey() == 0 && response.getResponse().getValue() == 0) {
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
                disableButtons();
                isGameFinished = true;
            }
        }
        currentGuessColumn = 0;
    }

    private void displayResponse(Response response) {
        List<String> pegColors = response.getPegColors();
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
        Collections.shuffle(pegs); // randomize display order

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
        text.setText("Code created. The computer will now try to guess it.");
        disableButtons();
        Code userCode = new Code(codeList);
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
            dot.setFill(MastermindUtils.getColor(generatedCode.getColor(i)));
            createGrid.add(dot, i, 0);
        }
    }

    private void disableButtons() {
        Button[] colorButtons = { greenButton, redButton, blueButton, yellowButton, orangeButton, purpleButton };

        for (Button button : colorButtons) {
            button.setDisable(true);
        }
        checkButton.setDisable(true);
        resetButton.setDisable(true);
    }

    @FXML
    void onNextBtnClick(ActionEvent event) throws IOException {
        MastermindUtils.loadScene(event, "/mastermind/gui/fxml/GameOver.fxml");
    }

    @FXML
    void onQuestionBtnClick(ActionEvent event) throws IOException {
        // TODO add help window popup
    }

}
