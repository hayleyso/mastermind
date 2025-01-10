package mastermind.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import mastermind.Mastermind;
import mastermind.Utils;
import mastermind.core.Code;
import mastermind.core.State;
import mastermind.core.Response;
import mastermind.core.solvers.MastermindAlgorithm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.classfile.components.ClassPrinter.Node;
import java.util.*;
import java.util.stream.Collectors;

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
    protected Button blackButton;
    @FXML
    protected Button whiteButton;
    @FXML
    protected Button noneButton;
    @FXML
    protected Button nextButton;
    @FXML
    protected Button questionButton;
    @FXML
    protected GridPane guessGrid;
    @FXML
    protected GridPane createGrid;
    @FXML
    protected GridPane responseGrid;
    @FXML
    protected GridPane responseButtons; 
    @FXML
    protected Text text;
    
    private String username;
    private String mode;
    private String guessLevel;
    private String createLevel;
    private List<String> guesses = new ArrayList<>();
    private List<String> responses = new ArrayList<>();

    private Code generatedCode;
    private State state = State.getInstance();
    private MastermindAlgorithm solver;

    private int currentGuessRow;
    private int currentGuessColumn;
    private int currentCreateColumn;
    private int currentResponseRow;

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

        for (int i = 0; i < responseGrid.getRowCount(); i++) {
            for (int j = 0; j < responseGrid.getColumnCount(); j++) {
                Circle peg = new Circle(4);
                peg.setFill(Color.PALETURQUOISE);
                responseGrid.add(peg, j, i);
            }
        }

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
            Utils.saveToFile(username, mode, createLevel);
            // disable color button until user returns all black or
            // if attempts == 10
        }
        startTime = System.currentTimeMillis();

        setUpButtons();
        handleButtonActions();

        // add overwriting if the user already has a game in progress
        // and does not finish again
    }

    private void setUpButtons() {
        Button[] colorButtons = { greenButton, redButton, blueButton, yellowButton, orangeButton, purpleButton };
        for (Button button : colorButtons) {
            button.setShape(new Circle(15));
            button.setMinSize(30, 30);
            button.setMaxSize(30, 30);
        }
        Button[] responseButtons = { blackButton, whiteButton, noneButton };
        for (Button button : responseButtons) {
            button.setShape(new Circle(6));
            button.setMinSize(15, 15);
            button.setMaxSize(15, 15);
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
            greenButton.setOnAction(event -> addColorToCreateGrid(Code.Color.GREEN));
            redButton.setOnAction(event -> addColorToCreateGrid(Code.Color.RED));
            blueButton.setOnAction(event -> addColorToCreateGrid(Code.Color.BLUE));
            yellowButton.setOnAction(event -> addColorToCreateGrid(Code.Color.YELLOW));
            orangeButton.setOnAction(event -> addColorToCreateGrid(Code.Color.ORANGE));
            purpleButton.setOnAction(event -> addColorToCreateGrid(Code.Color.PURPLE));
            checkButton.setOnAction(event -> submitResponse());
            resetButton.setOnAction(event -> resetResponse());
            
            if (isGameFinished) {
                checkButton.setOnAction(event -> submitCode());
                resetButton.setOnAction(event -> resetCreate());
            }
            blackButton.setOnAction(event -> addColorToResponse(Color.BLACK));
            whiteButton.setOnAction(event -> addColorToResponse(Color.WHITE));
            noneButton.setOnAction(event -> addColorToResponse(Color.TRANSPARENT));
        } 
    }

    protected void addColorToCreateGrid(Code.Color color) {
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

    private void addColorToResponse(Color color) {
        if (currentResponseRow < 4 && currentResponseRow < Mastermind.NUM_GUESSES * 2) {
            int column = (currentResponseRow % 2);
            Circle peg = new Circle(4);
            peg.setFill(color);
            if (color != Color.TRANSPARENT) {
                peg.setStroke(Color.BLACK);
                peg.setStrokeWidth(1.5);
            }
            responseGrid.add(peg, column, currentResponseRow/2);
            currentResponseRow++;
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
        Response response = new Response(generatedCode, userGuess);
        displayResponse(response);

        guesses.add(userGuess.toString());
        responses.add(response.toString());

        try {
            Utils.saveGameState(username, currentGuessRow, guesses, responses);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (response.getResponse().getKey() == Mastermind.CODE_LENGTH) {
            if (currentGuessRow == 0) {
                text.setText("Congratulations! It took you " + (currentGuessRow + 1) + " guess.");
            } else {
                text.setText("Congratulations! It took you " + (currentGuessRow + 1) + " guesses.");
            }
            revealCode();
            disableButtons();
            nextButton.setVisible(true);

            endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;
            isGameFinished = true;

            try {
                Utils.deleteGameState(username);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Utils.updateGuessLeaderBoard(username, currentGuessRow + 1, timeTaken);

        } else {
            currentGuessRow++;

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
                try {
                    Utils.deleteGameState(username);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
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

    // only enable buttons at the end
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
        // text.setText("Code created. The computer will now try to guess it.");
        disableButtons();
        Code userCode = new Code(codeList);

        // implement method that compares to previous guesses
        // gets response
        // compares response to the user's entered response
        // display text any errors
    }

    // get the # of black and white pegs per guess
    private void submitResponse() {
        for (int i = 0; i < responseGrid.getRowCount(); i++) {
            for (int j = 0; j < responseGrid.getColumnCount(); j++) {
                Circle peg = (Circle) Utils.getNodeByRowColumnIndex(responseGrid, i, j);
                if (peg.getFill().equals(Color.BLACK)) {
                    numBlack++;
                } else if (peg.getFill().equals(Color.WHITE)) {
                    numWhite++;
                }
            }
        }
        currentResponseRow += 2;      
        System.out.println(numBlack + " " + numWhite);
    }

    private void resetResponse() {
        responseGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null);
        currentResponseRow = 0;
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
        Utils.loadScene(event, "/mastermind/gui/fxml/GameOver.fxml");
    }

    @FXML
    void onQuestionBtnClick(ActionEvent event) throws IOException {
        // TODO add help window popup
        // add one for guess
        // one for create
    }

    public void loadGameState() throws IOException {
        username = state.getUsername();
        File file = new File(Utils.DIRECTORY_PATH + username + ".txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            currentGuessRow = 0;

            mode = reader.readLine(); // first line

            if ("guess".equals(mode)) {
                // second line for guess
                generatedCode = new Code(Arrays.stream(reader.readLine().split(""))
                        .map(s -> Code.Color.valueOf(s).ordinal())
                        .collect(Collectors.toList()));
               
            } if ("create".equals(mode)) {
                // second line for create
                createLevel = reader.readLine();
                
            }

            // read the current guess and response
            // for create mode also check if user has not responded yet
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 2)
                    continue;

                String guess = parts[0];
                String response = parts[1];

                for (int j = 0; j < guess.length(); j++) {
                    Code.Color color = Code.Color.valueOf(String.valueOf(guess.charAt(j)));
                    displayColors(color, guessGrid, j, currentGuessRow);
                }

                List<Integer> colorIndices = Arrays.stream(guess.split(""))
                        .map(s -> Code.Color.valueOf(s).ordinal())
                        .collect(Collectors.toList());

                Response resp = new Response(generatedCode, new Code(colorIndices));
                displayResponse(resp);

                guesses.add(guess);
                responses.add(response);
                currentGuessRow++;
            }
            currentGuessColumn = 0;
        }

    }

}
