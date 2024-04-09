import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InputDevice {

    // Change path for testing purposes
    String filePath = "C:\\Projects\\OSMain\\files\\Test1.txt";


    public Word[] getInput(){
        try (BufferedReader reader = new BufferedReader(new FileReader(this.filePath))){

            String line;
            byte[] bytes;
            Word[] words;

            while ((line = reader.readLine()) != null) {
                bytes = line.getBytes();

                if(bytes.length%4 != 0) {
                    words = new Word[(bytes.length/4)+1];
                }
                else words = new Word[bytes.length/4];
                for(int i = 0; i < words.length; i++) {
                    words[i] = new Word();
                }
                int i = 3, j = 0;
                for(byte b : bytes) {
                    if (i >= 0) {
                        words[j].setByte(i, b);
                        i--;
                    }
                    else {
                        i = 3;
                        j++;
                        words[j].setByte(i, b);
                    }
                }
                return words;
            }
        }
        catch(IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}