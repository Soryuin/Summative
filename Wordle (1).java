package wordle;

/**
 * Main class for the Wordle Game.
 */
import javafx.application.Application;
import javafx.stage.Stage;

public class Wordle extends Application {

    private static final int MAX_ATTEMPTS = 6;

    @Override
    public void start(Stage primaryStage) {
        // Load words from file using wordLoader class
        wordLoader loader = new wordLoader();
        String[] words = loader.loadWords("words.txt");

        if (words.length == 0) {
            System.err.println("No words loaded. Exiting.");
            System.exit(1);
        }

        int wordLength = words[0].length();

        // Initialize game logic
        wordleGame game = new wordleGame(words, MAX_ATTEMPTS);
        game.startGame();

        // Initialize UI with word length and attempts
        userInterface ui = new userInterface(wordLength, MAX_ATTEMPTS);
        ui.setGame(game);            // Set game reference in UI
        ui.start(primaryStage);      // Start UI stage
        ui.displayIntro();           // Show intro text
    }

    public static void main(String[] args) {
        launch(args);
    }
}

