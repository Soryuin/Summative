/*
 * Wordle game logic class
 * Handles guess checking, game progress, and feedback generation.
 */
package wordle;

import java.util.Random;

/**
 *
 * 
 * Author: azhan52
 */
public class wordleGame {
    private String[] wordList;       // List of valid words to choose from
    private String secretWord;       // The word to guess
    private int maxAttempts;         // Maximum number of allowed guesses
    private int currentAttempt;      // Current guess count
    private String[] guesses;        // Array to store all guesses

    /**
     * Constructor to initialize the game with a word list and maximum attempts.
     *
     * @param wordList List of possible secret words
     * @param maxAttempts Maximum number of guesses allowed
     */
    public wordleGame(String[] wordList, int maxAttempts) {
        this.wordList = wordList;
        this.maxAttempts = maxAttempts;
        this.currentAttempt = 0;
        this.guesses = new String[maxAttempts];
    }

    /**
     * Starts a new game by randomly selecting a secret word
     * and resetting the attempt counter and guess list.
     */
    public void startGame() {
        Random random = new Random();
        secretWord = wordList[random.nextInt(wordList.length)];
        currentAttempt = 0;
        guesses = new String[maxAttempts];
    }

    /**
     * Checks the player's guess against the secret word and returns feedback.
     * 'G' = correct letter in correct place (green)
     * 'Y' = correct letter in wrong place (yellow)
     * '-' = incorrect letter (gray)
     */
    public feedback checkGuess(String guess) {
        if (guess == null || guess.length() != secretWord.length()) {
            return null; // Invalid guess length
        }

        if (currentAttempt >= maxAttempts) {
            return null; // No attempts left
        }

        char[] result = new char[secretWord.length()];
        boolean[] secretUsed = new boolean[secretWord.length()];

        // First pass: check for correct letters in correct positions (green)
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == secretWord.charAt(i)) {
                result[i] = 'G';
                secretUsed[i] = true;
            } else {
                result[i] = '-';
            }
        }

        // Second pass: check for correct letters in wrong positions (yellow)
        for (int i = 0; i < guess.length(); i++) {
            if (result[i] == 'G') continue;
            for (int j = 0; j < secretWord.length(); j++) {
                if (!secretUsed[j] && guess.charAt(i) == secretWord.charAt(j)) {
                    result[i] = 'Y';
                    secretUsed[j] = true;
                    break;
                }
            }
        }

        guesses[currentAttempt++] = guess;
        return new feedback(guess, result);
    }

    /**
     * Checks if the player's guess matches the secret word.
     */
    public boolean isCorrectGuess(String guess) {
        return guess != null && guess.equals(secretWord);
    }

    /**
     * Checks whether the game is over based on the number of attempts.
     */
    public boolean isGameOver() {
        return currentAttempt >= maxAttempts;
    }

    /**
     * Returns the secret word.
     *
     * @return The secret word as a String
     */
    public String getSecretWord() {
        return secretWord;
    }

    /**
     * Returns the number of guesses made so far.
     */
    public int getCurrentAttempt() {
        return currentAttempt;
    }

    /**
     * Returns the maximum number of guesses allowed.
     */
    public int getMaxAttempts() {
        return maxAttempts;
    }
}
