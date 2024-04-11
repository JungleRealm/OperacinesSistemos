import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class Main {
    public static void main(String[] args) throws IOException, MemoryOutOfBoundsException, NotEnoughMemoryException {

        RealMachine realMachine = new RealMachine();

//        System.out.println("---------------------------------------------------------------");
        Stack<Word> commands = Input.readFile("C:\\Projects\\OS_2_final\\Files\\Test1.txt");
//        System.out.println(commands);
//        System.out.println("After reading input");
//        for(int i = 0; i < commands.size(); i++){
//            System.out.println(Word.wordToInt(commands.get(i)));
//        }
//        System.out.println("-----------------------------------------------------------------");

        realMachine.storeCommands(commands);
//        System.out.println("-------------------------------------------------------------------");


        realMachine.run();


    }
}
