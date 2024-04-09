public class Main {
    public static void main(String[] args) {

        RealMachine realMachine = new RealMachine();
        int i;

        try {
            i = realMachine.loadProgram("C:\\Projects\\OSMain\\files\\Test1.txt");
            System.out.println("Program loaded successfully with index " + i);
            int stop = 0;
            while (stop == 0) {
                stop = realMachine.nextStep();
            }

        } catch (LoaderParseException ex) {
            System.out.println("Loading failed. Could not parse specified file.");
        } catch (OutOfMemoryException ex){
            System.out.println("Loading failed. Not enough memory.");
        } catch (MemoryException ex) {
            System.out.println("Unexpected memory failure. Loading failed.");
        }

    }
}
