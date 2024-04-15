public class CPU {

    public static final int SUPERVISOR = 0;
    public static final int User = 1;

    private static int MODE = SUPERVISOR; // User or Supervizor
    private static int PTR; //Pointer register
    private static int PC; //Komandos skaitliukas. It keeps track of the memory address of the next instruction to be fetched and executed
    private static int TI = 20; //Taimeris. Kas nurodyta laika generuoja interuptus
    private static int SP = Supervizor.getFullSupervizorMemory(); //Steko virsunes indeksas. It points to the top of the stack in memory.

    private static int SI = 0;
    private static int PI = 0;

    //TODO create a method that checks interrupts and returns them to RealMachine for handling

    public static void resetTI(){
        TI = 20;
    }

    public static void resetInterrupts(){
        SI = 0;
        PI = 0;
        resetTI();
    }

    public static void run(int pointerToStart, int pointerToEnd){

        SP = pointerToStart;
//        System.out.println(SP);

        for (int i = pointerToStart; i < pointerToEnd; i=i+RealMemory.getWordSize()){
            String command = getCommand();
//            System.out.println(command);
            if (command != null){
                interpretCommand(getCommand());
                SP = SP + RealMemory.getWordSize();
            } else {
                System.out.println("Illegal Argument encountered when fetching command");
                return;
            }

        }



    }

    public static String getCommand(){
        byte character1 = RealMemory.getElementFromMemory(SP);
        byte character2 = RealMemory.getElementFromMemory(SP+1);
        byte character3 = RealMemory.getElementFromMemory(SP+2);
        byte character4 = RealMemory.getElementFromMemory(SP+3);
        return cropWord(character1, character2, character3, character4);
    }

    public static String cropWord(byte character1, byte character2, byte character3, byte character4){
        StringBuilder word = new StringBuilder();
        if (character1 == 0){
            if (character2 == 0){
                if (character3 == 0){
                    if (character4 == 0){
//                        System.out.println("Illegal Argument encountered when fetching command");
                        return null;
                    } else {
                        word.append((char) character4);
                    }
                } else {
                    word.append((char) character3);
                    word.append((char) character4);
                }
            } else {
                word.append((char) character2);
                word.append((char) character3);
                word.append((char) character4);
            }
        } else {
            word.append((char) character1);
            word.append((char) character2);
            word.append((char) character3);
            word.append((char) character4);
        }
        return word.toString();
    }

    //TODO finish interpret command method to do what the command should

    public static void interpretCommand(String command){
        switch (command){
            case "PUSH":
                System.out.println("PUSH");
                break;

            case "POP":
                System.out.println("POP");
                break;

            case "ADD":
                System.out.println("ADD");
                break;

            case "SUB":
                System.out.println("SUB");
                break;

            case "MUL":
                System.out.println("MUL");
                break;

            case "DIV":
                System.out.println("DIV");
                break;

            case "JB":
                System.out.println("JB");
                break;

            case "JA":
                System.out.println("JA");
                break;

            case "JZ":
                System.out.println("JZ");
                break;

            case "PUTD":
                System.out.println("PUTD");
                break;

            case "GETD":
                System.out.println("GETD");
                break;

            case "PRTN":
                System.out.println("PRTN");
                break;

            case "HALT":
                System.out.println("HALT");
                break;

            default:
                System.out.println("Non command object found: " + command);
        }
    }

    public static int getPI() {
        return PI;
    }

    public static int getSI() {
        return SI;
    }
}
