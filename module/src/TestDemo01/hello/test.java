package TestDemo01.hello;

public class test {
    public static void main(String[] args) {
        //字符串分割截取场景
        String info="java.awt.Rectangle[x=134,y=490,width=50,height=50] 25 E:\\专业资料\\project_Java\\gobangGame\\module\\src\\com\\koko\\resources\\face\\5-1.gif 1";
        //获取点.的索引
//        String[] str = info.split(" ");    //对话框小信息
//        String str03=info.substring(0,1);
//
//        System.out.println(str[0]);
//        System.out.println(str[1]);
//        System.out.println(str[2]);
//        System.out.println(str[3]);
//        System.out.println(str03);
        int[] time=new int[1];
        time[0]++;
        System.out.println(time[0]);

//        int dex01=info.indexOf("=");
//        int dex02=info.indexOf(",");
//        int dex03=info.indexOf("i");
//        int dex04=info.indexOf("]");
//        int dex05=info.indexOf("E");
//        int dex06=info.indexOf("gif");
//        int dex07=info.indexOf("S");
//
//        //截取点之后的内容
//        String na = info.substring(dex01+1,dex02);  //x
//        String nb=info.substring(dex02+3,dex03-2);  //y
//        String nc=info.substring(dex05,dex06+3);    //头像地址
//        String nd=info.substring(dex04+1,dex05);    //位置号码
//        String ne=info.substring(dex06+3,dex07);  //姓名
//        String nf=info.substring(dex07);    //开始信号
//
//        System.out.println(na);
//        System.out.println(nb);
//        System.out.println(nc);
//        System.out.println(nd);
//        System.out.println(ne);
//        System.out.println(nf);
    }
}
