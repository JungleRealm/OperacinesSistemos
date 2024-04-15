import java.io.IOException;
import java.util.Stack;


//TODO Interrupt handling in RealMachine


public class RealMachine {

    Stack<String> readInput;


    public static Stack<String> read(String filePath) throws IOException {
        return Input.readFile(filePath);
    }

    public static void initializeVirtualMachine(){
        //TODO fill in initializeVirtualMachine method

        VirtualMachine virtualMachine = new VirtualMachine();
        ChannelDevice.moveFromSupervizorToVirtualMachineMemory(virtualMachine);
        virtualMachine.run();




    }

    public static Stack<Byte> convertToBytes(Stack<String> input){
        Stack<Byte> byteWords = new Stack<>();

        String word;
        while(!input.empty()){
            word = input.pop();
            if (word.length() == 0){
                System.out.println("Error reading word from file");
            } else if (word.length() == 1){
                byteWords.push((byte) 0);
                byteWords.push((byte) 0);
                byteWords.push((byte) 0);
                byteWords.push((byte) word.charAt(0));
            } else if(word.length() == 2){
                byteWords.push((byte) 0);
                byteWords.push((byte) 0);
                byteWords.push((byte) word.charAt(0));
                byteWords.push((byte) word.charAt(1));
            } else if (word.length() == 3){
                byteWords.push((byte) 0);
                byteWords.push((byte) word.charAt(0));
                byteWords.push((byte) word.charAt(1));
                byteWords.push((byte) word.charAt(2));
            } else if (word.length() == 4){
                byteWords.push((byte) word.charAt(0));
                byteWords.push((byte) word.charAt(1));
                byteWords.push((byte) word.charAt(2));
                byteWords.push((byte) word.charAt(3));
            } else {
                System.out.println("Word of length 5 or above found. Illegal argument");
                return null;
            }
        }

        return byteWords;
    }

    public static Stack<Byte> reverseStack(Stack<Byte> inputStack){
        Stack<Byte> reversedStack = new Stack<>();

        while(!inputStack.empty()){
            reversedStack.push(inputStack.pop());
        }
        return reversedStack;
    }

    public static Stack<Byte> orderStack(Stack<Byte> input){
        Stack<Byte> ordered = new Stack<>();
        byte element1;
        byte element2;
        byte element3;
        byte element4;
        while(!input.empty()){
            element4 = input.pop();
            element3 = input.pop();
            element2 = input.pop();
            element1 = input.pop();
            ordered.push(element1);
            ordered.push(element2);
            ordered.push(element3);
            ordered.push(element4);
        }
        return ordered;
    }

    public static void run(){
        //TODO fill in run method
    }






}
