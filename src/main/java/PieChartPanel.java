import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;

public class PieChartPanel extends JPanel implements Runnable{
    private Thread t;
    @Override
    public void run() {
        Interface.rightPanel.setLayout(new BorderLayout());
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("ARP Packet",Configuration.ARPcnt);
        dataset.setValue("UDP Packet",Configuration.UDPcnt);
        dataset.setValue("TCP Packet",Configuration.TCPcnt);
        dataset.setValue("ICMP Packet",Configuration.ICMPcnt);
        JFreeChart chart = ChartFactory.createPieChart(
                "Net Packet Category",
                dataset,
                true,
                true,
                false
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        Interface.rightPanel.removeAll();  // 移除已有的组件
        Interface.rightPanel.add(chartPanel, BorderLayout.CENTER);
        Interface.rightPanel.revalidate();  // 重新布局
    }
    public void start(){
        if(t == null)
            t = new Thread(this, "PieChartPanel");
        t.start();
    }

}
