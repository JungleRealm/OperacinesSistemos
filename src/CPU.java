public class CPU {

    private static int ZF = 0;
    private static int CF = 0;

    public static int add(int operand1, int operand2){
        return operand1+operand2;
    }

    public static int sub(int operand1, int operand2){
        int result = operand1-operand2;
        if(result == 0){
            ZF = 1;
            return 0;
        }
        return result;
    }

    public static int mul(int operand1, int operand2){
        return operand1*operand2;
    }

    public static int div(int operand1, int operand2){
        return operand1/operand2;
    }

    public static int getCF() {
        return CF;
    }

    public static int getZF() {
        return ZF;
    }

    public static void resetCF(){
        CF = 0;
    }

    public static void resetZF(){
        ZF = 0;
    }

}
