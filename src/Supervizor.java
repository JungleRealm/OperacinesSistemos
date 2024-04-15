public class Supervizor {

    private static final int supervizorMemoryBlocks = 16; //BLOCKS
    private static final int fullSupervizorMemory = supervizorMemoryBlocks * RealMemory.getWords() * RealMemory.getWordSize();
    private static int supervizoryMemoryStart = 0;
    private static int supervizorMemoryEnd = 0;


    public static void writeToSupervizorMemory(int index, byte data){
        RealMemory.writeToMemory(index, data);
        supervizorMemoryEnd++;
    }

    public static byte getFromSupervizorMemory(int index){
        return RealMemory.getElementFromMemory(index);
    }


    public static int getFullSupervizorMemory() {
        return fullSupervizorMemory;
    }

    public static int getSupervizorMemoryBlocks() {
        return supervizorMemoryBlocks;
    }

    public static int getSupervizoryMemoryStart() {
        return supervizoryMemoryStart;
    }

    public static int getSupervizorMemoryEnd() {
        return supervizorMemoryEnd;
    }

    public static void removeFromSupervizorMemory(int index){
        RealMemory.removeElementFromMemory(index);
    }
}
