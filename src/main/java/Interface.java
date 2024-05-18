import jpcap.JpcapCaptor;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface extends Thread {
    private Thread t;
    private Thread aNetPacket;
    private Thread aPrintTest;
    public static DefaultTableModel tableModel;
    public static JTable table;
    public static PieChartPanel rightPanel;
    public static JPanel lowerPanel;
    @Override
    public void run(){
//        try {
//            UIManager.setLookAndFeel(new NimbusLookAndFeel ());
//        } catch (UnsupportedLookAndFeelException e) {
//            throw new RuntimeException(e);
//        }
        int frameWidth = 1200;
        int frameHeight = 600;

        Configuration.devices = JpcapCaptor.getDeviceList();
        Configuration.interfaceToString();

        JFrame frame = new JFrame("NetPacketCatcher");
        frame.setSize(frameWidth,frameHeight);

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container mainContainer = frame.getContentPane();

        double upperPanelRatio = 0.7;
        double leftPanelRatio = 0.35;

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());

        lowerPanel = new JPanel();
        lowerPanel = new LineChartPanel();
        lowerPanel.setPreferredSize(new Dimension(0,300));

        JPanel leftUpPanel = new JPanel();
        leftUpPanel.setLayout(new FlowLayout());

        JPanel leftPanel = new JPanel();
        BorderLayout leftPanelBorderLayout = new BorderLayout();
        leftPanel.setLayout(leftPanelBorderLayout);
        leftPanel.add(leftUpPanel,BorderLayout.NORTH);

        rightPanel = new PieChartPanel();
        JSplitPane splitPanelRL = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPanel,rightPanel);
        splitPanelRL.setResizeWeight(leftPanelRatio);
        upperPanel.add(splitPanelRL);

        JSplitPane splitPanelUpDown = new JSplitPane(JSplitPane.VERTICAL_SPLIT,upperPanel,lowerPanel);
        splitPanelUpDown.setResizeWeight(upperPanelRatio);

        //文字提示
        JLabel tip = new JLabel("选择网卡");
        leftUpPanel.add(tip);

        //选择网卡栏
        JComboBox<Object> deviceSelect = new JComboBox<>(Configuration.devicesString.toArray());
        leftUpPanel.add(deviceSelect);

        //停止运行按钮
        JButton stopButton = new JButton("停止抓包");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Configuration.status = false;
            }
        });

        //列表
            //table
        String[] arguments = {"No","Time","Source","Destination","Protocol","Length","Info"};
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
//        table.setEnabled(false);
        for(String argument : arguments){
            tableModel.addColumn(argument);
        }

        //清空表格按钮
        JButton clearButton = new JButton("清空");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
//                Configuration.ALLcnt = 0;
                Configuration.UDPcnt = 0;
                Configuration.ARPcnt = 0;
                Configuration.TCPcnt = 0;
                Configuration.ICMPcnt = 0;
            }
        });

            //滚动列表
        JScrollPane scrollPane = new JScrollPane(table);
        leftPanel.add(scrollPane,BorderLayout.CENTER);

        //确定按钮
        JButton confirmSelect = new JButton("确定");
        confirmSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Configuration.status = true;
                Configuration.selectedDevice = Configuration.devices[deviceSelect.getSelectedIndex()];
//                if(aNetPacket==null){
                    aNetPacket = new NetPacket();
                    aNetPacket.start();
//                }
//                if(aPrintTest==null){
                    aPrintTest = new PrintTest();
                    aPrintTest.start();
//                }
            }
        });

        //查询按钮
        JButton search = new JButton("查询功能");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int packetInfoWidth = 500;
                int packetInfoHeight = 400;

                JDialog packetInfo = new JDialog(frame,"Packet Info",false);
                packetInfo.setResizable(false);
                packetInfo.setSize(packetInfoWidth,packetInfoHeight);
                packetInfo.setLocationRelativeTo(frame);
                packetInfo.setLayout(new BorderLayout());

                JPanel infoUpperPanel = new JPanel();
                infoUpperPanel.setLayout(new FlowLayout());

                JPanel infoLowerPanel = new JPanel();
                infoLowerPanel.setLayout(new FlowLayout());

                JLabel tip = new JLabel("请输入数据包编号:");

                JTextField numInput = new JTextField();
                numInput.setPreferredSize(new Dimension(100,25));

                JButton confirmSearch = new JButton("确认");
                confirmSearch.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int index = Integer.parseInt(numInput.getText());
                        String tempPacketData = PacketDataArray.packetDataRandomArray.get(index);
                        String tempPacketInfo = PacketDataArray.packetInfoRandomArray.get(index);
                        JLabel tipData = new JLabel("Packet Data");
                        JTextArea packetData = new JTextArea(tempPacketData);
                        packetData.setLineWrap(true);
                        packetData.setWrapStyleWord(true);
                        packetData.setOpaque(false); // 设置背景透明

                        JLabel tipInfo = new JLabel("Packet Info");
                        JTextArea packetInfo = new JTextArea(tempPacketInfo);
                        packetInfo.setLineWrap(true);
                        packetInfo.setWrapStyleWord(true);
                        packetInfo.setPreferredSize(new Dimension(packetInfoWidth-50, 60));
                        packetInfo.setOpaque(false); // 设置背景透明

                        JScrollPane scrollPane = new JScrollPane(packetData);
                        scrollPane.setOpaque(false); // 设置滚动栏背景透明
                        scrollPane.getViewport().setOpaque(false); // 设置视口背景透明
                        scrollPane.setPreferredSize(new Dimension(packetInfoWidth-50,200));


                        infoLowerPanel.removeAll();  // 移除已有的组件
                        infoLowerPanel.add(tipInfo);
                        infoLowerPanel.add(packetInfo);
                        infoLowerPanel.add(tipData);
                        infoLowerPanel.add(scrollPane);
                        infoLowerPanel.revalidate();  // 重新布局

                    }
                });

                infoUpperPanel.add(tip);
                infoUpperPanel.add(numInput);
                infoUpperPanel.add(confirmSearch);

                packetInfo.add(infoLowerPanel,BorderLayout.CENTER);
                packetInfo.add(infoUpperPanel,BorderLayout.NORTH);
                packetInfo.setVisible(true);
            }
        });

        //按钮添加
        leftUpPanel.add(confirmSelect);
        leftUpPanel.add(stopButton);
        leftUpPanel.add(clearButton);
        leftUpPanel.add(search);

        mainContainer.add(splitPanelUpDown);
        mainContainer.setVisible(true);
        frame.setVisible(true);
    }
    public void start(){
        if(t == null)
            t = new Thread(this, "Interface");
        t.start();
    }
}
