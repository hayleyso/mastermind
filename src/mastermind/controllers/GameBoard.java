package mastermind.controllers;

import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mastermind.Mastermind;
import mastermind.core.Code;
import mastermind.core.Response;

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
    protected GridPane guessGrid;
    @FXML
    protected GridPane createGrid;
    @FXML
    protected GridPane responseGrid;
    @FXML
    protected Text text;
    @FXML
    protected Button nextButton;

    private int currentGuessRow;
    private int currentGuessColumn;
    private int currentCreateColumn;
    private int currentResponseRow;

    private String gameMode;
    private Code generatedCode;

    Timeline timeline = new Timeline();

    public void initialize() {
        guessGrid.getChildren().clear();
        createGrid.getChildren().clear();
        responseGrid.getChildren().clear();

        for (int i=0; i<responseGrid.getRowCount(); i++) {
            for (int j=0; j<responseGrid.getColumnCount(); j++) {
                Circle peg = new Circle(4);
                peg.setFill(Color.PALETURQUOISE);
                responseGrid.add(peg, j, i);
            }
        }

        currentGuessRow = 0;
        currentGuessColumn = 0;
        currentCreateColumn = 0;
        currentResponseRow = 0;

        setUpButtons();

        if ("guess".equals(gameMode)) {
            generateRandomCode();
            hideCode();
            text.setText("Please enter your guess.");
        } 
        if ("create".equals(gameMode)) {
            text.setText("Please create a code.");
        }
    }

    private void generateRandomCode() {
        List<Integer> codeList = new ArrayList<>();
        for (int i = 0; i < Mastermind.CODE_LENGTH; i++) {
            codeList.add(new Random().nextInt(Code.Color.values().length));
        }
        generatedCode = new Code(codeList);
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
        initialize();
    }

    private void setUpButtons() {
        Button[] colorButtons = { greenButton, redButton, blueButton, yellowButton, orangeButton, purpleButton };
        for (Button button : colorButtons) {
            button.setShape(new Circle(16));
            button.setMinSize(30, 30);
            button.setMaxSize(30, 30);
        }
        resetButton.setShape(new Circle(20));
        resetButton.setMinSize(44, 44);
        resetButton.setMaxSize(44, 44);
        checkButton.setShape(new Circle(20));
        checkButton.setMinSize(44, 44);
        checkButton.setMaxSize(44, 44);

        nextButton.setVisible(false);

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
        Circle dot = new Circle(12);
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
        if (currentGuessColumn < Mastermind.CODE_LENGTH) {
            text.setText("Please enter four colors.");
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
                    : dot.getFill().equals(Color.MEDIUMPURPLE) ? Code.Color.PURPLE.ordinal() : -1);
        }
        
        Code userGuess = new Code(guessList);
        Response response = new Response(generatedCode, userGuess);
        displayResponse(response);
    
        if (response.getResponse().getKey() == Mastermind.CODE_LENGTH) {
            text.setText("You win!");
            revealCode();
            nextButton.setVisible(true);
        } else {
            currentGuessRow++;
            currentGuessColumn = 0;
           
            if (response.getResponse().getKey() == 0) {
                text.setText("None correct. " + (Mastermind.NUM_GUESSES - currentGuessRow) + " attempts left.");
            } else if (currentGuessRow == Mastermind.NUM_GUESSES - 1) {
                text.setText("1 attempt left");
            } else {
                text.setText((Mastermind.NUM_GUESSES - currentGuessRow) + " attempts left.");
            } 

            if (currentGuessRow >= Mastermind.NUM_GUESSES) {
                text.setText("You lose.");
                revealCode();
                nextButton.setVisible(true);
            }
        }
        currentGuessColumn = 0;
    }
    
    private void displayResponse(Response response) {
        List<String> pegColors = response.getPegColors();
    
        for (int i = 0; i < pegColors.size(); i++) {
            Circle peg = new Circle(4);
    
            if ("BLACK".equals(pegColors.get(i))) {
                peg.setFill(Color.BLACK);
                peg.setStroke(Color.BLACK);
                peg.setStrokeWidth(1.5);
            } else if ("WHITE".equals(pegColors.get(i))) {
                peg.setFill(Color.WHITE);
                peg.setStroke(Color.BLACK);
                peg.setStrokeWidth(1.5);
            } else {
                continue; 
            }
    
            int columnOffset = i % 2; 
            int rowOffset = currentResponseRow + (i / 2);  
    
            responseGrid.add(peg, columnOffset, rowOffset);
        }
        currentResponseRow += (pegColors.size() + 1) / 2;
    }    
    
    private void submitCode() {
        if (currentCreateColumn < Mastermind.CODE_LENGTH) {
            text.setText("Please create a code.");
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
        text.setText("Code created. The computer will now try to guess it!");
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
            dot.setFill(getColorFromCode(generatedCode.getColor(i)));
            createGrid.add(dot, i, 0);
        }
    }


    @FXML
    void onNextBtnClick() throws IOException {
        Parent GameOverParent = FXMLLoader.load(getClass().getResource("/mastermind/gui/fxml/GameOver.fxml"));
        Scene HTP2Scene = new Scene(GameOverParent);
        Stage window = (Stage) greenButton.getScene().getWindow();        
        window.setScene(HTP2Scene);
        window.show();
    }

}
