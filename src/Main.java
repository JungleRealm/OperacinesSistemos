import java.io.IOException;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        String filePath = "C:\\Projects\\OS_2_1\\Files\\test6.txt";
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
//                System.out.println(orderedStack)
                RealMemory.storeInMemory(orderedStack);
                ChannelDevice.moveFromRealMemoryToSupervizor(RealMemory.getFreeMemoryPointerStart(), RealMemory.getFreeMemoryPointer());
                RealMachine.initializeVirtualMachine().run();

            }







        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
