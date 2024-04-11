import java.util.Stack;

public class MemoryManagementUnit {

    private int maxCommandSize = 1;

    private RealMemory realMemory;
    private RealMemory externalMemory;
    private int memoryPointer;
    private int arraySize;

    public MemoryManagementUnit(RealMemory realMemory, RealMemory externalMemory) {
        this.realMemory = realMemory;
        this.externalMemory = externalMemory;
        this.memoryPointer = 0;
    }

    public void storeCommands(Stack<Word> commands) throws NotEnoughMemoryException {
        this.arraySize = commands.size();
        //System.out.println(arraySize);
        int totalMemoryRequired = commands.size();
        if (totalMemoryRequired <= realMemory.getSize()) {
            // Store commands in real memory
            while (!commands.isEmpty()) {
                realMemory.write(memoryPointer, Word.wordToInt(commands.pop()));
                memoryPointer += maxCommandSize;
            }
        } else if (totalMemoryRequired <= externalMemory.getSize()) {
            // Store commands in external memory
            while (!commands.isEmpty()) {
                externalMemory.write(memoryPointer, Word.wordToInt(commands.pop()));
                memoryPointer += maxCommandSize;
            }
        } else {
            throw new NotEnoughMemoryException("Not enough memory to store commands.");
        }
    }

//    public int popFromMemory() throws NotEnoughMemoryException {
//        int data = 0;
//        try {
//            if (programCounter < realMemory.getSize()) {
//                data = realMemory.pop(programCounter);
//            } else {
//                data = externalMemory.pop(programCounter - realMemory.getSize());
//            }
//            programCounter += maxCommandSize; // Increment program counter for next instruction
//        } catch (MemoryOutOfBoundsException e) {
//            e.printStackTrace();
//        }
//        return data;
//    }

    public RealMemory getRealMemory(){
        return realMemory;
    }

    public RealMemory getExternalMemory(){
        return externalMemory;
    }

    public int getMemoryPointer(){
        return this.memoryPointer;
    }

    public int getMaxCommandSize() {
        return maxCommandSize;
    }

    public int getArraySize(){
        return this.arraySize;
    }
}
