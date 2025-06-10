/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wordle;

/**
 *
 * @author azhan52
 */

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class userInterface {

    private final int WORD_LENGTH;
    private final int MAX_ATTEMPTS;

    private Stage stage;
    private GridPane guessesGrid;
    private TextField inputField;
    private Button submitButton;
    private Label statusLabel;

    private int currentAttempt = 0;

    private String userInput = null;
    private final Object lock = new Object();

    public userInterface(int wordLength, int maxAttempts) {
        this.WORD_LENGTH = wordLength;
        this.MAX_ATTEMPTS = maxAttempts;
    }

    public void start(Stage primaryStage) {
        stage = primaryStage;
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20;");

        statusLabel = new Label("Welcome! Guess the " + WORD_LENGTH + "-letter word.");
        statusLabel.setFont(Font.font("Arial", 20));

        guessesGrid = new GridPane();
        guessesGrid.setHgap(5);
        guessesGrid.setVgap(5);
        guessesGrid.setAlignment(Pos.CENTER);

        // Initialize grid with empty labels
        for (int row = 0; row < MAX_ATTEMPTS; row++) {
            for (int col = 0; col < WORD_LENGTH; col++) {
                Label label = createLetterLabel(" ");
                guessesGrid.add(label, col, row);
            }
        }

        inputField = new TextField();
        inputField.setFont(Font.font(18));
        inputField.setPrefWidth(200);
        inputField.setAlignment(Pos.CENTER);

        submitButton = new Button("Submit");
        submitButton.setDefaultButton(true);

        HBox inputBox = new HBox(10, inputField, submitButton);
        inputBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(statusLabel, guessesGrid, inputBox);

        submitButton.setOnAction(e -> onSubmit());
        inputField.setOnAction(e -> onSubmit());

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Wordle JavaFX");
        stage.show();

        inputField.requestFocus();
    }

    private Label createLetterLabel(String letter) {
        Label label = new Label(letter);
        label.setPrefSize(40, 40);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("Monospaced", 24));
        label.setStyle("-fx-border-color: gray; -fx-border-width: 2; -fx-background-color: lightgray;");
        return label;
    }

    private void onSubmit() {
        String guess = inputField.getText().trim().toUpperCase();
        if (guess.length() != WORD_LENGTH) {
            showAlert("Invalid input", "Please enter a " + WORD_LENGTH + "-letter word.");
            return;
        }
        synchronized (lock) {
            userInput = guess;
            lock.notify();
        }
        inputField.clear();
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle(title);
            alert.showAndWait();
        });
    }

   
    public String getUserInput() {
        synchronized (lock) {
            userInput = null;
            try {
                while (userInput == null) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return userInput;
        }
    }

    /**
     * Shows the guess with color feedback: green (correct pos), yellow (wrong pos), gray (absent).
     */
    public void displayFeedback(feedback fb) {
       Platform.runLater(() -> {
        char[] result = fb.getResult();
        String guess = fb.getGuess();

        for (int i = 0; i < WORD_LENGTH; i++) {
            Label label = (Label) getNodeFromGridPane(guessesGrid, i, currentAttempt);
            label.setText(String.valueOf(guess.charAt(i)));

            switch (result[i]) {
                case 'G':
                    label.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-border-color: gray; -fx-border-width: 2;");
                    break;
                case 'Y':
                    label.setStyle("-fx-background-color: yellow; -fx-text-fill: black; -fx-border-color: gray; -fx-border-width: 2;");
                    break;
                default:
                    label.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-border-color: gray; -fx-border-width: 2;");
                    break;
            }
        }
        currentAttempt++;
    });
}

    private javafx.scene.Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public void displayIntro() {
        Platform.runLater(() -> statusLabel.setText("Welcome! Guess the " + WORD_LENGTH + "-letter word."));
    }

    public void displayWin() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Congratulations! You guessed the word!", ButtonType.OK);
            alert.setTitle("You Win!");
            alert.setHeaderText(null);
            alert.showAndWait();
            statusLabel.setText("You Win!");
            inputField.setDisable(true);
            submitButton.setDisable(true);
        });
    }

    public void displayLose(String correctWord) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Game Over! The correct word was: " + correctWord, ButtonType.OK);
            alert.setTitle("You Lose!");
            alert.setHeaderText(null);
            alert.showAndWait();
            statusLabel.setText("Game Over! Word was: " + correctWord);
            inputField.setDisable(true);
            submitButton.setDisable(true);
        });
    }
}
