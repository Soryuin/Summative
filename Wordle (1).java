package wordle;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class for the Wordle Game.
 */
public class Wordle extends Application {

    private static final int MAX_ATTEMPTS = 6;  // Maximum number of attempts

    @Override
    public void start(Stage primaryStage) {
        // Load words from file
        wordLoader loader = new wordLoader();
        String[] words = loader.loadWords("words.txt");
        
        if (words.length == 0) {
            System.err.println("No words loaded. Exiting.");
            // Gracefully exit if no words are loaded
            System.exit(1);
        }

        // Use first word length for the game word length (assuming all words are of the same length)
        int wordLength = words[0].length();

        // Initialize user interface
        userInterface ui = new userInterface(wordLength, MAX_ATTEMPTS);
        ui.start(primaryStage);

        // Initialize the game logic
        wordleGame game = new wordleGame(words, MAX_ATTEMPTS);
        game.startGame();

        ui.displayIntro();  // Display introduction to the user

        // Run game loop on a separate thread so UI thread isn't blocked
        new Thread(() -> {
            while (!game.isGameOver()) {
                String guess = ui.getUserInput();  // Get the user's guess

                // Check the guess and get feedback
                feedback fb = game.checkGuess(guess);
                if (fb == null) {
                    // This should not happen because input length is checked in UI
                    continue;
                }

                // Display feedback for the guess
                ui.displayFeedback(fb);

                if (game.isCorrectGuess(guess)) {
                    ui.displayWin();  // Display win message
                    return;
                }
            }

            // Game over without correct guess
            ui.displayLose(game.getSecretWord());
        }).start();
    }

    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}
