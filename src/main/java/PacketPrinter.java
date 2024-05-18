import jpcap.PacketReceiver;
import jpcap.packet.*;

import java.time.Duration;
import java.time.Instant;

public class PacketPrinter implements PacketReceiver {
    Instant startTime = Instant.now();
    Instant endTime;
    Duration elapsedTime;
    public void receivePacket(Packet packet) {
        if(!Configuration.status || packet == null){
            return;
        }
        IPPacket ipPacket = null;
        EthernetPacket ethernetPacket = (EthernetPacket) packet.datalink;
        TCPPacket tcpPacket = null;
        UDPPacket udpPacket = null;
        ARPPacket arpPacket = null;
        ICMPPacket icmpPacket = null;

        String time;
        int timeInt;
        String sourceIp = null,destinationIp = null,sourceMac = null,destinationMac = null,protocol = null,data = null,info = null;
        int length = 0;

        sourceMac = ethernetPacket.getSourceAddress();
        destinationMac = ethernetPacket.getDestinationAddress();

        if (packet instanceof IPPacket) {
            ipPacket = (IPPacket) packet;
            sourceIp = ipPacket.src_ip.getHostAddress();
            destinationIp = ipPacket.dst_ip.getHostAddress();
            switch (ipPacket.protocol){
                case NetPacket.IPPROTO_ICMP:
                    protocol = "ICMP";
                    break;
                case NetPacket.IPPROTO_IGMP:
                    protocol = "IGMP";
                    break;
                case NetPacket.IPPROTO_IP:
                    protocol = "IP";
                    break;
                case NetPacket.IPPROTO_TCP:
                    protocol = "TCP";
                    break;
                case NetPacket.IPPROTO_UDP:
                    protocol = "UDP";
                    break;
                case NetPacket.IPPROTO_IPv6:
                    protocol = "IPv6";
                    break;
                case NetPacket.IPPROTO_HOPOPT:
                    protocol = "HOPOPT";
                    break;
                case NetPacket.IPPROTO_IPv6_Route:
                    protocol = "IPv6_Route";
                    break;
                case NetPacket.IPPROTO_IPv6_Frag:
                    protocol = "IPv6_Frag";
                    break;
                case NetPacket.IPPROTO_IPv6_ICMP:
                    protocol = "IPv6_ICMP";
                    break;
                default:
                    protocol = "UNKNOWN";
            }
            length = ((IPPacket) packet).length;
        }
        if(packet instanceof ARPPacket){
            //ARP包
            arpPacket = (ARPPacket) packet;
            sourceIp = arpPacket.getSenderProtocolAddress().toString().replaceFirst("^/", "");
            destinationIp = arpPacket.getTargetProtocolAddress().toString().replaceFirst("^/", "");
            protocol = "ARP";
            length = arpPacket.caplen;
            info = arpPacket.toString();
            Configuration.ARPcnt ++;
        }
        if(packet instanceof TCPPacket){
            //TCP包
            tcpPacket = (TCPPacket) packet;
            info = tcpPacket.toString();
            Configuration.TCPcnt ++;
        }
        if(packet instanceof UDPPacket){
            //UDP包
            udpPacket = (UDPPacket) packet;
            info = udpPacket.toString();
            Configuration.UDPcnt ++;
        }
        if(packet instanceof ICMPPacket){
            //ICMP包
            icmpPacket = (ICMPPacket) packet;
            info = icmpPacket.toString();
            Configuration.ICMPcnt ++;
        }
        endTime = Instant.now();
        elapsedTime = Duration.between(startTime,endTime);
        timeInt = (int)elapsedTime.toSeconds();
        while (Configuration.numTime.size() <= timeInt) {
            Configuration.numTime.add(0);
            Configuration.time++;
        }
        Configuration.numTime.set(timeInt,Configuration.numTime.get(timeInt) + 1);
        time = String.valueOf(elapsedTime.toSeconds())+"."+String.valueOf(elapsedTime.toNanosPart());
        data = NetPacket.bytesToHex(packet.data);
        PacketDataArray.packetDataArray.put(new PacketData(Configuration.ALLcnt++,time,sourceMac,destinationMac,sourceIp,destinationIp,protocol,data,length,info));
    }
}
