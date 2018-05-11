package io.predictech.webspheremq;

import com.ibm.mq.*;

import java.io.IOException;

/**
 * @author Weijie Zhao
 * @email tttx(at)me.com
 * @date 2018/5/7
 * @description
 */
public class WebSphereMQ {

    private static int ccsid = 1208; // 服务器MQ服务使用的编码1381代表GBK、1208代表UTF
    private static MQQueueManager qMgr;
    private static String qmName;
    private static String qName;


    private WebSphereMQ() {
    }

    private static WebSphereMQ instance = null;

    public static WebSphereMQ getInstance(String hostname,int port,String qmName,String channel,String qName){
        if(instance == null){
            instance = new WebSphereMQ();

            MQEnvironment.hostname = hostname;
            MQEnvironment.channel = channel;
            MQEnvironment.CCSID = ccsid;
            MQEnvironment.port = port;
            MQEnvironment.userID = "mqadmin";
            MQEnvironment.password = "mqadmin";

            WebSphereMQ.qmName = qmName;
            WebSphereMQ.qName = qName;

            try {
                // 定义并初始化队列管理器对象并连接
                qMgr = new MQQueueManager(qmName);
            } catch (MQException e) {
                System.out.println("初使化MQ出错");
                e.printStackTrace();
            }

        }
        return instance;
    }

    // 往MQ发送消息
    public  String sendMessage(String message) {
        try {
            // 设置将要连接的队列属性
            int openOptions = MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING;
            // 连接队列,关闭了就重新打开
            if (qMgr == null || !qMgr.isConnected()) {
                qMgr = new MQQueueManager(qmName);
            }
            MQQueue queue = qMgr.accessQueue(qName, openOptions);
            // 定义一个简单的消息
            MQMessage putMessage = new MQMessage();
            // 将数据放入消息缓冲区
            putMessage.writeUTF(message);
            // 将消息写入队列
            queue.put(putMessage, new MQPutMessageOptions());
            queue.close();
        } catch (MQException ex) {
            ex.printStackTrace();
            System.out.println("A WebSphere MQ error occurred : Completion code " + ex.completionCode + " Reason code "
                    + ex.reasonCode);
        } catch (IOException ex) {
            System.out.println("An error occurred whilst writing to the message buffer: " + ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                qMgr.disconnect();
            } catch (MQException e) {
                e.printStackTrace();
            }
        }
        return "sendMessage success! {" + message + "}";
    }

    // 从队列中去获取消息，如果队列中没有消息，就会发生异常
    public  String getMessage() {
        String message = null;
        try {
            // 设置将要连接的队列属性
            int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT;
            MQMessage retrieve = new MQMessage();
            // 设置取出消息的属性（默认属性）
            MQGetMessageOptions gmo = new MQGetMessageOptions();
            gmo.options = gmo.options + MQC.MQGMO_SYNCPOINT;// 在同步点控制下获取消息
            gmo.options = gmo.options + MQC.MQGMO_WAIT; // 如果在队列上没有消息则等待
            gmo.options = gmo.options + MQC.MQGMO_FAIL_IF_QUIESCING;// 如果队列管理器停顿则失败
            gmo.waitInterval = 1000; // 设置等待的毫秒时间限制
            // 关闭了就重新打开
            if (qMgr == null || !qMgr.isConnected()) {
                qMgr = new MQQueueManager(qmName);
            }
            MQQueue queue = qMgr.accessQueue(qName, openOptions);
            // 从队列中取出消息
            queue.get(retrieve, gmo);
            message = retrieve.readUTF();
            queue.close();
        } catch (MQException ex) {
            System.out.println("A WebSphere MQ error occurred : Completion code " + ex.completionCode + " Reason code "
                    + ex.reasonCode);
        } catch (IOException ex) {
            System.out.println("An error occurred whilst writing to the message buffer: " + ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                qMgr.disconnect();
            } catch (MQException e) {
                e.printStackTrace();
            }
        }
        return " getMessage success! {" + message + "} ";
    }
}
