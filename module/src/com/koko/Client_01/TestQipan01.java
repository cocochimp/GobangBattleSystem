package com.koko.Client_01;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

//4、绘制棋盘
class TestQipan01 extends JPanel implements MouseListener {
    Socket client = null;
    DataOutputStream dos = null;
    String file_path="E:\\专业资料\\后端项目资料\\project_Java\\Finish_Project\\gobangGame\\module\\src\\com\\koko\\resources\\";
    String color="qizi";   //存储颜色

    //引入图片
    Image qipan;
    Image blackqizi;
    Image whiteqizi;

    //棋子坐标
    int row=-1;int line=-1;
    int arrayX=-1;int arrayY=-1;
    int[][] all_chess=new int[15][15];

    public TestQipan01(){
        super();
        //读取棋子和棋盘的文件
        try {
            qipan= ImageIO.read(new File(file_path+"gobang\\board.gif"));
            blackqizi=ImageIO.read(new File(file_path+"gobang\\heiqi.gif"));
            whiteqizi= ImageIO.read(new File(file_path+"gobang\\baiqi.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        connectServer();
        createReaderThread();

        this.addMouseListener(this);
    }

    //画笔
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //画棋盘
        g.drawImage(qipan,0,0,this);

        //画棋子
        for(int i=0;i<15;i++){
            for(int j=0;j<15;j++){
                if(all_chess[i][j]==1){
                    g.drawImage(blackqizi,i*35+10,j*35+10,this);
                }else if(all_chess[i][j]==2){
                    g.drawImage(whiteqizi,i*35+10,j*35+10,this);
                }
            }
        }
    }


    //鼠标点击响应
    @Override
    public void mouseClicked(MouseEvent e) {//五子棋的判断
        row=e.getX();
        line=e.getY();
        arrayX=(row-10)/35;
        arrayY=(line-10)/35;

        if(all_chess[arrayX][arrayY]==0){
            try{
                dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF(color+" "+arrayX+" "+arrayY);
            } catch (IOException ioException) {
                System.out.println("不要点外面!");
            }
        }
    }

    //连接服务器
    public void connectServer() {
        try {
            client = new Socket("127.0.0.1", 10001);
            dos = new DataOutputStream(client.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建线程
    public void createReaderThread() {
        try {
            clientReader01 reader = new clientReader01(new DataInputStream(client.getInputStream()),this);
            reader.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //内部类
    //接收从服务器端传来的数据，并进行处理
    class clientReader01 extends Thread {
        DataInputStream dis;
        TestQipan01 tp;

        public clientReader01(DataInputStream dis,TestQipan01 tp) {
            this.dis = dis;
            this.tp=tp;
        }

        public void run() {
            while (true) {
                try {
                    String info = dis.readUTF();
                    String[] str_01=info.split(" ");

                    //储存棋子坐标
                    if(str_01[0].equals("black")) {
                        int x=Integer.parseInt(str_01[1]);
                        int y=Integer.parseInt(str_01[2]);
                        all_chess[x][y]=1;
                    }
                    else if(str_01[0].equals("white")){
                        int x=Integer.parseInt(str_01[1]);
                        int y=Integer.parseInt(str_01[2]);
                        all_chess[x][y]=2;
                    }

                    //判断输赢
                    if(str_01[1].equals("end")){
                        if(str_01[0].equals("black_win")){
                            System.out.println("黑棋胜利!");
                            JOptionPane.showMessageDialog(tp,"黑棋胜!","返回",JOptionPane.PLAIN_MESSAGE);
                        }else if(str_01[0].equals("white_win")){
                            System.out.println("白棋胜利!");
                            JOptionPane.showMessageDialog(tp,"白棋胜!","返回",JOptionPane.PLAIN_MESSAGE);
                        }
                        for(int i=0;i<15;i++){
                            for(int j=0;j<15;j++){
                                all_chess[i][j]=0;
                            }
                        }
                    }

                    tp.repaint();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
}
