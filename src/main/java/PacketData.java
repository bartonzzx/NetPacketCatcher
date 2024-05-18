public class PacketData {
    public int num;
    public String time;
    public String sourceMAC;
    public String destinationMAC;
    public String sourceIP;
    public String destinationIP;
    public String protocol;
    public String data;
    public int length;
    public String info;
    public PacketData(int num,String time,String sourceMAC,String destinationMAC, String sourceIP, String destinationIP, String protocol, String data,int length,String info){
        this.num = num;
        this.time = time;
        this.sourceMAC = sourceMAC;
        this.destinationMAC = destinationMAC;
        this.sourceIP = sourceIP;
        this.destinationIP = destinationIP;
        this.protocol = protocol;
        this.data = data;
        this.length = length;
        this.info = info;
    }
}