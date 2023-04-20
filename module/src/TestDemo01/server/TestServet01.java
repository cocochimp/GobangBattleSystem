package TestDemo01.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServet01 {
    ServerSocket ss=null;
    DataInputStream dis=null;   //装饰流
    DataOutputStream dos=null;

    public TestServet01(){
        try {
            ss=new ServerSocket(12345);
            Socket server = ss.accept();
            dis = new DataInputStream(server.getInputStream());
            dos = new DataOutputStream(server.getOutputStream());
            MsgReader serverReader = new MsgReader(dis);
            serverReader.start();
            MsgSender serverSender = new MsgSender(dos);
            serverSender.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestServet01();
    }

}

class MsgReader extends Thread{
    DataInputStream mydis;
    public MsgReader(DataInputStream dis){
        this.mydis=dis;
    }

    public void run(){
        while(true){
            try {
                String msg = mydis.readUTF();
                System.out.println("服务端发来的消息是:"+msg);
                if(msg.equals("byebye")){
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

class MsgSender extends Thread{
    DataOutputStream mydos;

    public MsgSender(DataOutputStream dos){
        this.mydos=dos;
    }

    public void run(){
        while(true){
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                String msg = br.readLine();

                mydos.writeUTF(msg);
                if(msg.equals("byebye")){
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}