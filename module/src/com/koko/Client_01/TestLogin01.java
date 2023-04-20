package com.koko.Client_01;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//1、登录界面
class TestLogin01 extends Component implements ActionListener {
    String[] str = new String[1];

    JFrame jf=new JFrame("登录窗口");
    String file_path="E:\\专业资料\\后端项目资料\\project_Java\\Finish_Project\\gobangGame\\module\\src\\com\\koko\\resources\\";

    //标签(JLabel)与密码框(JPasswordField)
    JLabel info=new JLabel("请输入你的个人信息");
    JLabel username=new JLabel("用户名:");
    JLabel password=new JLabel("密码:");
    JLabel face=new JLabel("头像:");
    JLabel use_face=new JLabel(new ImageIcon(file_path+"face\\1-1.gif"));

    //文本框(JTextField)
    JTextField userName=new JTextField();
    JPasswordField Password=new JPasswordField();

    //按钮(JButton)
    JButton Connect=new JButton("连接");
    JButton Reset=new JButton("重置");
    JButton Exit=new JButton("退出");

    public TestLogin01(){
        str[0]="use_face.getText()";
        //1、整体布局:边框布局管理器
        jf.getContentPane().setLayout(new BorderLayout());
        JPanel north=new JPanel();
        JPanel center=new JPanel();
        JPanel south=new JPanel();

        //2、北边布局:
        north.setLayout(new BorderLayout());
        JPanel north_north=new JPanel();
        JPanel north_left=new JPanel();
        JPanel north_center=new JPanel();

        //北_北:
        north_north.add(info);

        //北_西:网格布局管理器
        north_left.setLayout(new GridLayout(3,1,1,1));
        north_left.add(username);
        north_left.add(password);
        north_left.add(face);

        //北_中:网格布局管理器
        north_center.setLayout(new GridLayout(3,1,1,1));
        north_center.add(userName);
        north_center.add(Password);
        north_center.add(use_face);

        north.add(north_north,"North");
        north.add(north_left,"West");
        north.add(north_center,"Center");

        //3、中区布局:不使用布局管理器
        center.setLayout(null);
        int x=0,y=0;
        for(int i=1;i<=85;i++){
            if(x<420){
                JButton jb=new JButton(new ImageIcon(file_path+"face\\"+i+"-1.gif"));
                jb.setBounds(x,y,42,42);
                jb.addActionListener(this);
                center.add(jb);
                x+=42;
            }else{
                y+=42;
                x=0;
                JButton jb=new JButton(new ImageIcon(file_path+"face\\"+i+"-1.gif"));
                jb.setBounds(x,y,42,42);
                jb.addActionListener(this);
                center.add(jb);
            }
        }

        //4、南区布局:流式布局管理器
        //并注册监听器
        south.setLayout(new FlowLayout());
        south.add(Connect);
        Connect.addActionListener(this);
        south.add(Reset);
        Reset.addActionListener(this);
        south.add(Exit);
        Exit.addActionListener(this);

        //将各个小布局加进整体布局中
        jf.getContentPane().add(north,"North");
        jf.getContentPane().add(center,"Center");
        jf.getContentPane().add(south,"South");

        //设置Swing窗口基本属性
        jf.setSize(440,550);
        jf.setVisible(true);
        jf.setLocation(50,50);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //退出键
        if(e.getSource()==Exit){
            System.exit(0);
        }
        //重置键
        else if(e.getSource()==Reset){
            use_face.setIcon(new ImageIcon(file_path+"face\\1-1.gif"));
            userName.setText(null);
            Password.setText(null);
        }
        //连接键
        else if(e.getSource()==Connect){
            isTextField(e);
        }
        else{
            //设置新头像
            JButton btn = (JButton)e.getSource();
            use_face.setIcon(btn.getIcon());
        }
    }


    //判断文本框内容是否合法
    public void isTextField(ActionEvent e){
        //1、判断姓名是否为空;
        String name=userName.getText();
        int name_long=name.length();

        //2、判断密码是否为6位数;
        int password=Password.getPassword().length;

        if(name_long>0 && password==6){
            JOptionPane.showMessageDialog(this,"欢迎进入游戏大厅!","结果",JOptionPane.PLAIN_MESSAGE);
            Icon face=use_face.getIcon();
            new TestUser01(name,face);
            jf.setVisible(false);
        }else if(name_long==0 && password==6 ){
            JOptionPane.showMessageDialog(this,"姓名不能为空","结果",JOptionPane.ERROR_MESSAGE);
        }else if(name_long>0){
            //JOptionPane.showMessageDialog(this,"密码不为6位数！","结果",JOptionPane.ERROR_MESSAGE);
            Icon face=use_face.getIcon();
            new TestUser01(name,face);
            jf.setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this,"姓名为空，且密码不为六位数！","结果",JOptionPane.ERROR_MESSAGE);
        }
    }
}
