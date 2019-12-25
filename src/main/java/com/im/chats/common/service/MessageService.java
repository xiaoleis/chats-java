package com.im.chats.common.service;

import com.im.chats.common.dao.SqlDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MessageService {

    @Resource
    private SqlDao sqlDao;

    private final String sqlchatbs  = " select chatbs  from im_user_chats where userid = ? and hhdxid = ? ";

    private final String cjmessagesql = " insert into im_chats_message " +
            "(chatbs,senduserid,message,type,cjsj) values" +
            " (?,?,?,?,SYSDATE()) ";

    public String saveMessage(String userid, String hhdxid, String message){
        String chatbs = "";
        List<Map<String,Object>> chatlist = sqlDao.queryForList(sqlchatbs,new Object[]{userid,hhdxid});
        //如果该会话已存在
        if(chatlist.size() == 0){
            chatbs = UUID.randomUUID().toString().replace("-","");
            String cjhhsql = " insert into im_user_chats (chatbs,userid,hhdxid,hhdxlx) values (?,?,?,?) ";
            sqlDao.update(cjhhsql,new Object[]{chatbs,userid,hhdxid,"persion"});
            sqlDao.update(cjmessagesql,new Object[]{chatbs,userid,message,"text"});
        }else{
            chatbs = chatlist.get(0).get("chatbs").toString();
            sqlDao.update(cjmessagesql,new Object[]{chatbs,userid,message,"text"});
        }
        return "";
    }



}
