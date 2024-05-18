import jpcap.NetworkInterface;

import java.util.ArrayList;

public class Configuration{
    public static final Object lock = new Object();
    public static boolean status = true;
    public static Integer time = 0;
    public static ArrayList<Integer> numTime = new ArrayList<>(3600);
    public static NetworkInterface selectedDevice;
    public static NetworkInterface[] devices;
    public static ArrayList<String> devicesString;
    public static int ALLcnt = 0;
    public static int ARPcnt = 0;
    public static int UDPcnt = 0;
    public static int TCPcnt = 0;
    public static int ICMPcnt = 0;
    public static void interfaceToString(){
        devicesString = new ArrayList<String>();
        for(NetworkInterface device:devices){
            devicesString.add(device.description + "    " + "MAC: " + NetPacket.bytesToHex(device.mac_address));
        }
    }
}
