public class RealMemory {
    private static final int COMMAND_SIZE = 4; // Size of each command in bytes
    private int[] memory = null;

    public RealMemory(int size) {
        memory = new int[size];
    }

    public Word read(int address) {
        return Word.intToWord(memory[address]);
    }

    public void write(int address, int data) {
//        System.out.println("address: " + address + " data: " + data);
        memory[address] = data;
    }

    public int getSize() {
        return memory.length;
    }

//    public int getCommandSize(){
//        return this.getCommandSize();
//    }

    public Word pop(int address) throws MemoryOutOfBoundsException {
        if (isValidAddress(address)) {
            Word data = Word.intToWord(memory[address]);
            memory[address] = 0; // Clear the memory location
            return data;
        } else {
            throw new MemoryOutOfBoundsException();
        }
    }

    public boolean isValidAddress(int address) {
        return address >= 0 && address < memory.length;
    }
}