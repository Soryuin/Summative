/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wordle;

/**
 *
 * @author azhan52
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class userInterface {

    private int wordLength;
    private int maxAttempts;
    private wordleGame game;

    private Stage stage;
    private GridPane grid;
    private TextField inputField;
    private Button submitButton;
    private Label messageLabel;

    private int currentRow = 0;
    private Label[][] letterLabels;

    // Keyboard keys and their buttons
    private final String[] keyboardRows = {
            "QWERTYUIOP",
            "ASDFGHJKL",
            "ZXCVBNM"
    };
    private Map<Character, Button> keyboardButtons = new HashMap<>();

    public userInterface(int wordLength, int maxAttempts) {
        this.wordLength = wordLength;
        this.maxAttempts = maxAttempts;
        this.letterLabels = new Label[maxAttempts][wordLength];
    }

    public void setGame(wordleGame game) {
        this.game = game;
    }

    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("Wordle Game");

        BorderPane root = new BorderPane();

        // Grid for guesses
        grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);

        for (int row = 0; row < maxAttempts; row++) {
            for (int col = 0; col < wordLength; col++) {
                Label lbl = new Label(" ");
                lbl.setPrefSize(40, 40);
                lbl.setAlignment(Pos.CENTER);
                lbl.setFont(Font.font("Comic Sans MS", 24));
                lbl.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
                grid.add(lbl, col, row);
                letterLabels[row][col] = lbl;
            }
        }

        // Input box
        HBox inputBox = new HBox(10);
        inputBox.setAlignment(Pos.CENTER);

        inputField = new TextField();
        inputField.setPrefWidth(wordLength * 40);
        inputField.setFont(Font.font("Comic Sans MS", 18));
        inputField.setPromptText("Enter your guess");

        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Comic Sans MS", 18));

        inputBox.getChildren().addAll(inputField, submitButton);

        // Message label
        messageLabel = new Label("Welcome to Wordle!");
        messageLabel.setFont(Font.font("Comic Sans MS", 16));
        messageLabel.setTextFill(Color.BLUE);
        messageLabel.setAlignment(Pos.CENTER);
       
        // Keyboard pane
        VBox keyboardPane = new VBox(5);
        keyboardPane.setAlignment(Pos.CENTER);
        keyboardPane.setPadding(new Insets(10, 0, 20, 0));

        for (String row : keyboardRows) {
            HBox rowBox = new HBox(5);
            rowBox.setAlignment(Pos.CENTER);
            for (char c : row.toCharArray()) {
                Button keyButton = new Button(String.valueOf(c));
                keyButton.setPrefSize(35, 45);
                keyButton.setFont(Font.font("Comic Sans MS", 14));
                keyButton.setFocusTraversable(false);
                keyButton.setDisable(true); // not clickable, just display
                
                keyboardButtons.put(c, keyButton);
                rowBox.getChildren().add(keyButton);
            }
            keyboardPane.getChildren().add(rowBox);
        }

        root.setTop(messageLabel);
        BorderPane.setAlignment(messageLabel, Pos.CENTER);
        BorderPane.setMargin(messageLabel, new Insets(10, 0, 10, 0));

        root.setCenter(grid);
        root.setBottom(new VBox(inputBox, keyboardPane));

        Scene scene = new Scene(root, wordLength * 50 + 100, maxAttempts * 60 + 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Submit button action
        submitButton.setOnAction(e -> handleSubmit());

        // Submit on Enter key press
        inputField.setOnAction(e -> handleSubmit());
    }

    private void handleSubmit() {
        String guess = inputField.getText().trim().toUpperCase();

        if (guess.length() != wordLength) {
            messageLabel.setText("Guess must be exactly " + wordLength + " letters.");
            return;
        }

        if (game == null) {
            messageLabel.setText("Game not initialized.");
            return;
        }

        feedback fb = game.checkGuess(guess.toLowerCase());

        if (fb == null) {
            messageLabel.setText("Invalid guess or no attempts left.");
            return;
        }

        char[] results = fb.getResult();

        // Update grid
        for (int i = 0; i < wordLength; i++) {
            Label lbl = letterLabels[currentRow][i];
            char letter = guess.charAt(i);
            lbl.setText(String.valueOf(letter));

            switch (results[i]) {
                case 'G':
                    lbl.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-border-color: black; -fx-border-width: 2px;");
                    updateKeyColor(letter, 'G');
                    break;
                case 'Y':
                    lbl.setStyle("-fx-background-color: gold; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2px;");
                    updateKeyColor(letter, 'Y');
                    break;
                default:
                    lbl.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2px;");
                    updateKeyColor(letter, '-');
                    break;
            }
        }

        currentRow++;
        inputField.clear();

        if (game.isCorrectGuess(guess.toLowerCase())) {
            messageLabel.setText("Congratulations! You guessed the word.");
            inputField.setDisable(true);
            submitButton.setDisable(true);
        } else if (game.isGameOver()) {
            messageLabel.setText("Game over! The word was: " + game.getSecretWord().toUpperCase());
            inputField.setDisable(true);
            submitButton.setDisable(true);
        } else {
            messageLabel.setText("Attempts left: " + (maxAttempts - game.getCurrentAttempt()));
        }
    }

    private void updateKeyColor(char letter, char feedbackChar) {
        Button keyButton = keyboardButtons.get(letter);
        if (keyButton == null) return;

        // Priority order: Green > Yellow > Gray
        String currentStyle = keyButton.getStyle();

        if (feedbackChar == 'G') {
            // Green is highest priority, always override
            keyButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        } else if (feedbackChar == 'Y') {
            // Yellow if not already green
            if (!currentStyle.contains("green")) {
                keyButton.setStyle("-fx-background-color: gold; -fx-text-fill: black;");
            }
        } else {
            // Gray only if not green or yellow
            if (!currentStyle.contains("green") && !currentStyle.contains("gold")) {
                keyButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");
            }
        }
    }

    public void displayIntro() {
        messageLabel.setText("WORDLE.");
    }
}


