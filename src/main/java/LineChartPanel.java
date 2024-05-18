import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class LineChartPanel extends JPanel implements Runnable {
    private Thread t;
    public XYSeries series = null;
    public static XYSeriesCollection dataset = null;
    @Override
    public void run() {
        Interface.lowerPanel.setLayout(new BorderLayout());
        series = new XYSeries("Packet Count");
        dataset = new XYSeriesCollection(series);
        if(Configuration.time>=1){
            for(int i = 0;i < Configuration.numTime.size();i ++){
                series.add(i,Configuration.numTime.get(i));
            }
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                        "Packet Flow",
                        "Time/s",
                        "Value",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
                );

        ChartPanel chartPanel = new ChartPanel(chart);
        Interface.lowerPanel.removeAll();  // 移除已有的组件
        Interface.lowerPanel.add(chartPanel, BorderLayout.CENTER);
        Interface.lowerPanel.revalidate();  // 重新布局
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, "LineChartPanel");
        }
    }
}