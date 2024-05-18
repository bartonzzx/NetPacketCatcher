import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Array;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.LinkedTransferQueue;

public class NetPacket extends Thread{
    private Thread t;
    public static final short IPPROTO_ICMP = 1;
    public static final short IPPROTO_IGMP = 2;
    public static final short IPPROTO_IP = 4;
    public static final short IPPROTO_TCP = 6;
    public static final short IPPROTO_UDP = 17;
    public static final short IPPROTO_IPv6 = 41;
    public static final short IPPROTO_HOPOPT = 0;
    public static final short IPPROTO_IPv6_Route = 43;
    public static final short IPPROTO_IPv6_Frag = 44;
    public static final short IPPROTO_IPv6_ICMP = 58;
    @Override
    public void run() {
        JpcapCaptor jpcap = null;
        System.out.println("选择的网卡为：" + Configuration.selectedDevice.name + " " + Configuration.selectedDevice.description);
        try {
            jpcap = JpcapCaptor.openDevice(Configuration.selectedDevice, 1512, true, 1000);// 捕获时间为1s
        } catch (IOException e) {
            System.out.println("抓取数据包时出现异常!!");
        }
        assert jpcap != null;
        jpcap.loopPacket(-1, new PacketPrinter());
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        int count = 0; // 用于计数，每两个16进制数添加空格，每八个16进制数添加换行符
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex).append(' ');

            // 在每两个16进制数之后添加空格
            count++;
            if (count == 2) {
                hexString.append(' ');
                count = 0;
            }

            // 在每八个16进制数之后添加换行符
            if (count == 8) {
                hexString.append('\n');
                count = 0;
            }
        }
        return hexString.toString().toUpperCase();
    }

    public void start() {
        if(t == null)
            t = new Thread(this, "NetPacket");
        t.start();
    }
}