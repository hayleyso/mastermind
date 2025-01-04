package mastermind.controllers;

import mastermind.*;
import mastermind.core.*;
import mastermind.core.Code;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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
    private GridPane guessGrid;

    private Code actualCode;

    private int currentRow = 0; 
    private int currentCol = 0; 

    public void initialize() {
        setUpButtons();
        
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

        greenButton.setOnAction(event -> addColor(Code.Color.Green));
        redButton.setOnAction(event -> addColor(Code.Color.Red));
        blueButton.setOnAction(event -> addColor(Code.Color.Blue));
        yellowButton.setOnAction(event -> addColor(Code.Color.Yellow));
        orangeButton.setOnAction(event -> addColor(Code.Color.Orange));
        purpleButton.setOnAction(event -> addColor(Code.Color.Purple));

        checkButton.setOnAction(event -> submitSelection());
        resetButton.setOnAction(event -> resetSelection());
    }

    private void addColor(Code.Color color) {
        if (currentCol < Mastermind.CODE_LENGTH) {
            displayColors(color);
            currentCol++;

            if (currentCol >= Mastermind.CODE_LENGTH) {
                currentCol = 0;
                currentRow++;
            }
            
            if (currentRow >= Mastermind.MAX_GUESSES) {
                currentRow = Mastermind.MAX_GUESSES - 1; 
            }
        }
    }

    private void displayColors(Code.Color color) {
        Circle dot = new Circle(12);
        dot.setFill(getColorFromCode(color));
        
        guessGrid.add(dot, currentCol, currentRow); 
    }

    private Color getColorFromCode(Code.Color color) {
        switch (color) {
            case Green: return Color.SEAGREEN;
            case Red: return Color.CRIMSON;
            case Blue: return Color.ROYALBLUE;
            case Yellow: return Color.GOLD;
            case Orange: return Color.DARKORANGE;
            case Purple: return Color.MEDIUMPURPLE;
            default: return Color.TRANSPARENT; 
        }
    }

    private void submitSelection() {
        
    }

    private void resetSelection() {
        currentRow = 0; 
        currentCol = 0; 
        guessGrid.getChildren().clear();
    }
}
