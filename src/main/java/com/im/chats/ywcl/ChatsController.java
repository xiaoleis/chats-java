package com.im.chats.ywcl;

import com.alibaba.fastjson.JSONObject;
import com.im.chats.common.service.cookieService;
import com.im.chats.common.service.sqlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/chats")
public class ChatsController {

    @Resource
    private com.im.chats.common.service.sqlService sqlService;

    @Resource
    private com.im.chats.common.service.cookieService cookieService;


    /**
     * 获取会话列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getChatsList")
    public void getChatsList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject map  = cookieService.tokenyz(request);
        //cookieService.tokenyz(request);
        //如果token认证成功，则读取会话列表
        if("1".equals(map.get("status").toString())){
            String userid = map.get("result").toString();
            String sql = "SELECT\n" +
                    "\tiuc.hhdxid,\n" +
                    "\tiuc.hhdxlx,\n" +
                    "  iu.nc,\n" +
                    "\tifnull(ium.message,'') as message,\n" +
                    "\tifnull(ium.type,'') as type, \n" +
                    "\tifnull(substr(ium.cjsj,6,11),'') as time\n" +
                    "FROM\n" +
                    "\tim_user_chats iuc\n" +
                    "left join im_user iu on iu.userid = iuc.hhdxid\n" +
                    "left join (\n" +
                    "\tselect chatbs,cjsj,message,type from im_chats_message where messagexh = (select max(messagexh) from im_chats_message) \n" +
                    ")ium on ium.chatbs = iuc.chatbs\n" +
                    "where iuc.userid = ?";
            List<Map<String,Object>> chatlist = sqlService.queryForList(sql,new Object[]{userid});
            map.put("status","1");
            map.put("result",chatlist);
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(map.toJSONString());
    }

    /**
     * 获取最近的聊天记录
     * @param response
     * @param request
     */
    @RequestMapping("/getMessagelist")
    public void getMessageList(HttpServletResponse response, HttpServletRequest request) throws IOException {
        JSONObject map = cookieService.tokenyz(request);
        if("1".equals(map.get("status").toString())){
            String hhdxid = request.getParameter("hhdxid");
            String userid = map.get("result").toString();
            String sql = "SELECT\n" +
                            "  b.messagexh,\n" +
                            "\ta.chatbs,\n" +
                            "\ta.userid,\n" +
                            "\ta.hhdxid,\n" +
                            "\tb.senduserid,\n" +
                            "\tb.type,\n" +
                            "\tb.cjsj,\n" +
                            "\tb.message\n" +
                            "FROM\n" +
                            "\tim_user_chats a\n" +
                            "\tRIGHT JOIN im_chats_message b ON a.chatbs = b.chatbs \n" +
                            "WHERE\n" +
                            "\ta.userid = ? \n" +
                            "\tAND a.hhdxid = ?\n" +
                            "order by b.messagexh\n";
            List<Map<String,Object>> list = sqlService.queryForList(sql,new Object[]{userid,hhdxid});
            map.put("status","1");
            map.put("result",list);
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(map.toJSONString());
    }




}
