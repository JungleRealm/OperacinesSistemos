import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FlashDrive {

    private static String[] StringMemory = new String[16*16];
    private static byte[] memory = new byte[16*16*4];
    private static final int ptr = 0;
    private static int shift = 0;

    public static boolean load() throws IOException {
        int counter = 0;
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("E:\\new\\input.txt"))) {
            String line;
            while((line = bufferedReader.readLine()) != null){
                if (line == null){
                    break;
                }
                String[] parts = line.split("\\s+");
                int i = 0;
                while(i < parts.length){
                    StringMemory[counter] = parts[i];
                    i++;
                    counter++;
                }
            }

            System.out.println("Success reading input file");
            convertToBytes();
            return true;
        } catch (IOException exception){
            System.out.println("Unable to read input file");
            exception.printStackTrace();
        }
        return false;
    }

    // TODO delete this method
    public static void testPrint(){
        int i = 0;
        while(i < StringMemory.length && StringMemory[i] != null){
            System.out.println("Flash Drive Memory slot: " + i + " element found: " + StringMemory[i]);
            i++;
        }
    }

    // TODO delete this method
    public static void testBytePrint(){
        int i = ptr;
        while(i < memory.length){
            if (memory[i] == 0 && memory[i+1] == 0 && memory[i+2] == 0 && memory[i+3] == 0){
                break;
            }
            System.out.println("Flash Drive Memory slot: " + i + " element found: " + memory[i] + " char: " + (char) memory[i]);
            i++;
        }
    }

    public static void clearMemory(){
        StringMemory = new String[StringMemory.length];
    }

    public static byte getData(int index){
        return memory[index];
    }

    private static void resetShift(){
        shift = 0;
    }

    public static void convertToBytes(){
        resetShift();
        boolean errorFound = false;
        String word;
//        testPrint();
        for (int i = 0; i < StringMemory.length; i++){
            word = StringMemory[i];

            if (word == null){
                break;
            }

            if (word.length() == 0){
                System.out.println("Error reading a word from flash drive array");
                errorFound = true;
                break;
            } else if (word.length() == 1){
                memory[shift] = (byte) 0;
                memory[shift+1] = (byte) 0;
                memory[shift+2] = (byte) 0;
                memory[shift+3] = (byte) word.charAt(0);
                shift = shift + 4;
            } else if (word.length() == 2){
                memory[shift] = (byte) 0;
                memory[shift+1] = (byte) 0;
                memory[shift+2] = (byte) word.charAt(0);
                memory[shift+3] = (byte) word.charAt(1);
                shift = shift + 4;
            } else if (word.length() == 3){
                memory[shift] = (byte) 0;
                memory[shift+1] = (byte) word.charAt(0);
                memory[shift+2] = (byte) word.charAt(1);
                memory[shift+3] = (byte) word.charAt(2);
                shift = shift + 4;
            } else if (word.length() == 4){
                memory[shift] = (byte) word.charAt(0);
                memory[shift+1] = (byte) word.charAt(1);
                memory[shift+2] = (byte) word.charAt(2);
                memory[shift+3] = (byte) word.charAt(3);
                shift = shift + 4;
            } else {
                for (int j = 0; j < word.length(); j++){
                    memory[shift] = (byte) word.charAt(j);
                    shift++;
                }
            }
        }

        if (!errorFound){
            System.out.println("Flash Drive String -> Bytes conversion completed");
        } else {
            System.out.println("Error encountered. Conversion halted");
        }
    }

    public static int getPtr() {
        return ptr;
    }

    public static int getShift() {
        return shift;
    }
}
