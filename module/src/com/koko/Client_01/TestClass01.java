package com.koko.Client_01;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

//3、游戏界面
public class TestClass01 implements ActionListener {
    Socket client = null;
    DataOutputStream dos = null;

    //存储上个页面传来的信息
    String[] str = new String[1];
    Icon[] ico = new Icon[1];

    String file_path="E:\\专业资料\\后端项目资料\\project_Java\\Finish_Project\\gobangGame\\module\\src\\com\\koko\\resources\\";
    JFrame jf=new JFrame("五子棋游戏");

    //布局设置
    JTabbedPane jtp_1=new JTabbedPane(SwingConstants.TOP);
    JSplitPane jsp_main=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JSplitPane jsp_left=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    JSplitPane jsp_left_north=new JSplitPane(JSplitPane.VERTICAL_SPLIT);

    //设置按钮
    JButton start_music=new JButton("播放音乐");
    JButton start=new JButton("开始游戏");
    JButton flatter=new JButton("求和");
    JButton defeat=new JButton("认输");
    JButton exit=new JButton("返回房间");
    JButton jb_sent=new JButton("发送");

    //1-1、左上
    JTabbedPane jtp_2=new JTabbedPane(SwingConstants.TOP);
    JPanel left_north=new JPanel();
    JPanel left_north_center=new JPanel();
    JPanel left_north_south=new JPanel();

    //1-2、左中
    JTabbedPane jtp_3=new JTabbedPane(SwingConstants.TOP);
    JPanel left_center=new JPanel();
    JPanel left_center_center=new JPanel();
    JPanel left_center_south=new JPanel();

    //1-3、左下:
    JTabbedPane jtp_4=new JTabbedPane();
    JPanel left_south=new JPanel();
    JTextArea jta = new JTextArea();
    JScrollPane left_south_center = new JScrollPane(jta);
    JPanel left_south_south=new JPanel();
    JPanel left_south_south_center=new JPanel();
    JPanel left_south_south_right=new JPanel();
    JTextField sent_info=new JTextField("",9);

    //2、右边:
    JPanel right=new JPanel();
    JPanel right_north=new JPanel();
    JPanel right_south=new JPanel();
    JPanel right_up_left=new JPanel();

    //2-3、右下:
    JPanel right_down_center=new JPanel();

    //构造方法
    public TestClass01(String my_name,Icon my_face){
        str[0]=my_name;
        ico[0]=my_face;
        JTextArea jta = new JTextArea();

        //设置 jspLeft 和 jspLeftUp 属性
        jsp_left.setDividerLocation(400);
        jsp_left.setDividerSize(3);
        jsp_left_north.setDividerLocation(200);
        jsp_left_north.setDividerSize(3);

        left_north.setLayout(new BorderLayout());
        left_north_center.add(new JLabel());    //***
        left_north_center.add(new JLabel());    //***
        left_north_south.add(new JLabel("P1玩家"));
        left_north.add(left_north_center,"Center");
        left_north.add(left_north_south,"South");

        jtp_2.addTab("自己",left_north);
        jsp_left_north.setTopComponent(jtp_2);

        left_center.setLayout(new BorderLayout());
        left_center_center.add(new JLabel());    //***
        left_center_center.add(new JLabel());    //***
        left_center_south.add(new JLabel("P2玩家"));
        left_center.add(left_center_center,"Center");
        left_center.add(left_center_south,"South");

        jtp_3.addTab("对手",left_center);
        jsp_left_north.setBottomComponent(jtp_3);

        left_south.setLayout(new BorderLayout());
        left_south_south_center.add(sent_info);
        left_south_south_right.add(jb_sent);
        left_south_south.add(left_south_south_center,"Center");
        left_south_south.add(left_south_south_right,"West");
        left_south.add(left_south_center,"Center");
        left_south.add(left_south_south,"South");

        jtp_4.addTab("聊天",left_south);

        //1、整合成左边
        jsp_left.setTopComponent(jsp_left_north);
        jsp_left.setBottomComponent(jtp_4);

        right.setLayout(new BorderLayout());

        //2-1、右上:
        right_north.setLayout(new BorderLayout());
        right_up_left.add(new JLabel("<<< 五子棋游戏——房间 >>>"));
        right_north.add(right_up_left,"West");
        right.add(right_north,"North");

        //2-2、右中:
        right.add(new TestQipan01(),"Center");

        //2-3、右下:
        right_south.setLayout(new BorderLayout());
        right_down_center.add(start_music);
        right_down_center.add(start);
        right_down_center.add(flatter);
        right_down_center.add(defeat);
        right_down_center.add(exit);

        //所有按钮的监听器
        jb_sent.addActionListener(this);
        start_music.addActionListener(this);
        start.addActionListener(this);
        flatter.addActionListener(this);
        defeat.addActionListener(this);
        exit.addActionListener(this);

        right_south.add(right_down_center,"Center");
        right.add(right_south,"South");

        //设置jsp_main
        jsp_main.setDividerLocation(200);
        jsp_main.setDividerSize(3);
        jsp_main.setLeftComponent(jsp_left);
        jsp_main.setRightComponent(right);

        //设置jtp_1属性
        jtp_1.addTab("游戏大厅",jsp_main);

        //设置jf属性
        jf.getContentPane().add(jtp_1);
        jf.setSize(800,700);
        jf.setLocation(-10,50);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        connectServer();    //连接服务器:界面 10086
        createReaderThread(jta);    //创建线程:界面

        //将数据写入服务器
        try {
            dos = new DataOutputStream(client.getOutputStream());
            dos.writeUTF("display");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jb_sent){
            try {
                dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF("info"+"::"+str[0]+"::"+sent_info.getText());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        else if(e.getSource()==start_music){
            //播放音乐
            //while中的true可换成参数来控制音乐的停止播放
            new Thread(this::playMusic).start();
        }
        else if(e.getSource()==start){
            try {
                dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF("start_game");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        else if(e.getSource()==flatter){
            try {
                dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF("flatter");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        else if(e.getSource()==defeat){
            try {
                dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF("defeat");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        else if(e.getSource()==exit){
            JOptionPane.showMessageDialog(jf,"返回房间!","返回",JOptionPane.PLAIN_MESSAGE);
            jf.setVisible(false);
            new TestUser01(str[0],ico[0]);
        }
    }

    //内部类
    //接收从服务器端传来的数据，并进行处理:界面
    class clientReader01 extends Thread {
        DataInputStream dis;
        JTextArea jta;
        TestClass01 tc;

        public clientReader01(DataInputStream dis, JTextArea jta,TestClass01 tc) {
            this.dis = dis;
            this.jta = jta;
            this.tc = tc;
        }

        public void run() {
            while (true) {
                try {
                    String info = dis.readUTF();
                    String[] str01=info.split(" ");
                    String[] str02 = info.split("::");    //聊天框信息

                    //1、出现自己和对手的信息
                    if(info.startsWith("java")){
                        if(str01[2].equals(ico[0].toString())){
                            System.out.println("1:"+info);
                            left_north_center.add(new JLabel(new ImageIcon(str01[2])));
                            left_north_center.add(new JLabel(str01[3]));
                        }else{
                            left_center_center.add(new JLabel(new ImageIcon(str01[2])));
                            left_center_center.add(new JLabel(str01[3]));
                        }
                    }

                    //2、开始游戏
                    if(info.equals("start_game")){
                        JOptionPane.showMessageDialog(jf,"双方准备完毕,游戏开始!","结果",JOptionPane.PLAIN_MESSAGE);
                    }

                    //3、聊天框事件
                    if(str02[0].equals("info")) {
                        System.out.println("客户端运行!");
                        tc.jta.append(str02[1] + ":" + str02[2] + "\n");
                        tc.sent_info.setText("");
                    }

                    //4、求和按钮和认输按钮
                    if(info.equals("flatter")){
                        JOptionPane.showMessageDialog(jf,"有玩家申请和棋,游戏结束!","结果",JOptionPane.PLAIN_MESSAGE);
                    }
                    if(info.equals("black_lost")){
                        JOptionPane.showMessageDialog(jf,"黑棋认输,游戏结束!","结果",JOptionPane.PLAIN_MESSAGE);
                    }
                    else if(info.equals("white_lost")){
                        JOptionPane.showMessageDialog(jf,"白棋认输,游戏结束!","结果",JOptionPane.PLAIN_MESSAGE);
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    //连接服务器:界面
    public void connectServer() {
        try {
            client = new Socket("127.0.0.1", 10086);
            dos = new DataOutputStream(client.getOutputStream());
            dos.writeUTF(str[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建线程:界面
    public void createReaderThread(JTextArea jta) {
        try {
            clientReader01 reader = new clientReader01(new DataInputStream(client.getInputStream()), jta,this);
            reader.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //播放音乐
    public void playMusic() {// 背景音乐播放
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(file_path+"music\\斗地主.wav"));    //绝对路径
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);

            // value可以用来设置音量，从0-2.0
            double value = 2;
            float dB = (float) (Math.log(value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {
                nByte = ais.read(buffer, 0, SIZE);
                sdl.write(buffer, 0, nByte);
            }
            sdl.stop();
        } catch (Exception e) {
            System.out.println("歌曲已经放完一首!");
        }
    }
}