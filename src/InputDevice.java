package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import javax.swing.JTextField;

public class InputDevice {

    // Change path for testing purposes
    String filePath;

    InputDevice(String filePath){
        this.filePath = filePath;
    }


    public ArrayList<String> getInput(){
        try (BufferedReader reader = new BufferedReader(new FileReader(this.filePath))){

            String line;
            ArrayList<String> commands = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                // Split the line into words using space as delimiter
                String[] words = line.split(" ");
                //System.out.println(line);

                // Iterate through the words
                for (String word : words) {
                    // Add the word to the list
                    commands.add(word);

                    // If the word is "HALT", stop reading
                    if (word.equals("HALT")) {
                        return commands;
                    }
                }

                // If "HALT" is found, break from the outer loop as well
                if (commands.contains("HALT")) {
                    return commands;
                }
            }
            return commands;
        }
        catch(IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}