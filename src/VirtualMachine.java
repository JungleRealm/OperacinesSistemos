public class VirtualMachine {
    private int loaded;
    private int pc;
    private int sp;
    private int ptr;

    public VirtualMachine(int loaded, int pc, int sp, int ptr){
        this.loaded = loaded;
        this.pc = pc;
        this.sp = sp;
        this.ptr = ptr;
    }


    public int getPTR() {
        return ptr;
    }

    public int getLoaded() {
        return loaded;
    }
    public int getPC() {
        return pc;
    }
    public int getSP() {
        return sp;
    }

    public void setPC(int pc) {
        this.pc = pc;
    }

    public void setSP(int sp) {
        this.sp = sp;
    }

    public void setPTR(int ptr){
        this.ptr = ptr;
    }

}