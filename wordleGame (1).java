/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wordle;

/**
 *
 * @author azhan52
 */
import java.util.Random;

public class wordleGame {

    private String[] wordList;
    private String secretWord;
    private final int maxAttempts;
    private int currentAttempt;
    private String[] guesses;

    public wordleGame(String[] wordList, int maxAttempts) {
        this.wordList = wordList;
        this.maxAttempts = maxAttempts;
        this.currentAttempt = 0;
        this.guesses = new String[maxAttempts];
    }

    public void startGame() {
        Random random = new Random();
        secretWord = wordList[random.nextInt(wordList.length)];
        currentAttempt = 0;
        guesses = new String[maxAttempts];
    }

    /**
     * Checks the user's guess and returns feedback. If guess is invalid or
     * attempts are exhausted, returns null.
     */
    public feedback checkGuess(String guess) {
        if (guess == null || guess.length() != secretWord.length()) {
            return null; // Invalid guess length
        }

        // Reject if guess contains non-letter characters (e.g., numbers or symbols)
        if (!guess.matches("[a-zA-Z]+")) {
            return null; // Invalid characters in guess
        }

        if (currentAttempt >= maxAttempts) {
            return null; // No attempts left
        }

        char[] result = new char[secretWord.length()];
        boolean[] secretUsed = new boolean[secretWord.length()];
        boolean[] guessUsed = new boolean[secretWord.length()];

        // First pass: check for correct letter in correct position (green)
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == secretWord.charAt(i)) {
                result[i] = 'G';
                secretUsed[i] = true;
                guessUsed[i] = true;
            } else {
                result[i] = '-';
            }
        }

        // Second pass: check for correct letter in wrong position (yellow)
        for (int i = 0; i < guess.length(); i++) {
            if (result[i] == 'G') {
                continue;
            }

            for (int j = 0; j < secretWord.length(); j++) {
                if (!secretUsed[j] && !guessUsed[i] && guess.charAt(i) == secretWord.charAt(j)) {
                    result[i] = 'Y';
                    secretUsed[j] = true;
                    guessUsed[i] = true;
                    break;
                }
            }
        }

        guesses[currentAttempt++] = guess;
        return new feedback(guess, result);

    }

    public boolean isCorrectGuess(String guess) {
        return guess != null && guess.equals(secretWord);
    }

    public boolean isGameOver() {
        return currentAttempt >= maxAttempts;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public int getCurrentAttempt() {
        return currentAttempt;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }
}
