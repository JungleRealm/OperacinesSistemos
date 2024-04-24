public class ExternalMemory {

    private static int ptr = Supervisor.getSupervisorSize();
    private static int memoryMultiplier = 5;
    private static int memorySize = memoryMultiplier * RealMachine.getBlockSize() * RealMachine.getWordSize() * RealMachine.getWords();
    private static byte[] memory = new byte[memorySize];

    public static void write(int index, byte data){
        memory[index] = data;
//        if (memory[index] == 0){
//            memory[index] = data;
//        } else {
//            System.out.println("Attempting to write into memory slot that already has data");
//            RealMachine.setPI(1);
//            return;
//        }

    }

    public static void writeToSupervisor(int index, byte data){
        memory[index] = data;
    }

    public static byte getData(int index){
        return memory[index];
    }

    public static void clearMemory(){
        for (int i = ptr; i < memorySize; i++){
            memory[i] = 0;
        }
    }


    public static int getPtr() {
        return ptr;
    }

//    public static int getShift() {
//        return shift;
//    }

    // TODO DELETE THIS METHOD
//    public static void printMemory(){
//        for (int i = ptr; i < ptr + shift; i++){
//            System.out.println("Real Memory address: " + i + " data found: " + memory[i] + " char: " + (char) memory[i]);
//        }
//    }

    public static int getMemorySize() {
        return memorySize;
    }

    public static int requestMemory(int size){
        int foundSize = 0;
        int pointerToStart = ptr;
        for (int i = ptr; i < memorySize; i++){
            if (size > memorySize-i){
                RealMachine.setPI(3);
                return -1;
            }
            if (getData(i) == 0){
                foundSize++;
                if (foundSize == size){
                    return pointerToStart;
                }
            } else {
                pointerToStart = i+1;
                foundSize = 0;
            }
        }
        return -1;
    }

    public static void remove(int index){
        memory[index] = 0;
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
