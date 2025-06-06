/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wordle;

/**
 *
 * @author azhan52
 */
 // Loads words from the specified file.
    // Each line in the file is treated as a separate word.
import java.io.*;
import java.util.*;

public class wordLoader {

    
    public String[] loadWords(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("holder file name"))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error reading file '" + filename + "': " + e.getMessage());
            // Return empty array if error occurs
            return new String[0];
        }
         return words.toArray(new String[0]);
        }
        
    }

   