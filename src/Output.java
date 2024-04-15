import java.util.Arrays;
import java.util.Stack;

// TODO in the last step, use this class to print out information to the screen

public class Output {
    private static byte[] printerMemory = new byte[RealMemory.getBlocks()*RealMemory.getWords()*RealMemory.getWordSize()];



    public static void print(byte[] printerMemory){
        for (int i = 0; i < printerMemory.length; i++){
            System.out.println(i + ": " + (char) printerMemory[i]);
        }
    }
}
