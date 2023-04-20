package TestDemo01.client;

import java.io.*;
import java.net.Socket;

public class TestClient01 {
    Socket client=null;
    DataInputStream dis=null;   //装饰流
    DataOutputStream dos=null;

    public TestClient01(){
        try {
            client=new Socket("127.0.0.1",12345);
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());
            MsgReader clientReader = new MsgReader(dis);
            clientReader.start();
            MsgSender clientSender = new MsgSender(dos);
            clientSender.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestClient01();
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
                System.out.println("客户端发来的消息是:"+msg);
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
