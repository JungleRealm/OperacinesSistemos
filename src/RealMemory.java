import java.nio.ByteBuffer;
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


    // TODO might need to redo this part to accept the Stack Pointer and size or something?
    public static byte[] getMemoryArray(int start, int stop) {
        byte[] requestedMemoryArray = new byte[stop-start];
        for (int i = 0; i < stop - start; i++){
            requestedMemoryArray[i] = memory[start + i];
            removeElementFromMemory(start + i);
        }
        return requestedMemoryArray;
    }

    public static void removeElementFromMemory(int index){
        memory[index] = 0;
    }

    //TODO move this method to the ChannelDevice class. That class is responsible for moving data between components
//    public static void writeToSupervizorMemory(byte[] array){
//        if (array.length > supervizorMemoryBlocks *words*wordSize){
//            System.out.println("Array is too big to be stored in a supervizor memory");
//            return;
//        }
//        for (int i = 0; i < array.length; i++){
//            memory[i] = array[i];
//            supervizorLastElement++;
//        }
//    }


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

}
