import java.util.Arrays;
import java.util.Stack;

// TODO in the last step, use this class to print out information to the screen

public class Output {
    private static Stack<Byte> printerMemory = new Stack<>();
    private static Stack<Character> characterStack = new Stack<>();

    public static void addToStack(byte input){
//        System.out.println(input + " was added to printer memory");
        printerMemory.push(input);
    }


    public static void print(){
        convertStackToCharacter();
        reverseCharacterStack();
        System.out.println(characterStack);
        printerMemory.clear();
        characterStack.clear();
    }

    public static void convertStackToCharacter(){
        while(!printerMemory.empty()){
            if (printerMemory.peek() != 0){
                characterStack.push((char) (byte) printerMemory.pop());
            } else {
                printerMemory.pop();
            }

        }
    }

    public static void reverseCharacterStack(){
        Stack<Character> temp = new Stack<>();
        while(!characterStack.empty()){
            temp.push(characterStack.pop());
        }
        characterStack = temp;
    }

}
