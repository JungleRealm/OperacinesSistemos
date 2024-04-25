public class Printer {

    private static int ptr = 0;
    private static byte[] memory = new byte[2*16*16*4];

    public static void write(int index, byte data){
        memory[index] = data;
    }

    public static void print(){
        for (int i = 0; i < memory.length; i++){
            if (memory[i] == 0 && memory[i+1] == 0 && memory[i+2] == 0 && memory[i+3] == 0){
                return;
            }
            System.out.println("Printer output: " + (char) memory[i]);
        }
        clearMemory();
    }

    public static void clearMemory(){
        for (int i = ptr; i < memory.length; i++){
            write(i, (byte) 0);
        }
    }

    public static int getPtr() {
        return ptr;
    }
}