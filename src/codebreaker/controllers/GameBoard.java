package codebreaker.controllers;

import codebreaker.CodeBreaker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
public class GameBoard {
    @FXML
    private ImageView background;
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

    public void initialize() {
        Button[] colorButtons = {greenButton, redButton, blueButton, yellowButton, orangeButton, purpleButton};
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
    }
}
