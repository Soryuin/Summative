/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wordle;

/**
 *
 * @author azhan52
 */
import java.util.Arrays;

public class feedback {
    private String guess;
    private char[] result;  // 'G' = green, 'Y' = yellow, '-' = gray

    public feedback(String guess, char[] result) {
        this.guess = guess;
        this.result = result;
    }

    public char[] getResult() {
        return result;
    }
//Returns a string representation of the Feedback object.
    @Override
    public String toString() {
        return "Feedback{guess='" + guess + "', result=" + Arrays.toString(result) + "}";
    }
}
