package mastermind.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import mastermind.Mastermind;
import mastermind.core.Code;
import mastermind.core.Response;
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
    protected GridPane guessGrid;
    @FXML
    protected GridPane createGrid;
    @FXML
    protected GridPane responseGrid;

    private int currentGuessRow = 0;
    private int currentGuessColumn = 0;
    private int currentCreateColumn = 0;
    private String gameMode;
    private Code generatedCode;

    public void initialize() {
        clearGrids();
        setUpButtons();
        if ("guess".equals(gameMode)) {
            hideCode();
        } 
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
        initialize();
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

        handleButtonActions();
    }

    private void handleButtonActions() {
        if ("create".equals(gameMode)) {
            greenButton.setOnAction(event -> addColorToCreateGrid(Code.Color.GREEN));
            redButton.setOnAction(event -> addColorToCreateGrid(Code.Color.RED));
            blueButton.setOnAction(event -> addColorToCreateGrid(Code.Color.BLUE));
            yellowButton.setOnAction(event -> addColorToCreateGrid(Code.Color.YELLOW));
            orangeButton.setOnAction(event -> addColorToCreateGrid(Code.Color.ORANGE));
            purpleButton.setOnAction(event -> addColorToCreateGrid(Code.Color.PURPLE));
            checkButton.setOnAction(event -> submitCode());
            resetButton.setOnAction(event -> resetCreate());
        } else if ("guess".equals(gameMode)) {
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
        if (currentGuessColumn < Mastermind.CODE_LENGTH && currentGuessRow < Mastermind.NUM_GUESSES) {
            displayColors(color, guessGrid, currentGuessColumn, currentGuessRow);
            currentGuessColumn++;
        }
    }

    private void displayColors(Code.Color color, GridPane grid, int col, int row) {
        Circle dot = new Circle(14);
        dot.setFill(getColorFromCode(color));
        grid.add(dot, col, row);
    }

    private Color getColorFromCode(Code.Color color) {
        switch (color) {
            case GREEN:
                return Color.SEAGREEN;
            case RED:
                return Color.CRIMSON;
            case BLUE:
                return Color.ROYALBLUE;
            case YELLOW:
                return Color.GOLD;
            case ORANGE:
                return Color.DARKORANGE;
            case PURPLE:
                return Color.MEDIUMPURPLE;
            default:
                return Color.TRANSPARENT;
        }
    }

    private void resetGuess() {
        guessGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == currentGuessRow);
        currentGuessColumn = 0;
    }

    private void resetCreate() {
        createGrid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) != null);
        currentCreateColumn = 0;
    }

    private void submitGuess() {
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

        currentGuessRow++;
    }

    private void submitCode() {
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
        Code userCode = new Code(codeList);
    }

    private void displayResponse(Response response) {

    }

    private void clearGrids() {
        guessGrid.getChildren().clear();
        createGrid.getChildren().clear();
    }

    private void hideCode() {
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            Circle grayCircle = new Circle(14);
            grayCircle.setFill(Color.GRAY);
            createGrid.add(grayCircle, i, 0);
        }
    }

    private void revealCode() {

    }


}
