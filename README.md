# GobangBattleSystem
基于Java GUI的广东海洋大学五子棋对战系统

（1）当以有双方玩家进入游戏大厅后，第三个玩家进入游戏大厅，可以看见前面的玩家的座位信息和头像、姓名信息：

![image-20230420110527315](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420110527.png)

![image-20230420110535853](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420110535.png)

 

（2）当对位存在对手时，一方点击“开始游戏”后，双方进入游戏对战界面

![image-20230420110549069](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420110549.png)

![image-20230420110556858](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420110556.png)

![image-20230420110603112](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420110603.png)



（3）聊天室中，双方可实现聊天

![image-20230420110610589](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420110610.png)

 

（4）当双方点击“开始游戏”后，进入游戏模式

![image-20230420110618429](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420110618.png)

 

（5）当某方胜利时，对战双方客户端弹出弹窗

![image-20230420110624989](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420110625.png)

 

 

（6）当有一方在游戏过程中点击“认输”按钮后，出现弹窗“xxx认输，游戏结束”（和棋同理）

![image-20230420110632224](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420110632.png)



> 响应逻辑

在五子棋界面中，主要有两个事件要将数据传入服务器进行处理并返回相应的信息给客户端进行响应：

1、当自己和对手都点击“开始游戏”按钮后，双方五子棋面板（客户端）与服务器进行“三次握手”，实现连接。服务器会随机给予一名玩家“棋权”，该玩家可下黑棋，下好棋后，将该点坐标存入相应数组（值为1），服务器通过遍历数组将所有点的坐标传回给自己与对手的客户端，客户端接收到坐标后通过画板将棋子位置绘制下来，然后交换“棋权”，并更换下棋颜色（白棋数组值为2）；

2、在服务器中编写一个“判断五子棋胜利”的算法，通过遍历数组中的数据进行判断，当赢得游戏后，将该玩家的姓名传回给自己和对手，然后将服务器储存棋子坐标的数组全部设置为0，最后在自己和对手的客户端显示“xxx，赢得比赛”的弹窗，结束比赛。
