public class ExternalMemory {
    private static int ptr = 0;
    private static int memorySize = 2*16*16*4;
    private static byte[] memory = new byte[memorySize];
    private static int shift = 0;

    public static void write(int index, byte data){
        if (memory[index] == 0){
            memory[index] = data;
            shift++;
        }
        System.out.println("Attempting to write into memory slot that already has data");
    }

    public static byte getData(int index){
        return memory[index];
    }


    public static int getPtr() {
        return ptr;
    }

    public static int getShift() {
        return shift;
    }

    // TODO DELETE THIS METHOD
    public static void printMemory(){
//        System.out.println(ptr + " ; " + shift);
        for (int i = ptr; i < ptr + shift; i++){
            System.out.println("External Memory address: " + i + " data found: " + memory[i] + " char: " + (char) memory[i]);
        }
    }

    public static int getMemorySize() {
        return memorySize;
    }

    public static int requestMemory(int size){
        int foundSize = 0;
        int pointerToStart = ptr;
        for (int i = ptr; i < memorySize; i++){
            if (getData(i) == 0){
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

    public static int getFileSize(String fileName){
        char[] fileChar = new char[fileName.length()];
        for (int i = 0; i < fileName.length(); i++){
            fileChar[i] = fileName.charAt(i);
        }
        int tempCounter = 0;

        for (int i = ptr; i < memorySize; i++){
            if (getData(i) == fileChar[0]){
                for (int j = 0; j < fileChar.length; j++){
                    if (getData(i+j) == fileChar[j]){
                        tempCounter++;
                        if (tempCounter == fileChar.length){
                            tempCounter = 0;
                            for (int memoryAddress = i; memoryAddress < memory.length; memoryAddress++){
                                if ((char) getData(memoryAddress) == '$' && (char) getData(memoryAddress+1) == 'E' && (char) getData(memoryAddress+2) == 'N' && (char) getData(memoryAddress+3) == 'D'){
                                    return  tempCounter-1;
                                }
                                tempCounter++;
                            }
                        }
                    }
                }
                tempCounter = 0;
            }
        }
        return -1; // FILE WAS NOT FOUND
    }

    public static int getFileStartPointer(String fileName){
        char[] fileChar = new char[fileName.length()];
        for (int i = 0; i < fileName.length(); i++){
            fileChar[i] = fileName.charAt(i);
        }
        int tempCounter = 0;
        for (int i = ptr; i < memorySize; i++){
            if (getData(i) == fileChar[0]){
                for (int j = 0; j < fileChar.length; j++){
                    if (getData(i+j) == fileChar[j]){
                        tempCounter++;
                        if (tempCounter == fileChar.length){
                            return i + fileChar.length;
                        }
                    }
                }
                tempCounter = 0;
            }
        }
        return -1; // FILE WAS NOT FOUND
    }


}
