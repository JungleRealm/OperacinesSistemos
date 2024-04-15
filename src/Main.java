import java.io.IOException;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        String filePath = "C:\\Projects\\OS_2_1\\Files\\test1.txt";
        Stack<String> readInput;
        try {
            readInput = RealMachine.read(filePath);
            Stack<Byte> result = RealMachine.convertToBytes(readInput);
            if (result == null){
                return;
            }
//            System.out.println(RealMemory.getFileSizeIndex());
            if (RealMemory.setAndStoreFileSize(result.size())){
//                System.out.println(RealMemory.getFileSizeIndex());
                Stack<Byte> orderedStack = RealMachine.orderStack(result);
//                System.out.println(orderedStack);
                Stack<Byte> reversed = RealMachine.reverseStack(orderedStack);
//                System.out.println(reversed);
                RealMemory.storeInMemory(reversed);
                ChannelDevice.moveFromRealMemoryToSupervizor(RealMemory.getFreeMemoryPointerStart(), RealMemory.getFreeMemoryPointer());
                RealMachine.initializeVirtualMachine();
//                CPU.run(Supervizor.getSupervizoryMemoryStart(), Supervizor.getSupervizorMemoryEnd());

            }







        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
