public class ChannelDevice {

    public static void moveFromRealMemoryToSupervizor(int fromStart, int fromStop) {

        for (int i = fromStart; i < fromStop; i++){

            Supervizor.writeToSupervizorMemory(Supervizor.getSupervizorMemoryEnd(), RealMemory.getElementFromMemory(i));
//            System.out.println(Supervizor.getSupervizorMemoryEnd() + " <- " + RealMemory.getElementFromMemory(i));
            RealMemory.removeElementFromMemory(i);
        }
    }

    public static void moveFromSupervizorToVirtualMachineMemory(VirtualMachine virtualMachine){
        int counter = Supervizor.getSupervizoryMemoryStart();
        while (counter != Supervizor.getSupervizorMemoryEnd()){
//            System.out.println("StackEnd: " + virtualMachine.getStackPointer() + " " + virtualMachine.getFromVirtualMachineMemory(virtualMachine.getStackPointer()) + " <- " + Supervizor.getFromSupervizorMemory(counter));

            virtualMachine.writeToVirtualMachineMemory(virtualMachine.getStackPointer(), Supervizor.getFromSupervizorMemory(counter));
            Supervizor.removeFromSupervizorMemory(counter);
            counter++;
        }
    }

    public static void moveFromVirtualMachineMemoryToPrinter(int fromStart, int fromStop, int toStart, int toStop){

        System.out.println("Name mismatch, could not move information");
    }

}
