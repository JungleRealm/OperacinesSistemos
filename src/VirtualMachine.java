public class VirtualMachine {

    // TODO finish the virtual machine class

    private static final int virtualMachineBlocks = 16;
    private static final int virtualMachineWords = 16;
    private static final int virtualMachineWordSize = 4;
    private final int virtualMachineMemoryStart;
    private int stackPointer;
    private int pointerToResultStackStart;
    private int pointerToResultStackEnd;

    public VirtualMachine(){
        virtualMachineMemoryStart = RealMemory.requestSize(virtualMachineBlocks*virtualMachineWords*virtualMachineWordSize);
        stackPointer = virtualMachineMemoryStart;

//        System.out.println(virtualMachineMemoryStart + " ; " + stackPointer);

        // TODO create a check in CPU class to check if pointerToResultStack is a valid address and is not bigger than stackPointer + virtualMachineBlocks*virtualMachineWords*virtualMachineWordSize
    }

    public void resetStackPointer(){
        stackPointer = virtualMachineMemoryStart;
    }

    public void run(){
        pointerToResultStackStart = stackPointer;
        System.out.println("Result stack start" + pointerToResultStackStart);
        pointerToResultStackEnd = pointerToResultStackStart;
        stackPointer--;

//        resetStackPointer();
//        System.out.println(stackPointer);
        while(stackPointer > virtualMachineMemoryStart){
            CPU.run(this);
        }
    }

    public void decrementStackPointerByWordSize(){
        stackPointer = stackPointer - RealMemory.getWordSize();
    }

    public void fork(){
        stackPointer = stackPointer - 3;
//        System.out.println(virtualMachineMemoryStart);
//        stackPointer = stackPointer + RealMemory.getWordSize();
        int resultSize = pointerToResultStackEnd - pointerToResultStackStart;
        int size = stackPointer - virtualMachineMemoryStart;
//        System.out.println("VM memory start = " + virtualMachineMemoryStart);
//        System.out.println("Stack pointer = " + stackPointer);
//        System.out.println(size);

        // Move the result stack up by the amount of commands remaining to execute
        pointerToResultStackEnd = pointerToResultStackStart + size;
        for (int i = pointerToResultStackStart; i < resultSize + pointerToResultStackStart; i++){
            writeToVirtualMachineMemory((i + size), getFromVirtualMachineMemory(i));
            pointerToResultStackEnd++;
        }
        pointerToResultStackStart = pointerToResultStackStart + resultSize;

        int temp = virtualMachineMemoryStart + size;
        stackPointer = virtualMachineMemoryStart + size;
        for (int i = virtualMachineMemoryStart + RealMemory.getWordSize() + RealMemory.getWordSize(); i < temp; i++){
            writeToVirtualMachineMemory((i + size - RealMemory.getWordSize() - RealMemory.getWordSize()), getFromVirtualMachineMemory(i));
        }
        stackPointer--;

//        printAllVMMemory();

//        System.out.println("----");
//        testDataPrint();


    }

    public void writeToVirtualMachineMemoryWithoutIncrement(int index, byte data){
        RealMemory.writeToMemory(index, data);
    }

    public void printAllVMMemory(){
        for (int i = virtualMachineMemoryStart; i <= stackPointer; i++){
            System.out.println("stack pointer = " + stackPointer + "; VM memory address: " + i + "; Element found: " + (char)getFromVirtualMachineMemory(i));
        }
    }

    public void incrementStackPointerByWordSize(){
        stackPointer = stackPointer + RealMemory.getWordSize();
    }

    public void decrementPointerToResultStackEnd(){
        pointerToResultStackEnd--;
    }

    public byte popElementFromResultStack(){
        byte result = getFromVirtualMachineMemory(pointerToResultStackEnd-1);
        RealMemory.removeElementFromMemory(pointerToResultStackEnd-1);
        decrementPointerToResultStackEnd();
        return result;
    }

    public byte popElementFromVirtualMachineMemory(){
        byte result = getFromVirtualMachineMemory(stackPointer);
        removeFromVirtualMachineMemory(stackPointer);
        stackPointer--;
        return result;
    }

    public int getVirtualMachineMemoryStart() {
        return virtualMachineMemoryStart;
    }

    public void writeToVirtualMachineMemory(int index, byte data){
        RealMemory.writeToMemory(index, data);
        stackPointer++;
    }



    public void printVirtualMachineMemory(){
        for (int i = virtualMachineMemoryStart; i < stackPointer; i++){
            System.out.println("address = " + i + " element = " + getFromVirtualMachineMemory(i));
        }

    }

    public void removeFromVirtualMachineMemory(int index){
        RealMemory.removeElementFromMemory(index);
    }

    public void writeToResultStackMemory(int index, byte data){
        RealMemory.writeToMemory(index, data);
        pointerToResultStackEnd++;
    }

    public int getPointerToResultStackEnd() {
        return pointerToResultStackEnd;
    }

    public byte getFromVirtualMachineMemory(int index){
        return RealMemory.getElementFromMemory(index);
    }

    public int getStackPointer() {
        return stackPointer;
    }

    public int getPointerToResultStackStart() {
        return pointerToResultStackStart;
    }

    public void setPointerToResultStackStart(int pointerToResultStackStart) {
        this.pointerToResultStackStart = pointerToResultStackStart;
    }

    public void testDataPrint(){
        for (int i = pointerToResultStackStart; i < pointerToResultStackEnd; i++){
            System.out.println("Memory address: " +  i + " Data found: " + (char) getFromVirtualMachineMemory(i));
        }

    }
}
