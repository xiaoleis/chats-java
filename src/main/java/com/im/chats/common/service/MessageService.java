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

    /**
     * 保存会话到数据库中
     * @param userid
     * @param hhdxid
     * @param message
     * @return
     */
    public String saveMessage(String userid, String hhdxid, String message){
        String chatbs = "";
        List<Map<String,Object>> chatlist = sqlDao.queryForList(sqlchatbs,new Object[]{userid,hhdxid});
        List<Map<String,Object>> chatlist1 = sqlDao.queryForList(sqlchatbs,new Object[]{hhdxid,userid});
        //如果我的会话已存在
        if(chatlist.size() == 0){
            chatbs = UUID.randomUUID().toString().replace("-","");
            String cjhhsql = " insert into im_user_chats (chatbs,userid,hhdxid,hhdxlx) values (?,?,?,?) ";
            sqlDao.update(cjhhsql,new Object[]{chatbs,userid,hhdxid,"persion"});
            sqlDao.update(cjmessagesql,new Object[]{chatbs,userid,message,"text"});
        }else{
            chatbs = chatlist.get(0).get("chatbs").toString();
            sqlDao.update(cjmessagesql,new Object[]{chatbs,userid,message,"text"});
        }

        //如果多方的会话已存在
        if(chatlist1.size() == 0){
            chatbs = UUID.randomUUID().toString().replace("-","");
            String cjhhsql = " insert into im_user_chats (chatbs,hhdxid,userid,,hhdxlx) values (?,?,?,?) ";
            sqlDao.update(cjhhsql,new Object[]{chatbs,hhdxid,userid,"persion"});
            sqlDao.update(cjmessagesql,new Object[]{chatbs,hhdxid,message,"text"});
        }else{
            chatbs = chatlist.get(0).get("chatbs").toString();
            sqlDao.update(cjmessagesql,new Object[]{chatbs,hhdxid,message,"text"});
        }

        return "";
    }



}
