public class ChannelDevice {

    public static void moveFromRealMemoryToSupervizor(int fromStart, int fromStop) {

        for (int i = fromStart; i < fromStop; i++){

            Supervizor.writeToSupervizorMemory(Supervizor.getSupervizorMemoryEnd(), RealMemory.getElementFromMemory(i));
//            System.out.println(Supervizor.getSupervizorMemoryEnd() + " <- " + RealMemory.getElementFromMemory(i));
            RealMemory.removeElementFromMemory(i);
        }
    }

    public static void moveFromSupervizorToVirtualMachineMemory(VirtualMachine virtualMachine){

        for (int i = Supervizor.getSupervizoryMemoryStart(); i < Supervizor.getSupervizorMemoryEnd(); i++){
//            System.out.println("stack pointer doring memory move: " + virtualMachine.getStackPointer());
            virtualMachine.writeToVirtualMachineMemory(virtualMachine.getStackPointer(), Supervizor.getFromSupervizorMemory(i));
            Supervizor.removeFromSupervizorMemory(i);
        }

    }

    public static void moveFromVirtualMachineMemoryToPrinter(byte data){
        Output.addToStack(data);
    }

}
