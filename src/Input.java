import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Stack;

public class Input {

    public static Stack<Word> readFile(String filePath) throws IOException {
        Stack<Word> wordList = new Stack<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                for (String part : parts) {
                    wordList.add(convertToWord(part));
                }
            }
        }
        return wordList;
    }

    private static Word convertToWord(String part) {
        Word word = new Word();
        int value;
        try {
            value = Integer.parseInt(part);
//            System.out.println("Value found: " + value);
        } catch (NumberFormatException e) {
            // If parsing as integer fails, it must be a command like "PUSH"
            value = convertCommandToValue(part);
        }
        ByteBuffer buffer = ByteBuffer.allocate(Word.SIZE).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);
        byte[] bytes = buffer.array();
        for (int i = 0; i < Word.SIZE; i++) {
            word.setByte(i, bytes[i]);
        }
        return word;
    }

    private static int convertCommandToValue(String command) {
        // You can add more commands here if needed
        int i;
        switch (command) {
            case "PUSH":
//                System.out.println("PUSH found");
                i = 1001;
                return i;
            case "ADD":
//                System.out.println("ADD found");
                i = 1002;
                return i;
            case "MUL":
//                System.out.println("MUL found");
                i = 1003;
                return i;
            case "PRTN":
//                System.out.println("PRTN found");
                i = 1004;
                return i;
            case "HALT":
//                System.out.println("HALT found");
                i = 1005;
                return i;
            case "POP":
//                System.out.println("POP found");
                i = 1006;
                return i;
            case "SUB":
//                System.out.println("SUB found");
                i = 1007;
                return i;
            case "DIV":
//                System.out.println("DIV found");
                i = 1008;
                return i;
            case "JB":
//                System.out.println("JB found");
                i = 1009;
                return i;
            case "JA":
//                System.out.println("JA found");
                i = 1010;
                return i;
            case "JZ":
//                System.out.println("JZ found");
                i = 1011;
                return i;
            case "GETD":
//                System.out.println("GETD found");
                i = 1012;
                return i;
            case "PUTD":
//                System.out.println("PUTD found");
                i = 1013;
                return i;
            case "PRTS":
                i = 1014;
                return i;
            default:
                throw new IllegalArgumentException("Unknown command: " + command);
        }
    }
}
