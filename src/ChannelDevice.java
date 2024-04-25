public class ChannelDevice {
    public static void xchg(String from, String to, int fromStart, int toStart){
        if (from.equals("FlashDrive") || from.equals("RealMemory") || from.equals("ExternalMemory") || from.equals("SupervisorMemory") || from.equals("VirtualMemory")){
            if (to.equals("RealMemory") || to.equals("ExternalMemory") || to.equals("SupervisorMemory") || to.equals("VirtualMemory") || to.equals("PrinterMemory")){

                if (from.equals("FlashDrive") && to.equals("RealMemory")){
                    RealMemory.write(toStart, FlashDrive.getData(fromStart));
                }
                if (from.equals("FlashDrive") && to.equals("ExternalMemory")){
                    ExternalMemory.write(toStart, FlashDrive.getData(fromStart));
                }
                if (from.equals("RealMemory") && to.equals("SupervisorMemory")){
                    Supervisor.write(toStart, RealMemory.getData(fromStart));
                }
                if (from.equals("ExternalMemory") && to.equals("SupervisorMemory")){
                    Supervisor.write(toStart, ExternalMemory.getData(fromStart));
                }
                if (from.equals("SupervisorMemory") && to.equals("VirtualMemory")){
                    RealMemory.write(toStart, Supervisor.getData(fromStart));
                }
                if (from.equals("VirtualMemory") && to.equals("SupervisorMemory")){
                    Supervisor.write(toStart, RealMemory.getData(fromStart));
                }
                if (from.equals("VirtualMemory") && to.equals("PrinterMemory")){
                    Printer.write(toStart, RealMemory.getData(fromStart));
                }
            } else {
                System.out.println("Channel Device could not recognise where data should be moved");
            }
        } else {
            System.out.println("Channel Device could not recognise from which location data should be taken");
        }
    }
}