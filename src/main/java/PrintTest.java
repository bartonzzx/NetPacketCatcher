public class PrintTest extends Thread{
    private Thread t;
    Thread aLineChartPanel = null;
    Thread aPieChartPanel = null;
    public static int i = 0;
    @Override
    public void run() {
        while(Configuration.status){
            try {
                PacketData tempPacketData = PacketDataArray.packetDataArray.take();
                PacketDataArray.packetDataRandomArray.add(tempPacketData.data);
                PacketDataArray.packetInfoRandomArray.add(tempPacketData.info);
                String[] tempData = {String.valueOf(tempPacketData.num),tempPacketData.time,tempPacketData.sourceIP,tempPacketData.destinationIP,tempPacketData.protocol,String.valueOf(tempPacketData.length),tempPacketData.info};
                Interface.tableModel.addRow(tempData);
                Interface.table.scrollRectToVisible(Interface.table.getCellRect(Interface.table.getRowCount(), 0, true));
                aPieChartPanel = new Thread(new PieChartPanel());
                aPieChartPanel.start();

                aLineChartPanel = new Thread(new LineChartPanel());
                aLineChartPanel.start();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void start(){
        if(t == null)
            t = new Thread(this, "PrintTest");
        t.start();
    }
}
