import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Stack;

public class Output {
    private final ArrayList<String> printerMemory = new ArrayList();

    public void add(String item){
        this.printerMemory.add(item);
    }

    public void print() {
        System.out.println("Output: " + this.printerMemory);
    }

}
