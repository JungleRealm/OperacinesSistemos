/*
Supervisor takes up from 0 -> 1024 in RealMemory memory.
When checking if information will fit in a supervisor, RealMemory requestSize method is called.
*/

public class Supervisor {

    private static int memorySize = RealMachine.getBlockSize() * RealMachine.getWords() * RealMachine.getWordSize();
    private static byte[] memory = new byte[memorySize];
    private static int ptr = 0;
    private static int shift = 0;

    public static int getSupervisorSize() {
        return memorySize;
    }

    public static int getPtr() {
        return ptr;
    }

    public static int requestMemory(int size){
        int foundSize = 0;
        int pointerToStart = ptr;
        for (int i = ptr; i < memorySize; i++){
            if (RealMemory.getData(i) == 0){
                foundSize++;
                if (foundSize == size){
                    return pointerToStart;
                }
            } else {
                pointerToStart = i;
                foundSize = 0;
            }
        }
        return -1;
    }

    public static void write(int index, byte data){
        RealMemory.writeToSupervisor(index, data);
        shift++;
    }

    public static void clearMemory(){
        for (int i = ptr; i < memorySize; i++){
            RealMemory.write(i, (byte) 0);
        }
    }

    public static byte getData(int index){
        return RealMemory.getData(index);
    }

    // TODO DELETE THIS METHOD
    public static void printSupervisorMemory(){
        for (int i = ptr; i < shift; i++){
            System.out.println("Supervisor memory address: " + i + " Data found: " + RealMemory.getData(i) + " char: " + (char) RealMemory.getData(i));
        }
    }

}
