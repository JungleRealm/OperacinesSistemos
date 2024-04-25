import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RealMachine {

    private static final int BlockSize = 16;
    private static final int Words = 16;
    private static final int WordSize = 4;

    private static final int TIMER_DEFAULT_VALUE = 20;
    private static final int SI_DEFAULT_VALUE = 0;
    private static final int PI_DEFAULT_VALUE = 0;

    private static int SI = SI_DEFAULT_VALUE;
    private static int PI = PI_DEFAULT_VALUE;
    private static int TI = TIMER_DEFAULT_VALUE;

    // 0 - SUPERVISOR
    // 1 - USER
    private static int MODE = 0;
    private static String selectedFile;

    private static final boolean STEPPING_DEFAULT_VALUE = false;
    private static boolean stepping = STEPPING_DEFAULT_VALUE;

    public static boolean DEFAULT_stopRunning_VALUE = false;
    private static boolean stopRunning =  DEFAULT_stopRunning_VALUE;

    public RealMachine() throws IOException {
        while(true) {
            userInput();

            if (mount()) {
                MODE = 1;
                int supervisorMemoryPointer = Supervisor.getPtr();

                MODE = 0;
                int realMemoryPointer = RealMemory.requestMemory(FlashDrive.getShift());
                MODE = 1;
                if (realMemoryPointer >= RealMemory.getPtr()) {

                    System.out.println("Moving data from Flash Memory to Real Memory");
                    for (int i = 0; i < FlashDrive.getShift(); i++) {
                        ChannelDevice.xchg("FlashDrive", "RealMemory", FlashDrive.getPtr() + i, realMemoryPointer + i);
                    }
                    System.out.println("Data successfully moved to Real Memory");
                    FlashDrive.clearByteMemory();

                    selectedFile = selectFile(getFileNamesFromRealMemory());
                    MODE = 0;
                    supervisorMemoryPointer = Supervisor.requestMemory(RealMemory.getFileSize(selectedFile));
                    MODE = 1;
                    if (supervisorMemoryPointer >= Supervisor.getPtr()) {
                        System.out.println("Moving data from Real Memory to Supervisor Memory");
                        for (int i = 0; i < RealMemory.getFileSize(selectedFile); i++) {
                            ChannelDevice.xchg("RealMemory", "SupervisorMemory", RealMemory.getFileStartPointer(selectedFile) + i, supervisorMemoryPointer + i);
                        }
                        System.out.println("Success moving data from Real Memory to Supervisor Memory");
                        System.out.println("Checking if syntax is correct");
                        if (!checkFileSyntax()) {
                            PI = 3;
                            interruptHandler();
                            break;
                        }
                    } else {
                        PI = 4;
                        interruptHandler();
                        break;
                    }
                } else {
                    MODE = 0;
                    int externalMemoryPointer = ExternalMemory.requestMemory(FlashDrive.getShift());
                    MODE = 1;

                    if (externalMemoryPointer < ExternalMemory.getPtr()){
                        PI = 3;
                        interruptHandler();
                        if (stopRunning){
                            break;
                        }
                    }

                    System.out.println("Moving data from Flash Memory to External Memory");
                    for (int i = 0; i < FlashDrive.getShift(); i++) {
                        ChannelDevice.xchg("FlashDrive", "ExternalMemory", FlashDrive.getPtr() + i, externalMemoryPointer + i);
                    }
//                    ExternalMemory.printMemory();
                    System.out.println("Data successfully moved to External Memory");
                    FlashDrive.clearByteMemory();

                    selectedFile = selectFile(getFileNamesFromExternalMemory());
                    MODE = 0;
                    supervisorMemoryPointer = Supervisor.requestMemory(ExternalMemory.getFileSize(selectedFile));
                    MODE = 1;
                    if (supervisorMemoryPointer >= Supervisor.getPtr()) {
                        System.out.println("Moving data from External Memory to Supervisor Memory");
                        for (int i = 0; i < ExternalMemory.getFileSize(selectedFile); i++) {
                            ChannelDevice.xchg("ExternalMemory", "SupervisorMemory", ExternalMemory.getFileStartPointer(selectedFile) + i, supervisorMemoryPointer + i);
                        }
                        System.out.println("Success moving data from External Memory to Supervisor Memory");
                        System.out.println("Checking if syntax is correct");
                        if (!checkFileSyntax()) {
                            PI = 3;
                            interruptHandler();
                            break;
                        }
                    } else {
                        PI = 4;
                        interruptHandler();
                        break;
                    }
                }
                System.out.println("Syntax is correct");
                System.out.println("Requesting memory for Virtual Machine from Real Memory");
                int virtualMachinePTR = RealMemory.requestMemory(VirtualMachine.getSize());
                if (virtualMachinePTR >= RealMemory.getPtr()) {
                    System.out.println("Memory acquired");
                    VirtualMachine virtualMachine = new VirtualMachine(virtualMachinePTR);
                    System.out.println("Virtual Machine created");
                    System.out.println("Initialising data transfer from Supervisor to Virtual Machine");
                    for (int i = 0; i < RealMemory.getFileSize(selectedFile); i++) {
                        ChannelDevice.xchg("SupervisorMemory", "VirtualMemory", supervisorMemoryPointer+RealMemory.getFileSize(selectedFile)-i-1, virtualMachinePTR + i);
                    }
                    System.out.println("Success moving data from Real Memory to Supervisor Memory");

                    Supervisor.clearMemory();

                    virtualMachine.setStackPointer(virtualMachinePTR + RealMemory.getFileSize(selectedFile) - 1);
                    virtualMachine.setResultPtr(virtualMachine.getStackPointer()+1);
                    virtualMachine.setResultStackPointer(virtualMachine.getResultPtr());

                    doesUserWantStepping();
                    virtualMachine.exec();
                    virtualMachine.clearMemory();
                } else {
                    PI = 3;
                    interruptHandler();
                }
                FlashDrive.clearByteMemory();
                Supervisor.clearMemory();
                RealMemory.clearMemory();
                ExternalMemory.clearMemory();
                resetStepping();
                resetStopRunning();
                System.out.println("-------------------------");

            }
        }
    }

    public static boolean getStopRunning(){
        return stopRunning;
    }

    public static void resetStopRunning(){
        stopRunning = DEFAULT_stopRunning_VALUE;
    }

    public static boolean getStepping(){
        return stepping;
    }

    public static void resetStepping(){
        stepping = STEPPING_DEFAULT_VALUE;
    }

    public static void doesUserWantStepping(){
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("Would you like to enable stepping? [YES/NO]");
        while(true){
            input = scanner.nextLine();
            if (input.equals("YES")){
                stepping = true;
                break;
            } else if (input.equals("NO")){
                stepping = false;
                break;
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    public static boolean mount() throws IOException {

        File[] roots = File.listRoots();
        boolean eDriveExists = false;
        for (File root : roots) {
            if (root.getAbsolutePath().startsWith("E:")) {
                eDriveExists = true;
                break;
            }
        }
        if (eDriveExists) {
            System.out.println("E flash drive is inserted");
            FlashDrive.load();

            return true;
        } else {
            System.out.println("E flash drive is not inserted");
            return false;
        }
    }

    public static void userInput(){
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("Click N to continue...");
        while (true){
            input = scanner.nextLine();
            if (input.equals("N")){
                break;
            }
        }
    }

    public static String selectFile(ArrayList<String> fileNames){
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("------------------------");
        System.out.println("Select which file to run");
        System.out.println("Found files: " + fileNames);
        while(true){
            input = scanner.nextLine();
            if (fileNames.contains(input)){
                return input;
            }
            System.out.println("Name not recognised");
        }
    }

    public static ArrayList<String> getFileNamesFromRealMemory(){
        ArrayList<String> fileNames = new ArrayList<>();
        StringBuilder characters = new StringBuilder();
        for (int i = RealMemory.getPtr(); i < RealMemory.getMemorySize(); i++){
            if ((char) RealMemory.getData(i) == '-' && (char) RealMemory.getData(i+1) == '-' && (char) RealMemory.getData(i+2) == '-' && (char) RealMemory.getData(i+3) == '-' ){
                i = i + 4;
                int innerCounter = i;
                if (RealMemory.getData(innerCounter + 1) != 0){
                    while((char) RealMemory.getData(innerCounter) != '$'){
                        characters.append((char) RealMemory.getData(innerCounter));
                        innerCounter++;
                        i++;
                    }
                    fileNames.add(characters.toString());
                    characters.setLength(0);
                }
            }
        }
        return fileNames;
    }

    public static ArrayList<String> getFileNamesFromExternalMemory(){
        ArrayList<String> fileNames = new ArrayList<>();
        StringBuilder characters = new StringBuilder();
        for (int i = ExternalMemory.getPtr(); i < ExternalMemory.getMemorySize(); i++){
            if ((char) ExternalMemory.getData(i) == '-' && (char) ExternalMemory.getData(i+1) == '-' && (char) ExternalMemory.getData(i+2) == '-' && (char) ExternalMemory.getData(i+3) == '-' ){
                i = i + 4;
                int innerCounter = i;
                if (ExternalMemory.getData(innerCounter + 1) != 0){
                    while((char) ExternalMemory.getData(innerCounter) != '$'){
                        characters.append((char) ExternalMemory.getData(innerCounter));
                        innerCounter++;
                        i++;
                    }
                    fileNames.add(characters.toString());
                    characters.setLength(0);
                }
            }
        }
        return fileNames;
    }


    public static int getWordSize() {
        return WordSize;
    }

    public static int getWords() {
        return Words;
    }

    public static int getBlockSize() {
        return BlockSize;
    }

    public static void interruptHandler(){
        switch (SI){
            case 1:
                MODE = 0;
                System.out.println("SI was found as 1");
                resetPI();
                break;
            case 2:
                MODE = 0;
                System.out.println("SI was found as 2");
                resetPI();
                break;
            case 3:
                MODE = 0;
                System.out.println("SI was found as 3");
                resetPI();
                break;
        }

        switch (PI){
            case 1:
                // INVALID ADDRESS
                MODE = 0;
                System.out.println("PI was found as 1");
                System.out.println("INVALID ADDRESS ENCOUNTERED");
                resetPI();
                stopRunning = true;
                break;
            case 2:
                // UNRECOGNISED COMMAND
                MODE = 0;
                System.out.println("PI was found as 2");
                System.out.println("UNRECOGNISED COMMAND ERROR");
                resetPI();
                stopRunning = true;
                break;
            case 3:
                // NOT ENOUGH MEMORY IN REAL MEMORY AND EXTERNAL MEMORY
                MODE = 0;
                System.out.println("PI Interrupt occurred");
                System.out.println("NOT ENOUGH MEMORY IN REAL MEMORY OR EXTERNAL MEMORY TO STORE FILE");
                resetPI();
                stopRunning = true;
                break;
            case 4:
                // NOT ENOUGH MEMORY IN SUPERVISOR
                MODE = 0;
                System.out.println("PI Interrupt occurred");
                System.out.println("NOT ENOUGH MEMORY IN SUPERVISOR MEMORY");
                resetPI();
                stopRunning = true;
                break;
            case 5:
                // HALT INTERRUPT
                MODE = 1;
                System.out.println("PI Interrupt occurred");
                System.out.println("HALT INTERRUPT");
                resetPI();
                stopRunning = true;
                break;
            case 6:
                // $END INTERRUPT
                MODE = 1;
                System.out.println("PI Interrupt occurred");
                System.out.println("$END INTERRUPT");
                resetPI();
                stopRunning = true;
                break;
        }
        if (TI == 0){
            // TIMER INTERRUPT
            MODE = 0;
            System.out.println("Timer interrupt has occurred");
            resetTimer();
        }

    }

    public static void resetSI(){
        SI = SI_DEFAULT_VALUE;
    }

    public static void resetPI(){
        PI = PI_DEFAULT_VALUE;
    }

    public static void resetTimer(){
        TI = TIMER_DEFAULT_VALUE;
    }

    public static int getSI() {
        return SI;
    }

    public static int getPI() {
        return PI;
    }

    public static int getMODE() {
        return MODE;
    }

    public static int getTI() {
        return TI;
    }

    public static void decrementTI(){
        TI--;
    }

    public static boolean checkFileSyntax(){
        String command;
        for(int i = 0; i < Supervisor.getSupervisorSize(); i = i + WordSize){
            if (Supervisor.getData(i) == 0 && Supervisor.getData(i+1) == 0 && Supervisor.getData(i+2) == 0 && Supervisor.getData(i+3) == 0){
                break;
            }
            command = cropCommand(Supervisor.getData(i), Supervisor.getData(i+1), Supervisor.getData(i+2), Supervisor.getData(i+3));
            int commandShift = recogniseCommand(command);
            if (commandShift < 0){
                PI = 2;
                return false;
            } else {
                i = i + commandShift;
            }

        }
        return true;
    }

    public static String cropCommand(byte character1, byte character2, byte character3, byte character4){
        StringBuilder command = new StringBuilder();
        if (character1 == 0){
            if (character2 == 0){
                if (character3 == 0){
                    if (character4 == 0){
                        return null;
                    } else {
                        command.append((char) character4);
                    }
                } else {
                    command.append((char) character3);
                    command.append((char) character4);
                }
            } else {
                command.append((char) character2);
                command.append((char) character3);
                command.append((char) character4);
            }
        } else {
            command.append((char) character1);
            command.append((char) character2);
            command.append((char) character3);
            command.append((char) character4);
        }
        return command.toString();
    }

    public static int recogniseCommand(String command){
        switch (command){
            case "$STR":
//                System.out.println("$STR found");
                return 0;
            case "PUSH":
//                System.out.println("PUSH found");
                return WordSize;
            case "POP":
//                System.out.println("POP found");
                return 0;
            case "ADD":
//                System.out.println("ADD found");
                return 0;
            case "SUB":
//                System.out.println("SUB found");
                return 0;
            case "MUL":
//                System.out.println("MUL found");
                return 0;
            case "DIV":
//                System.out.println("DIV found");
                return 0;
            case "JB":
//                System.out.println("JB found");
                return 0; // TODO ASK ABOUT WHERE TO JUMP
            case "JA":
//                System.out.println("JA found");
                return 0; // TODO ASK ABOUT WHERE TO JUMP
            case "JZ":
//                System.out.println("JZ found");
                return 0; // TODO ASK ABOUT WHERE TO JUMP
            case "PRTN":
//                System.out.println("PRTN found");
                return 0;
            case "FORK":
//                System.out.println("FORK found");
                return 0;
            case "CMP":
//                System.out.println("CMP found");
                return 0;
            case "SWP":
//                System.out.println("SWP found");
                return 0;
            case "INC":
//                System.out.println("INC found");
                return 0;
            case "DEC":
//                System.out.println("DEC found");
                return 0;
            case "HALT":
//                System.out.println("HALT found");
                return 0;
            case "$END":
//                System.out.println("$END found");
                return 0;
        }
        return -1;
    }

    public static void setPI(int PI) {
        RealMachine.PI = PI;
    }

    public static void setMODE(int MODE) {
        RealMachine.MODE = MODE;
    }

    public static void setSI(int SI) {
        RealMachine.SI = SI;
    }

    public static void setTI(int TI) {
        RealMachine.TI = TI;
    }
}