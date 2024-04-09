import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CPU {

    public final static byte PUSH = 10; //"PUSH";
    public final static byte POP = 20; //"POP";
    public final static byte POP1 = 31; //"POP";
    public final static byte POP0 = 32; //"POP";
    public final static byte ADD = 50; //"ADD";
    public final static byte SUB = 60; //"SUB";
    public final static byte MUL = 70; //"MUL";
    public final static byte DIV = 80; //"DIV";

    public final static byte JB = 90; //"JB";
    public final static byte JA = 100; //"JA";
    public final static byte JZ = 110; //"JZ";

    public final static byte GETD = (byte) 120; //"GETD"; // LOADxy
    public final static byte PUTD = (byte) 130; //"PUTD";
    public final static byte HALT = (byte) 140; //"HALT";
    public final static byte PRTN = (byte) 150; //"HALT";


    private int MODE;
    private int PTR; //Pointer register
    private int PC; //Komandos skaitliukas. It keeps track of the memory address of the next instruction to be fetched and executed
    private int TI; //Taimeris. Kas nurodyta laika generuoja interuptus
    private int SP; //Steko virsunes indeksas. It points to the top of the stack in memory.
    private int SI; //It represents the program interrupt type. It can indicate errors or exceptional conditions encountered during program execution, such as invalid memory access or undefined instructions.
    private int PI; //It represents the program interrupt type. It can indicate errors or exceptional conditions encountered during program execution, such as invalid memory access or undefined instructions.

    private MemoryManagementUnit memoryManagementUnit;

    private static final int TIME = 20;
    public static final int SUPERVISOR = 0;
    public static final int USER = 1; //????

    public CPU() {
        MODE = SUPERVISOR; //Veikimo rezimas
        PC = 0; //Komandos skaitliukas
        SP = 0; //Steko virsunes indeksas
        TI = TIME; //Laiko sekimo registras
        SI = 0; //Source index ??????
        PI = 0; //Processor register ???
    }

    public void setMemoryManagementUnit(MemoryManagementUnit memoryManagementUnit) {
        this.memoryManagementUnit = memoryManagementUnit;
    }

    public void resetInterrupts() {
        resetTI();
        SI = 0;
        PI = 0;
    }

    public void doCycle() {
        try {
            interpretCommand(memoryManagementUnit.read(PC));
        } catch (Exception memoryException) {
            memoryException.printStackTrace();
            Logger.getLogger(CPU.class.getName()).log(Level.SEVERE, null, memoryException);
        }
        TI--;
    }

    /*
        Switch cases:
        PUSH - 1
        POP - 2
        POP1 - 3
        POP0 - 4
        ADD - 5
        SUB - 6
        MUL - 7
        DIV - 8
        JB - 9
        JA - 10
        JZ - 11
        GETD - 12 //Load
        PUTD - 13
        HALT - 14
        */
    private void interpretCommand(Word word) {
        PC++;
        byte command = word.getByte(3);
        word.setByte(3, (byte) 0);
        try {
            switch (command) {
                // PUSH xy
                case PUSH: {
                    System.out.println("PUSH");
                    memoryManagementUnit.write(memoryManagementUnit.read(Word.wordToInt(word)), SP);
                    SP++;
                    break;
                }
                // POP xy
                case POP: {
                    System.out.println("POP");
                    memoryManagementUnit.write(memoryManagementUnit.read(SP-1), Word.wordToInt(word));
                    SP--;
                    break;
                }
                // POP xy
                case POP1: {
                    System.out.println("POP1");
                    memoryManagementUnit.write(memoryManagementUnit.read(SP-1), Word.wordToInt(word));
                    SP--;
                    break;
                }
                // POP xy
                case POP0: {
                    System.out.println("POP0");
                    memoryManagementUnit.write(memoryManagementUnit.read(SP-1), Word.wordToInt(word));
                    SP--;
                    break;
                }
                // ADD
                case ADD: {
                    System.out.println("ADD");
                    memoryManagementUnit.write(Word.intToWord(Word.wordToInt(memoryManagementUnit.read(SP-2)) + Word.wordToInt(memoryManagementUnit.read(SP-1))), SP-2);
                    SP--;
                    break;
                }
                // SUB
                case SUB: {
                    System.out.println("SUB");
                    memoryManagementUnit.write(Word.intToWord(Word.wordToInt(memoryManagementUnit.read(SP-2)) - Word.wordToInt(memoryManagementUnit.read(SP-1))), SP-2);
                    SP--;
                    break;
                }
                // MUL
                case MUL: {
                    System.out.println("MUL");
                    memoryManagementUnit.write(Word.intToWord(Word.wordToInt(memoryManagementUnit.read(SP-2)) * Word.wordToInt(memoryManagementUnit.read(SP-1))), SP-2);
                    SP--;
                    break;
                }
                // DIV
                case DIV: {
                    System.out.println("DIV");
                    memoryManagementUnit.write(Word.intToWord(Word.wordToInt(memoryManagementUnit.read(SP-2)) / Word.wordToInt(memoryManagementUnit.read(SP-1))), SP-2);
                    SP--;
                    break;
                }
                // JB
                case JB: {
                    System.out.println("JB");
                    if(Word.wordToInt(memoryManagementUnit.read(SP-1)) < 0) {
                        PC = Word.wordToInt(word);
                    }
                    break;
                }
                // JA
                case JA: {
                    System.out.println("JA");
                    if(Word.wordToInt(memoryManagementUnit.read(SP-1)) > 0) {
                        PC = Word.wordToInt(word);
                    }
                    break;
                }
                //JZ
                case JZ: {
                    System.out.println("JZ");
                    if(Word.wordToInt(memoryManagementUnit.read(SP-1)) == 0) {
                        PC = Word.wordToInt(word);
                    }
                    break;
                }
                 //GETD xy
                case GETD: {
                    System.out.println("GETD");
                    SI = 1;
                    break;
                }
                // PUTD xy
                case PUTD: {
                    System.out.println("PUTD");
                    SI = 2;
                    break;
                }
                // HALT
                case HALT: {
                    System.out.println("HALT");
                    SI = 3;
                    break;
                }
                //PRTN
                case PRTN: {
                    System.out.println("PRTN");
                }
                default: {
                    System.out.println("DEFAULT: " + command);
                    PI = 2;
                    break;
                }
            }
        } catch (MemoryException memoryException) {
            PI = 1;
        } catch (Exception exception) {
            SI = 4;
            PC--;
            //NOTE: after swap we need to reexecute command
        }
    }


    /*
    1 - neteisingas adresas
    2 - neegzistuoja operacijos kodas
    3 - nepakanka atminties isoriniame diske
    4 - komanda GETD
    5 - PUTD
    6 - HALT
    7 - taimerio
    8 - swapinimo
    */
    public int getInterrupt() {
        switch (PI) {
            case 1: return 1;
            case 2: return 2;
            case 3: return 3;
        }
        switch(SI) {
            case 1: return 4;
            case 2: return 5;
            case 3: return 6;
            case 4: return 8;
        }
        if(TI == 0) {
            return 7;
        }
        return 0;
    }

    public void resetTI() {
        this.TI = TIME;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public int getPTR() {
        return PTR;
    }

    public void setPTR(int PTR) {
        this.PTR = PTR;
    }

    public int getSP() {
        return SP;
    }

    public void setSP(int SP) {
        this.SP = SP;
    }

    public int getTI() {
        return TI;
    }

    public void setTI(int TI) {
        this.TI = TI;
    }

    public int getSI() {
        return SI;
    }

    public void setSI(int SI) {
        this.SI = SI;
    }

    public int getPI() {
        return PI;
    }

    public void setPI(int PI) {
        this.PI = PI;
    }

    public int getMODE() {
        return MODE;
    }

    public void setMODE(int MODE) {
        this.MODE = MODE;
    }

}
