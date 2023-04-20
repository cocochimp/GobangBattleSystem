package TestDemo02;

import java.io.*;
import java.net.*;
import java.util.*;

class User{
    String name;
    DataInputStream dis;
    DataOutputStream dos;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public DataInputStream getDis() {
        return dis;
    }
    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }
    public DataOutputStream getDos() {
        return dos;
    }
    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public User(String name, DataInputStream dis, DataOutputStream dos) {
        super();
        this.name = name;
        this.dis = dis;
        this.dos = dos;
    }

}

//服务器端的读取接受数据
class ChatServer {
    Vector<User> users = new Vector<User>();
    public void waitConnect() {
        try {
            ServerSocket ss = new ServerSocket(12345);
            while(true) {
                Socket server = ss.accept();
                DataInputStream dis = new DataInputStream(server.getInputStream());
                DataOutputStream dos = new DataOutputStream(server.getOutputStream());
                String userName = dis.readUTF();
                User user = new User(userName,dis,dos);
                users.add(user);
                MsgReader userReader = new MsgReader(user,users);
                userReader.start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public static void main(String args[]) {
        ChatServer chatserver = new ChatServer();
        chatserver.waitConnect();
    }
}

//
class MsgReader extends Thread{
    User user;
    Vector<User> userList =null;
    public MsgReader(User user,Vector<User> userList) {
        super();
        this.user = user;
        this.userList= userList;
    }

    public void run() {
        //通知所有人有新人来,遍历用户列表,向其他用户发送我进来的消息，向我发送其他用户存在的消息
        Iterator<User> it = userList.iterator();
        while(it.hasNext()) {
            User uu = (User)it.next();
            try {
                if(!uu.getName().equals(user.getName())) {
                    uu.getDos().writeUTF("User:"+user.getName());
                    user.getDos().writeUTF("User:"+uu.getName());
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //开始读取消息，并决定是单发还是群发
        while(true) {
            try {
                String info = user.getDis().readUTF();
                String[] str = info.split(":");
                if(str[2].equals("所有人")) {
                    it = userList.iterator();
                    while(it.hasNext()) {
                        User uu =it.next();
                        uu.getDos().writeUTF(info);
                    }
                }else {
                    it = userList.iterator();
                    while(it.hasNext()) {
                        User uu =it.next();
                        if(uu.getName().equals(str[2]))
                            uu.getDos().writeUTF(info);
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }
}
