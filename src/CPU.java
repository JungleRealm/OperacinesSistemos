import java.util.Stack;

public class CPU {

    private MemoryManagementUnit memoryManagementUnit;
    private Output output;


    private int MODE;
    private int PTR; //Pointer register
    private int PC; //Komandos skaitliukas. It keeps track of the memory address of the next instruction to be fetched and executed
    private int TI; //Taimeris. Kas nurodyta laika generuoja interuptus
    private int SP; //Steko virsunes indeksas. It points to the top of the stack in memory.
    private int Interrupt; //It represents the program interrupt type. It can indicate errors or exceptional conditions encountered during program execution, such as invalid memory access or undefined instructions.

    private static final int TIME = 20;
    public static final int SUPERVISOR = 0;

    public CPU(MemoryManagementUnit memoryManagementUnit, Output output) {
        this.memoryManagementUnit = memoryManagementUnit;
        this.output = output;

        MODE = SUPERVISOR; //Veikimo rezimas
        PC = 0; //Komandos skaitliukas
        SP = 1000; //Steko virsunes indeksas
        TI = TIME; //Laiko sekimo registras
        Interrupt = 0; //Source index ??????
    }

    public void resetInterrupts(){
        resetTI();
        Interrupt = 0;
    }

    public void run() throws MemoryOutOfBoundsException {
        Word instruction;
        int command;
        int operand2;
        int operand1;
        Word wordOperand1;
        Word wordOperand2;
        int result;

        //while (!(instruction == null)) {
            for(int i = 0; (i < memoryManagementUnit.getArraySize()) && Interrupt==0; i++){
            //Word instruction = instruction.pop();
            instruction = fetchInstructions();
            command = Word.wordToInt(instruction);


            switch (interpretValue(command)) {
                case "PUSH":
//                    System.out.println("PUSH reached");

                    try {
                        Word wordParameter = fetchInstructions();
                        if (wordParameter == null) {
                            System.out.println("Missing parameter for PUSH instruction");
                            getInterrupt(2);
                            return;
                        }
                        memoryManagementUnit.getRealMemory().write(SP, Word.wordToInt(wordParameter));
                        System.out.println("SP: " + SP + " Data: " + Word.wordToInt(wordParameter));
                        SP++;
                        i++;

                    } catch (NumberFormatException e) {
                        //System.out.println("Invalid parameter for PUSH instruction");
                        getInterrupt(2);
                        return;
                    }
                    break;

                case "ADD":
                    System.out.println("ADD reached");

                    SP--;
                    wordOperand2 = memoryManagementUnit.getRealMemory().pop(this.SP);

                    SP--;
                    wordOperand1 = memoryManagementUnit.getRealMemory().pop(this.SP);

                    if (wordOperand1 == null || wordOperand2 == null) {
                        //System.out.println("Error: Not enough operands for ADD instruction");
                        getInterrupt(2);
                        return;
                    } else {
                        operand2 = Word.wordToInt(wordOperand2);
                        operand1 = Word.wordToInt(wordOperand1);
                        result = operand1 + operand2;

                        memoryManagementUnit.getRealMemory().write(this.SP, result);
                        SP++;

                        System.out.println(operand1 + " + " + operand2 + " = " + result);

                    }
                    break;

                case "MUL":
                    System.out.println("MUL reached");

                    SP--;
                    wordOperand2 = memoryManagementUnit.getRealMemory().pop(this.SP);

                    SP--;
                    wordOperand1 = memoryManagementUnit.getRealMemory().pop(this.SP);

                    if (wordOperand1 == null || wordOperand2 == null) {
                        //System.out.println("Error: Not enough operands for MUL instruction");
                        getInterrupt(2);
                        return;
                    } else {
                        operand2 = Word.wordToInt(wordOperand2);
                        operand1 = Word.wordToInt(wordOperand1);
                        result = operand1 * operand2;

                        memoryManagementUnit.getRealMemory().write(this.SP, result);
                        SP++;

                        System.out.println(operand1 + " * " + operand2 + " = " + result);
                    }
                    break;

                case "POP":
                    System.out.println("POP reached");
                    SP--;
                    if (memoryManagementUnit.getRealMemory().read(this.SP) == null){
                        //System.out.println("Nothing to POP, stack is empty");
                        getInterrupt(2);
                        return;
                    } else {

                        memoryManagementUnit.getRealMemory().pop(this.SP);
                    }
                    break;

                case "SUB":
                    System.out.println("SUB reached");

                    SP--;
                    wordOperand2 = memoryManagementUnit.getRealMemory().pop(this.SP);

                    SP--;
                    wordOperand1 = memoryManagementUnit.getRealMemory().pop(this.SP);

                    if (wordOperand1 == null || wordOperand2 == null) {
                        System.out.println("Error: Not enough operands for SUB instruction");
                    } else {
                        operand2 = Word.wordToInt(wordOperand2);
                        operand1 = Word.wordToInt(wordOperand1);
                        result = operand1 - operand2;

                        memoryManagementUnit.getRealMemory().write(this.SP, result);
                        SP++;

                        System.out.println(operand1 + " - " + operand2 + " = " + result);
                    }
                    break;

                case "DIV":
                    System.out.println("DIV reached");

                    SP--;
                    wordOperand2 = memoryManagementUnit.getRealMemory().pop(this.SP);

                    SP--;
                    wordOperand1 = memoryManagementUnit.getRealMemory().pop(this.SP);

                    if (wordOperand1 == null || wordOperand2 == null) {
                        //System.out.println("Error: Not enough operands for DIV instruction");
                        getInterrupt(2);
                        return;
                    } else {
                        operand2 = Word.wordToInt(wordOperand2);
                        operand1 = Word.wordToInt(wordOperand1);
                        result = operand1 / operand2;

                        memoryManagementUnit.getRealMemory().write(this.SP, result);
                        SP++;

                        System.out.println(operand1 + " / " + operand2 + " = " + result);
                    }
                    break;

                case "JB":
                    System.out.println("JB reached");
                    break;

                case "JA":
                    System.out.println("JA reached");
                    break;

                case "JZ":
                    System.out.println("JZ reached");
                    break;

                case "GETD":
                    System.out.println("GETD reached");
                    break;

                case "PUTD":
                    System.out.println("PUTD reached");
                    break;

                case "PRTN":
                    System.out.println("PRTN reached");
                    SP--;
                    wordOperand1 = memoryManagementUnit.getRealMemory().pop(SP);
                    if (wordOperand1 == null){
                        getInterrupt(2);
                        return;
                    }
                    operand1 = Word.wordToInt(wordOperand1);
                    output.add(Integer.toString(operand1));
                    break;

                case "PRTS":
                    System.out.println("PRTS reached");
                    break;

                case "HALT": //HALT
                    System.out.println("HALT reached");
                    output.print();
                    getInterrupt(5);
                    return;

                default:
                    System.out.println("Default reached");
                    //System.out.println("Error: Unknown instruction: " + instruction + " Instruction integer value: " + Word.wordToInt(instruction));
                    getInterrupt(4);
                    return;
            }

            TI--;
            if(checkTimeInterrupt()){
                this.Interrupt = 1;
                return;
            }

        }

    }

    private Word fetchInstructions() throws MemoryOutOfBoundsException {
        Word instruction;

        if (this.PC <= memoryManagementUnit.getMaxCommandSize()* memoryManagementUnit.getMemoryPointer()) {

            instruction = memoryManagementUnit.getRealMemory().pop(this.getPC());
            System.out.println("Fetched element: " + Word.wordToInt(instruction));
            this.setPC(this.getPC()+memoryManagementUnit.getMaxCommandSize());
            return instruction;

        } else {
            getInterrupt(3);
            return null;
        }
    }

    public static String interpretValue(int value) {
        // Assuming each value represents a command or operand
        switch (value) {
            case 1001:
                return "PUSH";
            case 1002:
                return "ADD";
            case 1003:
                return "MUL";
            case 1004:
                return "PRTN";
            case 1005:
                return "HALT";
            case 1006:
                return "POP";
            case 1007:
                return "SUB";
            case 1008:
                return "DIV";
            case 1009:
                return "JB";
            case 1010:
                return "JA";
            case 1011:
                return "JZ";
            case 1012:
                return "GETD";
            case 1013:
                return "PUTD";
            case 1014:
                return "PRTS";
            default:
                return String.valueOf(value); // Default interpretation if no command matches
        }
    }

    public int getInterrupt(int interrupt){
        switch (interrupt){
            case 1: System.out.println("Time interrupt occurred"); return 1; // Time interrupt
            case 2: System.out.println("Missing parameters interrupt occurred"); return 2; // Missing parameters interrupt
            case 3: System.out.println("No instruction found in memory interrupt occurred"); return 3; // Time interrupt
            case 4: System.out.println("Undefined instruction found interrupt occurred"); return 4;
            case 5: System.out.println("HALT reached. HALT interrupt"); return 5;
            case 6: System.out.println("HALT reached. HALT interrupt"); return 6;

        }
        return 0;
    }

    public void resetTI() {
        this.TI = TIME;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public int getPTR() {
        return PTR;
    }

    public void setPTR(int PTR) {
        this.PTR = PTR;
    }

    public int getSP() {
        return SP;
    }

    public void setSP(int SP) {
        this.SP = SP;
    }

    public int getTI() {
        return TI;
    }

    public void setTI(int TI) {
        this.TI = TI;
    }

    public int getMODE() {
        return MODE;
    }

    public boolean checkTimeInterrupt(){
        return this.TI <= 0;
    }

}
