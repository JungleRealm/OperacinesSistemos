package Main;

import Main.CPU;
import Main.RealMachine;
import Main.Word;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Path to the .txt file
        String filePath = "C:\\Projects\\OSMain\\files\\Test.txt";

        CPU cpu = new CPU();
        InputDevice inputDevice = new InputDevice(filePath);
        ArrayList<String> commandList = inputDevice.getInput();

        int i = 0;
        while(!commandList.isEmpty()){
            cpu.doCycle(commandList.get(i));
            commandList.remove(i);
        }


    }
}
