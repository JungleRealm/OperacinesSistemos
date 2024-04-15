import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Input {

    public static Stack<String> readFile(String filePath) throws IOException {
        Stack<String> wordList = new Stack<>();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                for (int i = 0; i < parts.length; i++){
                    wordList.add(parts[i]);
                }
            }
        }
        return wordList;
    }
}
