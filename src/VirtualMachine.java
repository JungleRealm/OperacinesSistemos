public class VirtualMachine {

    private static final int virtualMachineBlocks = 16;
    private static final int virtualMachineWords = 16;
    private static final int virtualMachineWordSize = 4;
    private static final int size = virtualMachineBlocks * virtualMachineWords * virtualMachineWordSize;

    private final int ptr;
    private int stackPointer;

    private int resultPtr;
    private int resultStackPointer;

    public VirtualMachine(int ptr){
        this.ptr = ptr;
        stackPointer = this.ptr;
    }

    public void exec(){
        String command;
        while (true){
            if (RealMachine.getStepping()){
                RealMachine.userInput();
            }
            command = getCommand();
            System.out.println("Command read = " + command);
            if (stackPointer >= ptr){
                interpretCommand(command);
            } else {
                System.out.println("Finished executing commands");
                return;
            }

            if (RealMachine.getSI() + RealMachine.getPI() != 0 || RealMachine.getTI() == 0) {
                RealMachine.interruptHandler();
                if (RealMachine.getStopRunning()){
                    return;
                }
            }
        }
    }

    public String getCommand(){
        StringBuilder commandCharacters = new StringBuilder();
        for (int i = 0; i < virtualMachineWordSize; i++){
            commandCharacters.append((char) pop());
        }
        return (commandCharacters.toString()).trim();
    }

    public void interpretCommand(String command){
        byte character1;
        byte character2;
        byte character3;
        byte character4;
        byte character5;
        byte character6;
        byte character7;
        byte character8;
        int operand1;
        int operand2;
        int result;
        byte[] resultBytes;
        switch (command){
            case "$STR":
                System.out.println("$STR reached");
                break;
            case "PUSH":
                System.out.println("PUSH reached");

                character1 = pop();
                character2 = pop();
                character3 = pop();
                character4 = pop();
                pushToStack(character4);
                pushToStack(character3);
                pushToStack(character2);
                pushToStack(character1);

                if (RealMachine.getStepping()){
                    System.out.println("Character " + (char) character4 + " pushed to stack");
                    System.out.println("Character " + (char) character3 + " pushed to stack");
                    System.out.println("Character " + (char) character2 + " pushed to stack");
                    System.out.println("Character " + (char) character1 + " pushed to stack");
                }
                break;

            case "POP":
                System.out.println("POP reached");

                for (int i = 0; i < virtualMachineWordSize; i++){
                    if (RealMachine.getStepping()){
                        System.out.println("Character " + (char) popFromStack() + " popped from stack");
                    } else {
                        popFromStack();
                    }
                }
                break;

            case "ADD":
                System.out.println("ADD reached");

                character1 = popFromStack();
                character2 = popFromStack();
                character3 = popFromStack();
                character4 = popFromStack();
                operand1 = convertBytesToInt(character1, character2, character3, character4);

                character1 = popFromStack();
                character2 = popFromStack();
                character3 = popFromStack();
                character4 = popFromStack();
                operand2 = convertBytesToInt(character1, character2, character3, character4);

                result = CPU.add(operand1, operand2);
                resultBytes = convertStringToBytes(String.valueOf(result));

                if (RealMachine.getStepping()){
                    System.out.println("Operand1 = " + operand1);
                    System.out.println("Operand2 = " + operand2);
                    System.out.println("Result = " + result);
                }

                for (int i = resultBytes.length-1; i >= 0 ; i--){
                    if (RealMachine.getStepping()){
                        System.out.println("Character " + (char) resultBytes[i] + " pushed to stack");
                        pushToStack(resultBytes[i]);
                    } else {
                        pushToStack(resultBytes[i]);
                    }
                }
                break;

            case "SUB":
                System.out.println("SUB reached");

                character1 = popFromStack();
                character2 = popFromStack();
                character3 = popFromStack();
                character4 = popFromStack();
                operand1 = convertBytesToInt(character1, character2, character3, character4);

                character1 = popFromStack();
                character2 = popFromStack();
                character3 = popFromStack();
                character4 = popFromStack();
                operand2 = convertBytesToInt(character1, character2, character3, character4);

                result = CPU.sub(operand1, operand2);
                resultBytes = convertStringToBytes(String.valueOf(result));

                if (RealMachine.getStepping()){
                    System.out.println("Operand1 = " + operand1);
                    System.out.println("Operand2 = " + operand2);
                    System.out.println("Result = " + result);
                }

                for (int i = resultBytes.length-1; i >= 0 ; i--){
                    if (RealMachine.getStepping()){
                        System.out.println("Character " + (char) resultBytes[i] + " pushed to stack");
                        pushToStack(resultBytes[i]);
                    } else {
                        pushToStack(resultBytes[i]);
                    }
                }
                break;

            case "MUL":
                System.out.println("MUL reached");

                character1 = popFromStack();
                character2 = popFromStack();
                character3 = popFromStack();
                character4 = popFromStack();
                operand1 = convertBytesToInt(character1, character2, character3, character4);

                character1 = popFromStack();
                character2 = popFromStack();
                character3 = popFromStack();
                character4 = popFromStack();
                operand2 = convertBytesToInt(character1, character2, character3, character4);

                result = CPU.mul(operand1, operand2);
                resultBytes = convertStringToBytes(String.valueOf(result));

                if (RealMachine.getStepping()){
                    System.out.println("Operand1 = " + operand1);
                    System.out.println("Operand2 = " + operand2);
                    System.out.println("Result = " + result);
                }

                for (int i = resultBytes.length-1; i >= 0 ; i--){
                    if (RealMachine.getStepping()){
                        System.out.println("Character " + (char) resultBytes[i] + " pushed to stack");
                        pushToStack(resultBytes[i]);
                    } else {
                        pushToStack(resultBytes[i]);
                    }
                }
                break;

            case "DIV":
                System.out.println("DIV reached");

                character1 = popFromStack();
                character2 = popFromStack();
                character3 = popFromStack();
                character4 = popFromStack();
                operand1 = convertBytesToInt(character1, character2, character3, character4);

                character1 = popFromStack();
                character2 = popFromStack();
                character3 = popFromStack();
                character4 = popFromStack();
                operand2 = convertBytesToInt(character1, character2, character3, character4);

                result = CPU.div(operand1, operand2);

                System.out.println("Result = " + result);
                resultBytes = convertStringToBytes(String.valueOf(result));

                if (RealMachine.getStepping()){
                    System.out.println("Operand1 = " + operand1);
                    System.out.println("Operand2 = " + operand2);
                    System.out.println("Result = " + result);
                }

                for (int i = resultBytes.length-1; i >= 0 ; i--){
                    if (RealMachine.getStepping()){
                        System.out.println("Character " + (char) resultBytes[i] + " pushed to stack");
                        pushToStack(resultBytes[i]);
                    } else {
                        pushToStack(resultBytes[i]);
                    }
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

            case "FORK":
                System.out.println("FORK reached");
                fork();
                break;

            case "PRTN":
                System.out.println("PRTN reached");
                int printerCounter = 0;
                System.out.println("Moving data from Virtual Machine memory to Printer memory");
                for (int i = resultPtr; i < resultStackPointer; i = i + virtualMachineWordSize){
                    for (int j = virtualMachineWordSize-1; j >= 0; j--){
                        if (getData(i+j) != 0){
                            ChannelDevice.xchg("VirtualMemory", "PrinterMemory", (i+j), Printer.getPtr() + printerCounter);
                            printerCounter++;
                        }
                    }
                }

                while(resultStackPointer > resultPtr){
                    popFromStack();
                }

                System.out.println("Data transfer was successful");
                Printer.print();
                break;

            case "CMP":
                System.out.println("CMP reached");

                character1 = popFromStack();
                character2 = popFromStack();
                character3 = popFromStack();
                character4 = popFromStack();
                operand1 = convertBytesToInt(character1, character2, character3, character4);
                character1 = popFromStack();
                character2 = popFromStack();
                character3 = popFromStack();
                character4 = popFromStack();
                operand2 = convertBytesToInt(character1, character2, character3, character4);
                result = CPU.sub(operand1, operand2);

                if (CPU.getZF() == 1){
                    resultBytes = convertStringToBytes("0");
                    if (RealMachine.getStepping()){
                        System.out.println("Comparing values: " + operand1 + " and " + operand2);
                        System.out.println("Values are equal. Putting 0 at the top of the stack");
                    }

                    for (int i = resultBytes.length-1; i >= 0 ; i--){
                        pushToStack(resultBytes[i]);
                    }
                    CPU.resetZF();
                } else {
                    if (result < 0){
                        resultBytes = convertStringToBytes("2");

                        if (RealMachine.getStepping()){
                            System.out.println("Comparing values: " + operand1 + " and " + operand2);
                            System.out.println("Operand2 is bigger. Putting 2 at the top of the stack");
                        }
                        for (int i = resultBytes.length-1; i >= 0 ; i--){
                            pushToStack(resultBytes[i]);
                        }
                    } else {
                        resultBytes = convertStringToBytes("1");

                        if (RealMachine.getStepping()){
                            System.out.println("Comparing values: " + operand1 + " and " + operand2);
                            System.out.println("Operand1 is bigger. Putting 1 at the top of the stack");
                        }
                        for (int i = resultBytes.length-1; i >= 0 ; i--){
                            pushToStack(resultBytes[i]);
                        }
                    }
                }
                break;

            case "SWP":
                System.out.println("SWP reached");

                character1 = popFromStack();
                character2 = popFromStack();
                character3 = popFromStack();
                character4 = popFromStack();

                character5 = popFromStack();;
                character6 = popFromStack();
                character7 = popFromStack();
                character8 = popFromStack();

                pushToStack(character4);
                pushToStack(character3);
                pushToStack(character2);
                pushToStack(character1);

                pushToStack(character8);
                pushToStack(character7);
                pushToStack(character6);
                pushToStack(character5);

                if (RealMachine.getStepping()){
                    System.out.println("Top of the stack: " + (char) character1 + " " + (char) character2 + " " + (char) character3 + " " + (char) character4);
                    System.out.println("Second from the top: " + (char) character5 + " " + (char) character6 + " " + (char) character7 + " " + (char) character8);
                    System.out.println("Elements swapped");
                }
                break;

            case "INC":
                System.out.println("INC reached");

                character1 = popFromStack();
                character2 = popFromStack();
                character3 = popFromStack();
                character4 = popFromStack();
                operand1 = convertBytesToInt(character1, character2, character3, character4);

                result = CPU.add(operand1, 1);
                resultBytes = convertStringToBytes(String.valueOf(result));

                if (RealMachine.getStepping()){
                    System.out.println("Top of the stack value is incremented by 1");
                    System.out.println("Value found: " + character1 + " " + character2 + " " + character3 + " " + character4);
                }

                for (int i = resultBytes.length-1; i >= 0 ; i--){
                    if (RealMachine.getStepping()){
                        System.out.println("Character " + (char) resultBytes[i] + " is pushed to the top of the stack");
                    } else {
                        pushToStack(resultBytes[i]);
                    }
                }
                break;

            case "DEC":
                System.out.println("DEC reached");

                character1 = popFromStack();
                character2 = popFromStack();
                character3 = popFromStack();
                character4 = popFromStack();
                operand1 = convertBytesToInt(character1, character2, character3, character4);

                result = CPU.sub(operand1, 1);
                resultBytes = convertStringToBytes(String.valueOf(result));

                if (RealMachine.getStepping()){
                    System.out.println("Top of the stack value is decremented by 1");
                    System.out.println("Value found: " + character1 + " " + character2 + " " + character3 + " " + character4);
                }

                for (int i = resultBytes.length-1; i >= 0 ; i--){
                    if (RealMachine.getStepping()){
                        System.out.println("Character " + (char) resultBytes[i] + " is pushed to the top of the stack");
                    } else {
                        pushToStack(resultBytes[i]);
                    }
                }
                break;

            case "HALT":
                System.out.println("HALT reached");
                RealMachine.setPI(5);
                break;

            case "$END":
                System.out.println("$END reached");
                RealMachine.setPI(6);
                break;
        }
        RealMachine.decrementTI();
    }

    public int fork(){
        Supervisor.clearMemory();
        int remainingSize = stackPointer - ptr;
        if (RealMachine.getStepping()){
            System.out.println("Requesting memory for new virtual machine in supervisor");
        }

        int newPtr = Supervisor.requestMemory(remainingSize);

        if (newPtr < Supervisor.getPtr()){
            RealMachine.setPI(4);
            return -1;
        }

        if (RealMachine.getStepping()){
            System.out.println("Memory acquired");
        }
        int counter = 0;
        if (RealMachine.checkFileSyntax()){
            if (RealMachine.getStepping()){
                System.out.println("Syntax is correct");
                System.out.println("Copying data to Supervisor memory");
            }
            for (int i = stackPointer; i >= ptr; i--){
                ChannelDevice.xchg("VirtualMemory", "SupervisorMemory", i, newPtr+counter);
                counter++;
            }
            if (RealMachine.getStepping()){
                System.out.println("Copying completed");
            }
            if (RealMachine.getStepping()){
                System.out.println("Requesting memory from Real Memory");
            }

            newPtr = RealMemory.requestMemory(remainingSize);
            if (newPtr < 0){
                RealMachine.setPI(3);
                return -1;
            }

            if (RealMachine.getStepping()){
                System.out.println("Memory Acquired");
                System.out.println("Creating a new virtual machine");
            }

            VirtualMachine virtualMachine = new VirtualMachine(newPtr);
            if (RealMachine.getStepping()){
                System.out.println("New virtual machine created");
                System.out.println("Transferring data from supervisor to new virtual machine");
            }

            for (int i = 0; i <= remainingSize; i++){
                ChannelDevice.xchg("SupervisorMemory", "VirtualMemory", Supervisor.getPtr() + remainingSize - i, newPtr + i);
                counter++;
            }

            if (RealMachine.getStepping()){
                System.out.println("Data transferred");
                System.out.println("Running child branch");
            }

            virtualMachine.setStackPointer(newPtr+remainingSize);
            virtualMachine.setResultPtr(virtualMachine.getStackPointer()+1);
            virtualMachine.setResultStackPointer(virtualMachine.getResultPtr());
            virtualMachine.exec();
            RealMachine.resetStopRunning();
        } else {
            RealMachine.setPI(2);
        }
        return 0;
    }

    public byte[] convertStringToBytes(String string){
        byte[] byteArray = new byte[virtualMachineWordSize];
        if (string.length() == 1){
            byteArray[0] = 0;
            byteArray[1] = 0;
            byteArray[2] = 0;
            byteArray[3] = (byte) string.charAt(0);
        } else if (string.length() == 2){
            byteArray[0] = 0;
            byteArray[1] = 0;
            byteArray[2] = (byte) string.charAt(0);
            byteArray[3] = (byte) string.charAt(1);
        } else if (string.length() == 3){
            byteArray[0] = 0;
            byteArray[1] = (byte) string.charAt(0);
            byteArray[2] = (byte) string.charAt(1);
            byteArray[3] = (byte) string.charAt(2);
        } else {
            byteArray[0] = (byte) string.charAt(0);
            byteArray[1] = (byte) string.charAt(1);
            byteArray[2] = (byte) string.charAt(2);
            byteArray[3] = (byte) string.charAt(3);
        }
        return byteArray;
    }

    public int convertBytesToInt(byte character1, byte character2, byte character3, byte character4){
        StringBuilder characters = new StringBuilder();
        characters.append((char) character1);
        characters.append((char) character2);
        characters.append((char) character3);
        characters.append((char) character4);
        String value = characters.toString();
        String trimmedStr = value.trim();
        return Integer.parseInt(trimmedStr);
    }

    public void clearMemory(){
        for (int i = ptr; i < size; i++){
            RealMemory.write(i, (byte) 0);
        }
    }

    public byte getData(int index){
        return RealMemory.getData(index);
    }

    public static int getSize(){
        return size;
    }

    public void setStackPointer(int stackPointer) {
        this.stackPointer = stackPointer;
    }

    public void setResultPtr(int resultPtr) {
        this.resultPtr = resultPtr;
    }

    public void setResultStackPointer(int resultStackPointer) {
        this.resultStackPointer = resultStackPointer;
    }

    public int getResultPtr() {
        return resultPtr;
    }

    public int getResultStackPointer() {
        return resultStackPointer;
    }

    public int getStackPointer() {
        return stackPointer;
    }

    public int getPtr() {
        return ptr;
    }

    public byte pop(){
        byte data = RealMemory.getData(stackPointer);
        RealMemory.remove(stackPointer);
//        System.out.println("POPPED character = " + (char) data + " stackPointer = " + stackPointer);
        stackPointer--;
        return data;
    }

    public byte popFromStack(){
        resultStackPointer--;
        byte data = RealMemory.getData(resultStackPointer);
        RealMemory.remove(resultStackPointer);
        return data;
    }

    public void pushToStack(byte data){
        RealMemory.write(resultStackPointer, data);
        resultStackPointer++;
    }


    //TODO DELETE THIS METHOD
    public void printVirtualMemory(){
        for (int i = ptr; i < ptr + size; i++){
            if (getData(i) == 0 && getData(i+1) == 0 && getData(i+2) == 0 && getData(i+3) == 0){
                break;
            }
            System.out.println("Virtual Memory address: " + i + " Data found: " + getData(i) + " char: " + (char) getData(i));
        }
    }

    public void printResult(){
        for (int i = resultStackPointer-1; i >= resultPtr ; i--){
            System.out.println("Result Memory stack pointer = " + i + " Found data: " + (char) RealMemory.getData(i));
        }
    }
}