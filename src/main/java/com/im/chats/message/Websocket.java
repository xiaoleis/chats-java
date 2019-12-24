package com.im.chats.message;

import com.im.chats.common.service.MessageService;
import com.im.chats.common.service.cookieService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/Websocket/{userid}")
public class Websocket {

    //链接会话
    private Session session;

    @Resource
    private com.im.chats.common.service.cookieService cookieService;

    //@Resource
    private static MessageService messageService;

    @Resource
    public void setMessageService(MessageService messageService){
        Websocket.messageService = messageService;
    }


    //记录在线人数， 设计成线程安全的
    private static int onlineCount = 0;

    //发送人员编号
    private String userid = "";

    private static ConcurrentHashMap<String,Websocket> websocketmap = new ConcurrentHashMap<String, Websocket>();

    /**
     * 建立新的链接
     * @param prgam
     * @param session
     * @param config
     */
    @OnOpen
    public void onOpen(@PathParam(value = "userid") String prgam, Session session, EndpointConfig config){
        this.session = session;
        userid = prgam;
        //如果当前用户在线，则不加入在线池中
        if(websocketmap!= null){
            if(websocketmap.get(userid) == null){
                websocketmap.put(userid,this);
                addOnlineCount();
            }else{
                System.out.println("用户已在线");
            }
        }else{
            websocketmap.put(userid,this);
            addOnlineCount();
        }
        //System.out.println("在线池"+websocketmap.size());
        System.out.println(userid+"用户上线成功; 当前总共在线人数："+getOnlineCount());
    }

    /**
     * 关闭链接
     */
    @OnClose
    public void onClose(){
        if(!"".equals(userid)) {
            websocketmap.remove(userid);
            subOnlineCount();
            System.out.println(userid+"下线；当前在线人数：" + getOnlineCount());
        }
    }

    /**
     * 服务器收到消息后转给指定用户或者群发
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message,Session session){
        System.out.println("收到客户端的消息："+message);
        //发送给指定用户
        sendToUser(message);
    }

    //@OnMessage
    public void sendToUser(String message){
        String senduserno = message.split("###")[0];
        String sendmessage = message.split("###")[1];
        try {
            //如果该用户在线
            if(websocketmap.get(senduserno) != null){
                websocketmap.get(senduserno).sendMessage(new Date() + "@@@" + userid + "@@@" + sendmessage);
                //将聊天记录到数据库中
                String log =  messageService.saveMessage(senduserno,userid,sendmessage);
            }else {
                System.out.println(senduserno+"该用户不在线");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getOnlineCount() {
         return onlineCount;
    }


     public static synchronized void addOnlineCount() {
         Websocket.onlineCount++;
     }


     public static synchronized void subOnlineCount() {
          Websocket.onlineCount--;
     }


}
