public class VirtualMachine {

    // TODO finish the virtual machine class

    private static final int virtualMachineBlocks = 16;
    private static final int virtualMachineWords = 16;
    private static final int virtualMachineWordSize = 4;
    private final int VirtualMachineMemoryStart;
    private int stackPointer;

    public VirtualMachine(){
        stackPointer = RealMemory.requestSize(virtualMachineBlocks*virtualMachineWords*virtualMachineWordSize);
        VirtualMachineMemoryStart = stackPointer;

    }

    public void run(){
        CPU.run(VirtualMachineMemoryStart, stackPointer);
    }

    public int getVirtualMachineMemoryStart() {
        return VirtualMachineMemoryStart;
    }

    public void writeToVirtualMachineMemory(int index, byte data){
        RealMemory.writeToMemory(index, data);
        stackPointer++;
    }

    public byte getFromVirtualMachineMemory(int index){
        return RealMemory.getElementFromMemory(index);
    }

    public int getStackPointer() {
        return stackPointer;
    }
}
