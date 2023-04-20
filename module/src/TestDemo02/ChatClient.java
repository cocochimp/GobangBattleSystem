package TestDemo02;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient implements ActionListener{
    String name = null;
    Socket client = null;
    JFrame jf = null;
    JTextArea jta = null;   //文本域
    JTextField jtf = null;
    JScrollPane jsp = null;     //滚动面板
    JButton btnSend = null;
    JComboBox jcb = null;   //下拉列表框
    DataOutputStream dos=null;

    public void setClientName() {
        name = JOptionPane.showInputDialog("请输入您的姓名：");
        jcb.addItem(name);
        jf.setTitle(name);
    }

    public void connectServer() {
        try {
            client = new Socket("127.0.0.1",12345);
            dos = new DataOutputStream(client.getOutputStream());
            dos.writeUTF(name);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void createUI() {
        jf = new JFrame("客户端窗口");
        Container con = jf.getContentPane();
        con.setLayout(new BorderLayout());
        jta = new JTextArea();
        jsp = new JScrollPane(jta);
        jtf = new JTextField();
        jcb = new JComboBox();
        jcb.addItem("所有人");
        btnSend = new JButton("发送");
        btnSend.addActionListener(this);
        JPanel pSouth = new JPanel();
        pSouth.setLayout(new BorderLayout());
        pSouth.add(jcb,"West");
        pSouth.add(jtf,"Center");
        pSouth.add(btnSend,"East");
        con.add(jsp,"Center");
        con.add(pSouth,"South");

        jf.setSize(300, 300);

        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createReaderThread() {
        try {
            clientReader reader = new clientReader(new DataInputStream(client.getInputStream()),jta,jcb);
            reader.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        String info = null;
        info = "MSG:"+name+":"+jcb.getSelectedItem()+":"+jtf.getText();
        DataOutputStream dos;
        try {
            dos = new DataOutputStream(client.getOutputStream());
            dos.writeUTF(info);
            jtf.setText("");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public static void main(String args[]) {
        ChatClient chatclient = new ChatClient();
        chatclient.createUI();
        chatclient.setClientName();
        chatclient.connectServer();
        chatclient.createReaderThread();
    }

}

class clientReader extends Thread{
    DataInputStream dis = null;
    JTextArea jta = null;
    JComboBox jcb = null;
    public clientReader(DataInputStream dis,JTextArea jta,JComboBox jcb) {
        this.dis = dis;
        this.jta = jta;
        this.jcb = jcb;
    }
    public void run() {
        while(true) {
            try {
                String info = dis.readUTF();
                String[] str = info.split(":");
                if(str[0].equals("MSG"))
                    jta.append(info+"\n");
                else
                    jcb.addItem(str[1]);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}