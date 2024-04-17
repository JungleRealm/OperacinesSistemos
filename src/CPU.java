import java.util.Stack;

public class CPU {

    public static final int SUPERVISOR = 0;
    public static final int User = 1;

    private static int MODE = SUPERVISOR; // User or Supervizor
    private static int PTR; //Pointer register
    private static int PC; //Komandos skaitliukas. It keeps track of the memory address of the next instruction to be fetched and executed
    private static int TI = 20; //Taimeris. Kas nurodyta laika generuoja interuptus
    private static int SP = Supervizor.getFullSupervizorMemory(); //Steko virsunes indeksas. It points to the top of the stack in memory.

    private static int SI = 0;
    private static int PI = 0;

    //TODO create a method that checks interrupts and returns them to RealMachine for handling

    public static void resetTI(){
        TI = 20;
    }

    public static void resetInterrupts(){
        SI = 0;
        PI = 0;
        resetTI();
    }

    public static void run(VirtualMachine virtualMachine){
//        System.out.println(pointerToResultStack);
        String command;
        command = RealMemory.getCommand(virtualMachine);
//        virtualMachine.printAllVMMemory();
//        System.out.println("i = " + i + "; " + " Command found = " + command + " PointerToEnd = " + pointerToEnd);
        if (command != null){

            interpretCommand(command, virtualMachine);
//            virtualMachine.testDataPrint();
//            System.out.println("-----------------");

        } else {
            System.out.println("Illegal Argument encountered when fetching command");
            return;
        }


        TI--;
        getInterrupt();
    }

    //TODO finish interpret command method to do what the command should

    public static void interpretCommand(String command, VirtualMachine virtualMachine){
        String operand2;
        String operand1;
        Stack<String> temp = new Stack<>();
        Stack<Byte> result = new Stack<>();

        byte character1;
        byte character2;
        byte character3;
        byte character4;
        byte character5;
        byte character6;
        byte character7;
        byte character8;


        switch (command){
            case "PUSH":
                // DONE
                System.out.println("PUSH");

                for (int i = 0; i < RealMemory.getWordSize(); i++){
                    virtualMachine.writeToResultStackMemory(virtualMachine.getPointerToResultStackEnd(), virtualMachine.popElementFromVirtualMachineMemory());
                }
                break;

            case "POP":
                // DONE
                System.out.println("POP");
                for (int i = 0; i < RealMemory.getWordSize(); i++){
                    virtualMachine.popElementFromResultStack();
                }
                break;

            case "ADD":
                System.out.println("ADD");

                character1 = virtualMachine.popElementFromResultStack();
                character2 = virtualMachine.popElementFromResultStack();
                character3 = virtualMachine.popElementFromResultStack();
                character4 = virtualMachine.popElementFromResultStack();

                operand2 = RealMemory.cropWord(character4, character3, character2, character1);

                character5 = virtualMachine.popElementFromResultStack();
                character6 = virtualMachine.popElementFromResultStack();
                character7 = virtualMachine.popElementFromResultStack();
                character8 = virtualMachine.popElementFromResultStack();

                operand1 = RealMemory.cropWord(character8, character7, character6, character5);

                temp.push(Integer.toString (RealMachine.convertCommandToInteger(operand2) + RealMachine.convertCommandToInteger(operand1)));
                result = RealMachine.reverseStack(RealMachine.convertToBytes(temp));
                for (int i = 0; i < RealMemory.getWordSize(); i++){
                    virtualMachine.writeToResultStackMemory(virtualMachine.getPointerToResultStackEnd(), result.pop());
                }

                break;

            case "SUB":
                System.out.println("SUB");

                character1 = virtualMachine.popElementFromResultStack();
                character2 = virtualMachine.popElementFromResultStack();
                character3 = virtualMachine.popElementFromResultStack();
                character4 = virtualMachine.popElementFromResultStack();

                operand2 = RealMemory.cropWord(character4, character3, character2, character1);

                character5 = virtualMachine.popElementFromResultStack();
                character6 = virtualMachine.popElementFromResultStack();
                character7 = virtualMachine.popElementFromResultStack();
                character8 = virtualMachine.popElementFromResultStack();

                operand1 = RealMemory.cropWord(character8, character7, character6, character5);

                temp.push(Integer.toString (RealMachine.convertCommandToInteger(operand2) - RealMachine.convertCommandToInteger(operand1)));

//                System.out.println(temp);
                result = RealMachine.reverseStack(RealMachine.convertToBytes(temp));
//                System.out.println(result);
                for (int i = 0; i < RealMemory.getWordSize(); i++){
                    virtualMachine.writeToResultStackMemory(virtualMachine.getPointerToResultStackEnd(), result.pop());
                }
                break;

            case "MUL":
                System.out.println("MUL");
                character1 = virtualMachine.popElementFromResultStack();
                character2 = virtualMachine.popElementFromResultStack();
                character3 = virtualMachine.popElementFromResultStack();
                character4 = virtualMachine.popElementFromResultStack();

                operand2 = RealMemory.cropWord(character4, character3, character2, character1);

                character5 = virtualMachine.popElementFromResultStack();
                character6 = virtualMachine.popElementFromResultStack();
                character7 = virtualMachine.popElementFromResultStack();
                character8 = virtualMachine.popElementFromResultStack();

                operand1 = RealMemory.cropWord(character8, character7, character6, character5);

                temp.push(Integer.toString (RealMachine.convertCommandToInteger(operand2) * RealMachine.convertCommandToInteger(operand1)));
//                System.out.println(temp);
                result = RealMachine.reverseStack(RealMachine.convertToBytes(temp));
//                System.out.println(result);
                for (int i = 0; i < RealMemory.getWordSize(); i++){
                    virtualMachine.writeToResultStackMemory(virtualMachine.getPointerToResultStackEnd(), result.pop());
                }

                break;

            case "DIV":
                System.out.println("DIV");

                character1 = virtualMachine.popElementFromResultStack();
                character2 = virtualMachine.popElementFromResultStack();
                character3 = virtualMachine.popElementFromResultStack();
                character4 = virtualMachine.popElementFromResultStack();

                operand2 = RealMemory.cropWord(character4, character3, character2, character1);

                character5 = virtualMachine.popElementFromResultStack();
                character6 = virtualMachine.popElementFromResultStack();
                character7 = virtualMachine.popElementFromResultStack();
                character8 = virtualMachine.popElementFromResultStack();

                operand1 = RealMemory.cropWord(character8, character7, character6, character5);

                temp.push(Integer.toString (RealMachine.convertCommandToInteger(operand2) / RealMachine.convertCommandToInteger(operand1)));
                result = RealMachine.reverseStack(RealMachine.convertToBytes(temp));
                for (int i = 0; i < RealMemory.getWordSize(); i++){
                    virtualMachine.writeToResultStackMemory(virtualMachine.getPointerToResultStackEnd(), result.pop());
                }
                break;

            case "JB":
                System.out.println("JB");
                break;

            case "JA":
                System.out.println("JA");
                break;

            case "JZ":
                System.out.println("JZ");
                break;

            case "PUTD":
                System.out.println("PUTD");
                break;

            case "GETD":
                System.out.println("GETD");
                break;

            case "PRTN":
                System.out.println("PRTN");

                for (int i = virtualMachine.getPointerToResultStackEnd(); i > virtualMachine.getPointerToResultStackStart(); i--){
                    result.push(virtualMachine.popElementFromResultStack());
                }
                while(!result.empty()){
                    ChannelDevice.moveFromVirtualMachineMemoryToPrinter(result.pop());
                }
                break;

            case "FORK":
                System.out.println("FORK");
                //virtualMachine.decrementStackPointerByWordSize();
                virtualMachine.incrementStackPointerByWordSize();
                virtualMachine.fork();
                // TODO fork wont work if there are no things to copy. Create an interrupt


                break;

            case "HALT":
                System.out.println("HALT");
                Output.print();
                break;

            default:
                System.out.println("Non command object found: " + command);
        }
    }

    public static int getInterrupt(){
        switch (PI){
            case 1: return 1; // Incorrect address
            case 2: return 2; // Illegal command
            case 3: return 3; // Non enough space in external disk
        }
        switch (SI){
            case 1: return 4; // GETD
            case 2: return 5; // PUTD
            case 3: return 6; // HALT
            case 4: return 8; // Swap failed
        }
        if (TI == 0){
            return 7;
        }
        return 0;
    }

    public static int getPI() {
        return PI;
    }

    public static int getSI() {
        return SI;
    }

    public static int getSP() {
        return SP;
    }
}
