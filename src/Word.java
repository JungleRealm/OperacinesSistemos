import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Stack;

public class Word {
    public static final int SIZE = 4; //bytes
    private final byte[] data;

    public Word() {
        data = new byte[SIZE];
    }

    public Word(Word src) {
        data = src.data.clone();
    }

    public byte getByte(int index) {
        return data[index];
    }

    public void setByte(int index, byte info) {
        data[index] = info;
    }

    @Override
    public Word clone() {
        return new Word(this);
    }

    byte[] getBytes() {
        return Arrays.copyOf(data, SIZE);
    }

    public static Word intToWord(int value) {
        ByteBuffer bb = ByteBuffer.allocateDirect(SIZE);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.clear();
        bb.putInt(value);
        Word word = new Word();
        for (int i = 0; i < SIZE; i++) {
            word.setByte(i, bb.get(i));
        }
        return word;
    }

    public static void print(Stack<Word> wordList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Word word : wordList) {
            int value = wordToInt(word);
            // Interpret the value based on your application's logic
            //String interpretedValue = interpretValue(value);
            //stringBuilder.append(interpretedValue).append(" ");
        }
        System.out.println("Output: " + stringBuilder.toString());
    }

    public static int wordToInt(Word word) {
        byte[] bytes = word.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }

    public static Stack<Word> reverseArray(Stack<Word> providedArray){
        Stack<Word> reversedArray = new Stack<>();

        while(!providedArray.empty()){
            reversedArray.push(providedArray.pop());
        }
        return reversedArray;
    }


}
