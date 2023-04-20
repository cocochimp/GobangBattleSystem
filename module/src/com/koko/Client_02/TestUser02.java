package com.koko.Client_02;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;


//2、房间界面
class TestUser02 extends JFrame implements ActionListener {

    Socket client = null;
    DataOutputStream dos = null;
    String[] str = new String[1];
    Icon[] ico = new Icon[1];

    String file_path = "E:\\专业资料\\后端项目资料\\project_Java\\gobangGame\\module\\src\\com\\koko\\resources\\";
    Random r = new Random();

    //布局设置
    JFrame jf = new JFrame("客户端游戏主界面");
    JTabbedPane jtp_1 = new JTabbedPane(SwingConstants.TOP);
    JSplitPane jsp_main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JSplitPane jsp_left = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

    //左上部件
    JLabel use_face = new JLabel(new ImageIcon(file_path + "img\\boy1.gif"));
    JLabel use_name = new JLabel(" ");
    JLabel use_main_face=new JLabel();

    //右部件
    JLabel title = new JLabel("<<< 五子棋游戏 >>>");
    JButton start_game = new JButton("开始游戏");
    JButton auto_seat = new JButton("自动入座");
    JButton exit_game = new JButton("退出游戏");
    JButton exit_login = new JButton("退出登录");

    //座位部件
    JButton[] jb_noone = new JButton[30];
    JLabel[] jl_xqnoone = new JLabel[15];
    JLabel[] jl_name = new JLabel[30];
    JLabel[] jl_number = new JLabel[15];

    //其他参数
    int x = 29, y = 54, z = 79, sum = 1;
    Icon noone = new ImageIcon(file_path + "img\\noone.gif");
    Icon xqnoone = new ImageIcon(file_path + "img\\xqnoone.gif");

    //左上:用户信息
    JPanel left_north = new JPanel();

    JTabbedPane jtp_2 = new JTabbedPane(SwingConstants.TOP);
    JPanel left_north_center = new JPanel();
    JPanel left_north_south = new JPanel();

    //左下:设置服务器信息
    JTextArea jta = new JTextArea();
    JScrollPane left_south = new JScrollPane(jta);
    JTabbedPane jtp_3 = new JTabbedPane();


    //右边:游戏座位(边框布局)
    JPanel right = new JPanel();
    JPanel right_north = new JPanel();

    //右上:边框布局
    JPanel right_north_left = new JPanel();
    JPanel right_north_right = new JPanel();

    //右中:
    JPanel right_center = new JPanel();

    //带参构造方法中做界面
    public TestUser02(String my_name, Icon my_face) {
        str[0] = my_name;
        ico[0] = my_face;
        //头像
        use_main_face.setIcon(my_face);

        //1、设置jspLeft属性
        jsp_left.setDividerLocation(300);
        jsp_left.setDividerSize(3);

        left_north.setLayout(new BorderLayout());

        left_north_center.add(use_face);
        use_name.setText(my_name);
        left_north_south.add(use_main_face);
        left_north_south.add(use_name);
        left_north.add(left_north_center, "Center");
        left_north.add(left_north_south, "South");

        jtp_2.addTab("用户信息", left_north);
        jsp_left.setTopComponent(jtp_2);

        jtp_3.addTab("服务器信息", left_south);
        jsp_left.setBottomComponent(jtp_3);

        right.setLayout(new BorderLayout());

        right_north.setLayout(new BorderLayout());

        right_north_left.add(title);
        right_north_right.add(start_game);
        right_north_right.add(auto_seat);
        right_north_right.add(exit_login);
        right_north_right.add(exit_game);
        right_north.add(right_north_left, "West");
        right_north.add(right_north_right, "East");
        right.add(right_north, "North");

        right_center.setLayout(null);
        right_center.setBackground(Color.GRAY);

        seat(my_name, my_face);//排座位的方法
        right.add(right_center, "Center");

        //注册四大监听器
        exit_login.addActionListener(this);
        start_game.addActionListener(this);
        exit_game.addActionListener(this);
        auto_seat.addActionListener(this);

        //设置jspMain属性
        jsp_main.setDividerLocation(200);
        jsp_main.setDividerSize(3);
        jsp_main.setLeftComponent(jsp_left);
        jsp_main.setRightComponent(right);

        //设置jtp属性
        jtp_1.addTab("游戏大厅", jsp_main);

        //设置jf属性
        jf.getContentPane().add(jtp_1);
        jf.setSize(800, 700);
        jf.setLocation(760,50);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        connectServer();    //连接服务器
        createReaderThread(jta);    //创建线程

        //将数据写入服务器
        String info = "欢迎:" + str[0] + ":" + "进入房间";
        try {
            dos = new DataOutputStream(client.getOutputStream());
            dos.writeUTF(info);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //排座位并添加监听器的方法
    public void seat(String my_name, Icon my_face) {
        //排座位并实现坐座位功能
        for (int i = 0; i < 30; i++) {
            if (x >= 580) {
                x = 29;
                z = 79;
                y += 109;
            }
            if (i % 2 == 0) {
                jb_noone[i] = new JButton(noone);
                jl_xqnoone[i / 2] = new JLabel(xqnoone);
                jl_name[i] = new JLabel();
                jl_number[i / 2] = new JLabel(String.valueOf(sum));

                jb_noone[i].setBounds(x, y, 50, 50);
                jl_xqnoone[i / 2].setBounds(z, y - 5, 55, 55);
                jl_name[i].setBounds(x + 5, y + 55, 40, 20);
                jl_number[i / 2].setBounds(z + 20, y + 55, 20, 20);

                right_center.add(jb_noone[i]);
                right_center.add(jl_xqnoone[i / 2]);
                right_center.add(jl_name[i]);
                right_center.add(jl_number[i / 2]);

                x += 105;
                z += 184;
                sum++;
            }
            else {
                jb_noone[i] = new JButton(noone);
                jl_name[i] = new JLabel();
                jb_noone[i].setBounds(x, y, 50, 50);
                jl_name[i].setBounds(x + 5, y + 55, 40, 20);
                right_center.add(jb_noone[i]);
                right_center.add(jl_name[i]);
                x += 79;
            }

            //入座的监听器
            jb_noone[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < 30; i++) {
                        if (e.getSource() == jb_noone[i] && jb_noone[i].getIcon()==noone) {

                            //将 座位坐标位置 和 头像 写入服务器
                            Rectangle s = jb_noone[i].getBounds();
                            try {
                                dos = new DataOutputStream(client.getOutputStream());
                                dos.writeUTF(s+" "+i+" "+my_face+" "+my_name);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }

                    }

                }
            });
        }
    }

    //连接服务器
    public void connectServer() {
        try {
            client = new Socket("127.0.0.1", 10086);
            dos = new DataOutputStream(client.getOutputStream());
            dos.writeUTF(str[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建线程
    public void createReaderThread(JTextArea jta) {
        try {
            clientReader01 reader = new clientReader01(new DataInputStream(client.getInputStream()), jta);
            reader.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //鼠标点击事件
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exit_login) {
            JOptionPane.showMessageDialog(this, "返回登录页面!", "返回", JOptionPane.PLAIN_MESSAGE);
            jf.setVisible(false);
            new TestLogin02();
        }   //退出登录
        else if (e.getSource() == exit_game) {
            JOptionPane.showMessageDialog(this, "感谢您的游玩!", "退出", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }   //退出游戏
        else if (e.getSource() == start_game) {
            try {
                dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF("Start02");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }   //开始游戏
        else if (e.getSource() == auto_seat) {
            while (true) {
                int i = r.nextInt(30);
                if (String.valueOf(jb_noone[i].getIcon()).equals(String.valueOf(noone))) {
                    //将 座位坐标位置 和 头像 写入服务器
                    Rectangle s = jb_noone[i].getBounds();
                    try {
                        dos = new DataOutputStream(client.getOutputStream());
                        dos.writeUTF(s+" "+i+" "+ico[0]+" "+str[0]);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    break;
                }
            }
        }   //自动入座
    }

    //内部类
    //接收从服务器端传来的数据，并进行处理
    class clientReader01 extends Thread {
        DataInputStream dis;
        JTextArea jta;

        public clientReader01(DataInputStream dis, JTextArea jta) {
            this.dis = dis;
            this.jta = jta;
        }

        public void run() {
            while (true) {
                try {
                    String info = dis.readUTF();
                    String[] str01 = info.split(":");
                    String[] str02 = info.split(" ");
                    String st01 = info.substring(0, 4);

                    if (str01[0].equals("欢迎")) {
                        jta.append(info + "\n");
                    }
                    else if (st01.equals("java")) {

                        //截取点之后的内容
                        String nc=str02[2];    //头像地址
                        String nd=str02[1];   //位置号码
                        String ne=str02[3]; //姓名

                        //重新设置位置
                        for(int j = 0; j < 30; j++){
                            if(String.valueOf(jb_noone[j].getIcon()).equals(String.valueOf(new ImageIcon(nc)))){
                                jb_noone[j].setIcon(noone);
                                jl_name[j].setText(" ");
                            }
                        }
                        for (int i = 0; i < 30; i++) {
                            if (i == Integer.parseInt(nd)) {
                                jb_noone[i].setIcon(new ImageIcon(nc));
                                jl_name[i].setText(ne);
                            }
                        }

                    }
                    else if(info.equals("Start02")){
                        int time=0;
                        for(int i=0;i<30;i++){
                            if(!String.valueOf(jb_noone[i].getIcon()).equals(String.valueOf(noone)) && !String.valueOf(jb_noone[i+1].getIcon()).equals(String.valueOf(noone))){
                                JOptionPane.showMessageDialog(jf, "准备开始游戏!", "结果", JOptionPane.PLAIN_MESSAGE);
                                new TestClass02(str[0], ico[0]);
                                jf.setVisible(false);
                                time++;

                                //点开始游戏后,将 头像 和 名字 传入服务器
                                String touxiang=ico[0]+" "+str[0];
                                try {
                                    dos = new DataOutputStream(client.getOutputStream());
                                    dos.writeUTF(touxiang);
//                                    System.out.println(touxiang);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            i++;
                        }
                        if(time==0){
                            JOptionPane.showMessageDialog(jf, "等待玩家上桌！", "结果", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
