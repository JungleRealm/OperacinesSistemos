import java.util.Stack;

public class RealMachine {
    private MemoryManagementUnit memoryManagementUnit;
    private RealMemory realMemory;
    private RealMemory externalMemory;
    private CPU cpu;
    private Input input;
    private Output output;

    private int supervizorSize = 16;
    private int realSize = 4300;
    private int externalSize = 600;

    public RealMachine(){
        input = new Input();
        output = new Output();
        externalMemory = new RealMemory(externalSize);
        realMemory = new RealMemory(realSize);
        memoryManagementUnit = new MemoryManagementUnit(realMemory, realMemory);
        cpu = new CPU(memoryManagementUnit, output);
    }

    public void storeCommands(Stack<Word> commands) throws NotEnoughMemoryException {
        Stack<Word> reversedArray = Word.reverseArray(commands);
//        System.out.println("Post array reversal");
//        for(int i = 0; i < reversedArray.size(); i++){
//            System.out.println(Word.wordToInt(reversedArray.get(i)));
//        }
        memoryManagementUnit.storeCommands(reversedArray);
    }

    public void run() throws MemoryOutOfBoundsException {
        cpu.run();
    }
}
