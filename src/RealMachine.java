import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RealMachine {
    private MemoryManagementUnit memoryManagementUnit;
    private RealMemory realMemory;
    private RealMemory externalMemory;
    private CPU cpu;
    private InputDevice inputDevice;
    private OutputDevice outputDevice;

    public final static int SWAP_SIZE = 2; // blocks
    public final static int PAGE_TABLE_SIZE = 160; // words
    public final static int USER_MEMORY_SIZE = 6; //blocks

    private int supervisorSize = 206; // blocks

    private int vmTableAddress = 0;
    private int pageTableAddress = 1280;
    private int userMemoryAllocTableAdress = 52480;
    private int swapMemoryAllocTableAdress = 52488;
    private int swapFileAddress = 52490;
    private int swapTempMemAddress = 52491;
    private int vmCountAddress = 52492;
    private int vmIdAddress = 52493;

    public RealMachine() {
        cpu = new CPU();
        realMemory = new RealMemory(MemoryManagementUnit.BLOCKSIZE*(supervisorSize+USER_MEMORY_SIZE));
        memoryManagementUnit = new MemoryManagementUnit(cpu, realMemory, this, MemoryManagementUnit.BLOCKSIZE*SWAP_SIZE);
        cpu.setMemoryManagementUnit(memoryManagementUnit);
        externalMemory = new RealMemory(MemoryManagementUnit.BLOCKSIZE*SWAP_SIZE);
        inputDevice = new InputDevice();
        outputDevice = new OutputDevice();
    }


    //FIXME: handle LoaderException inside. deleteVM if load failed
    public int loadProgram(String fileName) throws MemoryException, LoaderParseException {
        int vmIndex = newVM();
        try {
            Loader loader = new Loader();
            VirtualMachine vm = getVM(vmIndex);
            cpu.setMODE(CPU.USER);
            cpu.setPTR(vm.getPTR());
            vm = loader.load(this, memoryManagementUnit, new File(fileName), vm);
            updateVM(vmIndex, vm);
            return vmIndex;
        } catch (LoaderParseException ex) {
            deleteVM(vmIndex);
            throw ex;
        }
    }

    public void unloadProgram(int index) throws MemoryException {
        memoryManagementUnit.freeMemory(getVM(index).getPTR());
        deleteVM(index);
    }

    public void runProgram() {
        while(true) {
            cpu.setMODE(CPU.USER);
            while(cpu.getInterrupt() == 0) {
                cpu.doCycle();
            }
            cpu.setMODE(CPU.SUPERVISOR);
            if(processInterrupt() == 1) {
                break;
            }
        }
    }

    public boolean selectProgram(int index) {
        try {
            return loadVM(index) == 1;
        } catch (MemoryException ex) {
            Logger.getLogger(RealMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int nextStep() {
        cpu.setMODE(CPU.USER);
        cpu.doCycle();
        cpu.setMODE(CPU.SUPERVISOR);
        return processInterrupt();
    }

    private int processInterrupt() {
            /* 
            1 - neteisingas adresas
            2 - neegzistuoja operacijos kodas
            3 - nepakanka atminties isoriniame diske
            4 - komanda GETD
            5 - PUTD
            6 - HALT
            7 - taimerio
            */
        try {
            while(cpu.getInterrupt() != 0) {
                switch(cpu.getInterrupt()) {
                    case 1: {
                        outputDevice.putString("Out of bounds");
                        cpu.resetInterrupts();
                        return 1;
                    }
                    case 2: {
                        outputDevice.putString("Invalid command");
                        cpu.resetInterrupts();
                        return 1;
                    }
                    case 3: {
                        outputDevice.putString("Not enough external disk space");
                        cpu.resetInterrupts();
                        return 1;
                    }
                    case 4: {
                        getd();
                        break;
                    }
                    case 5: {
                        putd();
                        break;
                    }
                    case 6: {
                        cpu.resetInterrupts();
                        return 1;
                    }
                    case 7: {
                        cpu.resetTI();
                        break;
                    }
                    case 8: {
                        executeSwap();
                        cpu.setSI(0);
                        break;
                    }
                }
            }
        }catch (MemoryException ex) {
            ex.printStackTrace();
            throw new Error();
        }
        return 0;
    }
    public void executeSwap() throws MemoryException {
        int lastMode = cpu.getMODE();
        cpu.setMODE(CPU.SUPERVISOR);
        int from = memoryManagementUnit.getBlockNrFromSwap();
        int to = memoryManagementUnit.getBlockNrToSwap();
        from = Word.wordToInt(memoryManagementUnit.read(swapFileAddress))+ from * MemoryManagementUnit.BLOCKSIZE;
        to = to * MemoryManagementUnit.BLOCKSIZE;
//        cpu.setCH3(1);
        for (int i = 0; i < MemoryManagementUnit.BLOCKSIZE; i++) {
            memoryManagementUnit.write(externalMemory.read(from+i), swapTempMemAddress);
            externalMemory.write(memoryManagementUnit.read(to+i), from+i);
            memoryManagementUnit.write(memoryManagementUnit.read(swapTempMemAddress), to+i);
        }
//        cpu.setCH3(0);
        memoryManagementUnit.updateTablesAfterSwap();
        cpu.setMODE(lastMode);
    }

    private void getd() throws MemoryException {
        int lastMode = cpu.getMODE();
        cpu.setMODE(CPU.USER);
        cpu.setSI(0);
        Word w_address;
        try {
            w_address = memoryManagementUnit.read(cpu.getPC()-1);
        } catch (BlockSwapException ex) {
            executeSwap();
            w_address = memoryManagementUnit.read(cpu.getPC()-1);
        }
        w_address.setByte(3, (byte) 0);
        int address = Word.wordToInt(w_address);
//        cpu.setCH1(1);
        Word[] input = inputDevice.getInput();
//        cpu.setCH1(0);
        for (Word w : input) {
            try {
                memoryManagementUnit.write(w, address);
            } catch (BlockSwapException ex) {
                executeSwap();
                memoryManagementUnit.write(w, address);
            }
            address++;
        }
        cpu.setMODE(lastMode);
    }

    private void putd() throws MemoryException {
        int lastMode = cpu.getMODE();
        cpu.setMODE(CPU.USER);
        cpu.setSI(0);
        Word w;
        try {
            w = memoryManagementUnit.read(cpu.getPC()-1);
        } catch (BlockSwapException ex) {
            executeSwap();
            w = memoryManagementUnit.read(cpu.getPC()-1);
        }
        w.setByte(3, (byte) 0);
        int address = Word.wordToInt(w);
//        cpu.setCH2(1);
        int i = 0;
        boolean null_byte = false;
        while(i <= 255 && !null_byte) {
            try {
                w = memoryManagementUnit.read(address + i);
            } catch (BlockSwapException ex) {
                executeSwap();
                w = memoryManagementUnit.read(address + i);
            }
            for (int j = 3; j >= 0; j--) {
                if (w.getByte(j) != 0) {
                    outputDevice.putByte(w.getByte(j));
                }
                else {
                    null_byte = true;
                    break;
                }
            }
            i++;
        }
//        cpu.setCH2(0);
        cpu.setMODE(lastMode);
    }

    public VirtualMachine getVM(int index) {
        try {
            int lastMode = cpu.getMODE();
            cpu.setMODE(CPU.SUPERVISOR);
            int loaded = Word.wordToInt(memoryManagementUnit.read(vmTableAddress + index*4));
            int pc = Word.wordToInt(memoryManagementUnit.read(vmTableAddress + (index * 4) + 1));
            int sp = Word.wordToInt(memoryManagementUnit.read(vmTableAddress + (index * 4) + 2));
            int ptr = Word.wordToInt(memoryManagementUnit.read(vmTableAddress + (index * 4) + 3));
            cpu.setMODE(lastMode);
            return new VirtualMachine(loaded, pc, sp, ptr);
        } catch (MemoryException ex) {
            throw new Error();
        }
    }

    private int newVM() throws MemoryException {
        int lastMode = cpu.getMODE();
        cpu.setMODE(CPU.SUPERVISOR);
        int i = 0;
        while (Word.wordToInt(memoryManagementUnit.read(vmTableAddress + i*4)) == 1) {
            i++;
        }
        int ptr = i*PAGE_TABLE_SIZE + pageTableAddress;
        updateVM(i, new VirtualMachine(1, 0, 0, ptr));
        memoryManagementUnit.write(Word.intToWord(Word.wordToInt(memoryManagementUnit.read(vmCountAddress))+1), vmCountAddress);
        cpu.setMODE(lastMode);
        return i;
    }

    private int loadVM(int i) throws MemoryException {
        int lastMode = cpu.getMODE();
        cpu.setMODE(CPU.SUPERVISOR);
        int loaded = Word.wordToInt(memoryManagementUnit.read(vmTableAddress + (i * 4)));
        if(loaded == 1) {
            cpu.setPC(Word.wordToInt(memoryManagementUnit.read(vmTableAddress + (i * 4) + 1)));
            cpu.setSP(Word.wordToInt(memoryManagementUnit.read(vmTableAddress + (i * 4) + 2)));
            cpu.setPTR(Word.wordToInt(memoryManagementUnit.read(vmTableAddress + (i * 4) + 3)));
            memoryManagementUnit.write(Word.intToWord(i), vmIdAddress);
        }
        cpu.setMODE(lastMode);
        return loaded;
    }

    private void deleteVM(int index) throws MemoryException {
        int lastMode = cpu.getMODE();
        cpu.setMODE(CPU.SUPERVISOR);
        if (Word.wordToInt(memoryManagementUnit.read(vmTableAddress + (index * 4))) == 1) {
            memoryManagementUnit.write(Word.intToWord(0), vmTableAddress + (index * 4));
            memoryManagementUnit.write(Word.intToWord(Word.wordToInt(memoryManagementUnit.read(vmCountAddress))-1), vmCountAddress);
        }
        cpu.setMODE(lastMode);
    }

    public void updateVM(int index, VirtualMachine vm){
        try {
            int lastMode = cpu.getMODE();
            cpu.setMODE(CPU.SUPERVISOR);
            memoryManagementUnit.write(Word.intToWord(vm.getLoaded()), vmTableAddress + (index * 4));
            memoryManagementUnit.write(Word.intToWord(vm.getPC()), vmTableAddress + (index * 4) + 1);
            memoryManagementUnit.write(Word.intToWord(vm.getSP()), vmTableAddress + (index * 4) + 2);
            memoryManagementUnit.write(Word.intToWord(vm.getPTR()), vmTableAddress + (index * 4) + 3);
            cpu.setMODE(lastMode);
        } catch (MemoryException ex) {
            throw new Error();
        }
    }

    int getVMCount() {
        try {
            int lastMode = cpu.getMODE();
            cpu.setMODE(CPU.SUPERVISOR);
            int vmCount = Word.wordToInt(memoryManagementUnit.read(vmCountAddress));
            cpu.setMODE(lastMode);
            return vmCount;
        } catch (MemoryException ex) {
            throw new Error();
        }
    }

    public int getSupervisorSize() {
        return supervisorSize * MemoryManagementUnit.BLOCKSIZE; //words
    }

    public int getUserAllocTableAddress() {
        return userMemoryAllocTableAdress;
    }

    public int getSwapAllocTableAddress() {
        return swapMemoryAllocTableAdress;
    }

    public Word[] viewRealMemory(){
        return realMemory.viewData();
    }
    public Word[] viewExternalMemory(){
        return externalMemory.viewData();
    }

    public CPU getCPU() {
        return cpu;
    }

}