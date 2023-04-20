package com.koko.LoginPage;

import java.io.*;
import java.net.*;
import java.util.*;

class Server{
    String name;
    DataInputStream dis;
    DataOutputStream dos;

    public Server(String name, DataInputStream dis, DataOutputStream dos) {
        super();
        this.name = name;
        this.dis = dis;
        this.dos = dos;
    }

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

}

class happy{
    String name;
    DataInputStream dis;
    DataOutputStream dos;

    public happy(String name,DataInputStream dis, DataOutputStream dos) {
        super();
        this.dis = dis;
        this.dos = dos;
    }

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
}

//服务器端的读取接受数据
class ChatServer01 {
    int[] arr=new int[1];   //判断是否双方是否都准备了
    int[] judge_start=new int[1];    //判断谁下棋(黑白)

    //请求客户端Client的连接:界面
    public void waitConnect01() {
        ArrayList<String> array_against=new ArrayList<>();  //存储对战信息
        ArrayList<String> array_local=new ArrayList<>();    //存储座位地址
        try {
            //将服务器对象写入集合
            Vector<Server> users = new Vector<>();
            ServerSocket ss = new ServerSocket(10086);
            while(true) {
                Socket server = ss.accept();
                DataInputStream dis = new DataInputStream(server.getInputStream());
                DataOutputStream dos = new DataOutputStream(server.getOutputStream());
                String userName = dis.readUTF();
                Server user = new Server(userName,dis,dos);
                users.add(user);
                MsgReader01 userReader = new MsgReader01(user,users,array_against,array_local,arr,judge_start);
                userReader.start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //请求客户端Client的连接:棋盘
    public void waitConnect02() {
        int[][] array=new int[15][15];
        try {
            //将服务器对象写入集合
            Vector<happy> users = new Vector<>();
            ServerSocket ss = new ServerSocket(10001);
            while(true) {
                Socket server = ss.accept();
                DataInputStream dis = new DataInputStream(server.getInputStream());
                DataOutputStream dos = new DataOutputStream(server.getOutputStream());
                String userName = dis.readUTF();
                happy user = new happy(userName,dis,dos);
                users.add(user);
                MsgReader02 userReader = new MsgReader02(user,users,array,arr,judge_start);
                userReader.start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        ChatServer01 chatserver = new ChatServer01();
        new Thread(chatserver::waitConnect01).start();
        new Thread(chatserver::waitConnect02).start();
    }
}

//界面:处理数据
class MsgReader01 extends Thread{
    Server user;
    Vector<Server> userList;
    ArrayList<String> array_against;
    ArrayList<String> array_local;
    int[] arr;
    int[] judge_start;
    String[] array_sent_number=new String[100];
    int i=0;

    public MsgReader01(Server user,Vector<Server> userList,ArrayList<String> array_against,ArrayList<String> array_local,int[] arr,int[] judge_start) {
        super();
        this.user = user;
        this.userList= userList;
        this.array_against=array_against;
        this.array_local=array_local;
        this.arr=arr;
        this.judge_start=judge_start;
    }

    public void run() {
        //通知所有人有新人来,遍历用户列表,向其他用户发送我进来的消息，向我发送其他用户存在的消息
        Iterator<Server> it;

        //开始读取消息，并显示数据
        while(true) {
            try {
                String info = user.getDis().readUTF();
                String[] str01 = info.split(":");    //信息框信息
                String[] str02 = info.split(" ");
                String str03=info.substring(0,1);
                String[] str04 = info.split("::");    //聊天框信息

                //一、TestUser界面
                //1、服务器信息:(欢迎:张三:进入房间)
                if (str01[0].equals("欢迎")) {
                    it = userList.iterator();
                    while (it.hasNext()) {
                        Server uu = it.next();
                        uu.getDos().writeUTF(info);
                    }
                }

                //2、入座处理信息:(按钮位置,座位数,头像地址,名字)
                //java.awt.Rectangle[x=502,y=272,width=50,height=50] 17
                //E:\专业资料\project_Java\gobangGame\module\src\com\koko\resources\face\2-1.gif 张三
                if(str02[0].startsWith("java")){
                    array_local.add(info);  //存储座位信息
                    it = userList.iterator();
                    while (it.hasNext()) {
                        Server uu = it.next();
                        uu.getDos().writeUTF(info);
                    }
                }

                //3、显示所有玩家按钮,用于后来者入座（弃用）
                if(info.equals("show_player")){
                    it = userList.iterator();
                    while (it.hasNext()) {
                        Server uu = it.next();
                        for(String s:array_local){
                            uu.getDos().writeUTF("local "+s);
                        }
                    }
                }

                //4、进入房间键
                if(info.startsWith("Start")){
                    for(String s:array_local){
                        String[] sp=s.split(" ");
                        String sent=sp[1];
                        System.out.println(sent);
                        array_sent_number[i]=sent;
                        System.out.println(array_sent_number[i]);
                        i++;
                    }
                    for(int j=0;j<array_sent_number.length;j++){
                        int x=Integer.parseInt(array_sent_number[j]);
                        int y=Integer.parseInt(array_sent_number[j+1]);
                        if(x%2==0 && x==y-1){
                            it = userList.iterator();
                            while (it.hasNext()) {
                                Server uu = it.next();
                                uu.getDos().writeUTF(String.valueOf(x));
                                uu.getDos().writeUTF(String.valueOf(y));
                            }
                        }
                    }
                }

                //5、点击开始键后,将头像和名字传到对战框
                if(str03.equals("E")){
                    it = userList.iterator();
                    if(userList.size()==4){     //当两个人都进入了房间才显示
                        while (it.hasNext()) {
                            Server uu = it.next();
                            for (String s : array_local) {
                                uu.getDos().writeUTF(s);
                            }
                        }
                    }
                }

                //进入新界面后，显示对战人员
                if(info.equals("display")){
                    it = userList.iterator();
                        while (it.hasNext()) {
                            Server uu = it.next();
                            for (String s : array_local) {
                                uu.getDos().writeUTF(s);
                            }
                        }

                }

                //二、TestClass界面
                //1、求和按钮 和 认输按钮 事件
                if(arr[0]==2){
                    if(info.equals("flatter")){
                        it = userList.iterator();
                        while (it.hasNext()) {
                            Server uu = it.next();
                            uu.getDos().writeUTF(info);
                        }
                        arr[0]=0;
                    }
                    if(info.equals("defeat")){
                        if(judge_start[0]==1){
                            it = userList.iterator();
                            while (it.hasNext()) {
                                Server uu = it.next();
                                uu.getDos().writeUTF("black_lost");
                            }
                        }else if(judge_start[0]==0){
                            it = userList.iterator();
                            while (it.hasNext()) {
                                Server uu = it.next();
                                uu.getDos().writeUTF("white_lost");
                            }
                        }
                        arr[0]=0;
                    }
                }

                //2、开始游戏按钮
                if(info.equals("start_game")){
                    arr[0]++;
                }
                if(arr[0]==2){
                    it = userList.iterator();
                    while (it.hasNext()) {
                        Server uu = it.next();
                        uu.getDos().writeUTF("start_game");
                    }
                }

                //3、聊天页面信息
                if(str04[0].equals("info")){
                    System.out.println(str04[1]+":"+str04[2]);
                    it = userList.iterator();
                    while (it.hasNext()) {
                        Server uu = it.next();
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

//棋盘:处理数据
class MsgReader02 extends Thread{
    happy user;
    Vector<happy> userList;
    int[][] array;
    int[] arr;
    int[] judge_start;

    public MsgReader02(happy user,Vector<happy> userList,int[][] array,int[] arr,int[] judge_start) {
        super();
        this.user = user;
        this.userList= userList;
        this.array=array;
        this.arr=arr;
        this.judge_start=judge_start;
    }

    public void run() {
        //通知所有人有新人来,遍历用户列表,向其他用户发送我进来的消息，向我发送其他用户存在的消息
        Iterator<happy> it;

        //开始读取消息，并显示数据
        while(true) {
            try {
                String info = user.getDis().readUTF();
                String[] str_01=info.split(" ");
                int x=Integer.parseInt(str_01[1]);
                int y=Integer.parseInt(str_01[2]);

                //1、开始游戏事件
                if(arr[0]>=2 && (str_01[0].equals("qizi"))){
                    //开始判断棋子,并将棋子坐标存入数组
                    if(judge_start[0]==0){
                        array[x][y]=1;
                        judge_start[0]=1;   //换另一个人下棋
                    }else if(judge_start[0]==1){
                        array[x][y]=2;
                        judge_start[0]=0;   //换另一个人下棋
                    }
                    //将棋子坐标全部遍历出去
                    for(int i=0;i<15;i++){
                        for(int j=0;j<15;j++){
                            if(array[i][j]==1){
                                it = userList.iterator();
                                while (it.hasNext()) {
                                    happy uu = it.next();
                                    uu.getDos().writeUTF("black"+" "+i+" "+j);
                                }
                            }else if(array[i][j]==2){
                                it = userList.iterator();
                                while (it.hasNext()) {
                                    happy uu = it.next();
                                    uu.getDos().writeUTF("white"+" "+i+" "+j);
                                }
                            }
                        }
                    }
                }

                //2、判断胜利,并将相应的信息传回给客户端
                if(winCol(array,x,y)==1){
                    it = userList.iterator();
                    for(int i=0;i<15;i++){
                        for(int j=0;j<15;j++){
                            array[i][j]=0;
                        }
                    }
                    judge_start[0]=0;   //初始化黑棋先下
                    arr[0]=0;   //还要点开始才能玩
                    while (it.hasNext()) {
                        happy uu = it.next();
                        uu.getDos().writeUTF("black_win"+" "+"end");
                    }
                }
                else if(winCol(array,x,y)==2){
                    it = userList.iterator();
                    for(int i=0;i<15;i++){
                        for(int j=0;j<15;j++){
                            array[i][j]=0;
                        }
                    }
                    judge_start[0]=0;   //初始化黑棋先下
                    arr[0]=0;   //还要点开始才能玩
                    while (it.hasNext()) {
                        happy uu = it.next();
                        uu.getDos().writeUTF("white_win"+" "+"end");
                    }
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //判断胜利的方法
    public int winCol(int[][] all_chess,int row,int col) {
        int qizi_count=1;

        //垂直方向判断
        for(int i = 1; i<=4; i++) {
            if (col-i>=0 && all_chess[row][col-i]==all_chess[row][col]) {
                qizi_count++;
            }else{
                break;
            }
        }
        for(int i=1;i<=4;i++) {
            if (col+i<15 && all_chess[row][col+i]==all_chess[row][col]) {
                qizi_count++;
            }else{
                break;
            }
        }
        if(qizi_count==5){
            return array[row][col];
        }
        qizi_count=1;

        //水平方向
        for(int i = 1; i<=4; i++) {
            if (row-i>=0 && all_chess[row-i][col]==all_chess[row][col]) {
                qizi_count++;
            }else{
                break;
            }
        }
        for(int i=1;i<=4;i++) {
            if (row+i<15 && all_chess[row+i][col]==all_chess[row][col]) {
                qizi_count++;
            }else{
                break;
            }
        }
        if(qizi_count==5){
            return all_chess[row][col];
        }
        qizi_count=1;

        //左上右下对角线
        for(int i = 1; i<=4; i++) {
            if (row-i>=0 && col-i>=0 && all_chess[row-i][col-i]==all_chess[row][col]) {
                qizi_count++;
            }else{
                break;
            }
        }
        for(int i=1;i<=4;i++) {
            if (row+i<15 && col+i<15 && all_chess[row+i][col+i]==all_chess[row][col]) {
                qizi_count++;
            }else{
                break;
            }
        }
        if(qizi_count==5){
            return all_chess[row][col];
        }
        qizi_count=1;

        //左下右上对角线
        for(int i = 1; i<=4; i++) {
            if (row-i>=0 && col+i<15 && all_chess[row-i][col+i]==all_chess[row][col]) {
                qizi_count++;
            }else{
                break;
            }
        }
        for(int i=1;i<=4;i++) {
            if (row+i<15 && col-i>0 && all_chess[row+i][col-i]==all_chess[row][col]) {
                qizi_count++;
            }else{
                break;
            }
        }
        if(qizi_count==5){
            return all_chess[row][col];
        }

        return 0;
    }

}
