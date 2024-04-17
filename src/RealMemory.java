import java.util.Stack;

public class RealMemory {
    private static final int blocks = 4*16; //BLOCKS
    private static final int words = 16; //WORDS
    private static final int wordSize = 4; //BYTES
    private static int freeMemoryPointer = Supervizor.getFullSupervizorMemory();
    private static int freeMemoryPointerStart = Supervizor.getFullSupervizorMemory();

    // BLOCK ; WORD ; A WORD IS MADE OF 4 BYTES
    private static byte[] memory = new byte[((blocks+ Supervizor.getSupervizorMemoryBlocks())*words*wordSize)]; // RESULT = 5120 bytes

    private static int fileSizeIndex = 0;

    public static boolean setAndStoreFileSize(int size){
        for (int i = memory.length-1; i > Supervizor.getFullSupervizorMemory(); i = i - RealMemory.getWordSize()){
            if (memory[i] + memory[i-1] + memory[i-2] + memory[i-3] == 0){

                memory[i-3] = (byte) ((size >> 24) & 0xFF); // Most significant byte
                memory[i-2] = (byte) ((size >> 16) & 0xFF); // 2nd most significant byte
                memory[i-1] = (byte) ((size >> 8) & 0xFF); // 2nd least significant byte
                memory[i] = (byte) (size & 0xFF); // Least significant byte

                fileSizeIndex = i-3;
                return true;
            }
        }
        return false;
    }

    public static int getFileSizeIndex() {
        return fileSizeIndex;
    }

    public static int getFileSize(){
        return ((memory[fileSizeIndex] & 0xFF) << 24) | ((memory[fileSizeIndex+1] & 0xFF) << 16) | ((memory[fileSizeIndex+2] & 0xFF) << 8) | (memory[fileSizeIndex+3] & 0xFF);
    }

    public static void storeInMemory(Stack<Byte> inputStack){

        while (!inputStack.empty()){
            memory[freeMemoryPointer] = inputStack.pop();
            freeMemoryPointer++;
        }
    }

    public static int getBlocks() {
        return blocks;
    }

    public static int getWords() {
        return words;
    }

    public static int getWordSize() {
        return wordSize;
    }

    public static void removeElementFromMemory(int index){
        memory[index] = 0;
    }

    public static int getFreeMemoryPointer() {
        return freeMemoryPointer;
    }

    public static int getFreeMemoryPointerStart() {
        return freeMemoryPointerStart;
    }

    public static int requestSize(int requestedSize){
        int memoryCounterStartingPosition = Supervizor.getFullSupervizorMemory();
        int memoryCounter = 0;
        for (int i = Supervizor.getFullSupervizorMemory(); i < memory.length; i=i+wordSize){
//            System.out.println("Requested size: " + requestedSize + " i value: " + i);
            if (requestedSize > memory.length - i){
                return -1;
            }
//            System.out.println(memory[i] + memory[i+1] + memory[i+2] + memory[i+3]);
            if (memory[i] == 0 && memory[i+1] == 0 && memory[i+2] == 0 && memory[i+3] == 0){

                if (memoryCounter == requestedSize){
                    return memoryCounterStartingPosition;
                }
                memoryCounter = memoryCounter + wordSize;
            } else {
                memoryCounterStartingPosition = i;
                memoryCounter = 0;
            }
//            System.out.println("MemoryCounterStartingPosition: " + memoryCounterStartingPosition + "; counter: " + memoryCounter + "; Elements found: " + memory[i] + " " + memory[i+1] + " " + memory[i+2] + " " + memory[i+3]);
        }
        return -1;
    }

    public static byte getElementFromMemory(int index){
        return memory[index];
    }

    public static void writeToMemory(int index, byte data){
        memory[index] = data;
    }

    public static byte pop(){
        byte popped = memory[freeMemoryPointer-1];
//        System.out.println("popped element = " + (char) popped);
        removeElementFromMemory(freeMemoryPointer-1);
        freeMemoryPointer--;
        return popped;
    }

    public static byte getCommandInBytes(){
        return pop();
    }


    public static String getCommand(VirtualMachine virtualMachine){
//        byte character1 = RealMemory.getElementFromMemory(CPU.getSP());
//        byte character2 = RealMemory.getElementFromMemory(CPU.getSP()+1);
//        byte character3 = RealMemory.getElementFromMemory(CPU.getSP()+2);
//        byte character4 = RealMemory.getElementFromMemory(CPU.getSP()+3);
//        return cropWord(character1, character2, character3, character4);

//        byte character1 = RealMemory.pop();
//        byte character2 = RealMemory.pop();
//        byte character3 = RealMemory.pop();
//        byte character4 = RealMemory.pop();

        byte character1 = virtualMachine.popElementFromVirtualMachineMemory();
        byte character2 = virtualMachine.popElementFromVirtualMachineMemory();
        byte character3 = virtualMachine.popElementFromVirtualMachineMemory();
        byte character4 = virtualMachine.popElementFromVirtualMachineMemory();

//        System.out.println("char1: " + (char) character1 + "; char2: " + (char) character2 + "; char3: " + (char) character3 + "; char4: " + (char) character4);

        return cropWord(character1, character2, character3, character4);
    }

    public static String cropWord(byte character1, byte character2, byte character3, byte character4){
        StringBuilder word = new StringBuilder();
        if (character1 == 0){
            if (character2 == 0){
                if (character3 == 0){
                    if (character4 == 0){
//                        System.out.println("Illegal Argument encountered when fetching command");
                        return null;
                    } else {
                        word.append((char) character4);
                    }
                } else {
                    word.append((char) character3);
                    word.append((char) character4);
                }
            } else {
                word.append((char) character2);
                word.append((char) character3);
                word.append((char) character4);
            }
        } else {
            word.append((char) character1);
            word.append((char) character2);
            word.append((char) character3);
            word.append((char) character4);
        }
        return word.toString();
    }

}
