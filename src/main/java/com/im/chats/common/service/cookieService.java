package com.im.chats.common.service;

import com.alibaba.fastjson.JSONObject;

import com.im.chats.common.dao.SqlDao;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class cookieService {

    @Resource
    private SqlDao sqlDao;

    /**
     * 验证token是否有效，并返回该token的userid
     * @param request
     * @return
     */
    public JSONObject tokenyz(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        JSONObject map= new JSONObject();
        String token = "";

        if(cookies != null){
            for (Cookie cookie : cookies) {
                switch(cookie.getName()){
                    case "token":
                        token = cookie.getValue();
                        break;
                    default:
                        break;
                }
            }
            if (!"".equals(token)) {
                String userid = sqlDao.queryForMap("select userid from im_user_pas where token = '"+token+"'")
                        .get("userid").toString();

                if(!"".equals(userid)){
                    map.put("status","1");
                    map.put("result",userid);
                }else{
                    map.put("status","0");
                    map.put("result","token已过期");
                }
            }else{
                map.put("status","0");
                map.put("result","token为空");
            }
        }else{
            map.put("status","0");
            map.put("result","cookie为空");
        }
        return map;
    }


}
