package com.im.chats.ywcl;

import com.alibaba.fastjson.JSONObject;

import com.im.chats.common.service.cookieService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping(value = "/sjcl")
public class LoginController {


    @Resource
    private com.im.chats.common.service.sqlService sqlService;

    @Resource
    private com.im.chats.common.service.cookieService cookieService;


    /**
     * 登录接口
     * @param name
     * @param password
     * @return
     */
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")   //解决前端收不到返回值的跨域问题
    @RequestMapping("/login")
    public void login(@RequestParam(value = "name") String name, @RequestParam(value = "password") String password ,
                        HttpServletRequest request, HttpServletResponse response) throws IOException {
        String log = "";
        String token = "";
        HttpSession session = request.getSession();
        if(!"".equals(name)){
             List<Map<String,Object>> list =  sqlService.queryForList("SELECT USERID FROM im_user WHERE loginame = '"+name+"' OR EMAIL = '"+name+"'");
             if(list.size() > 0 ) {
                 String userid =  list.get(0).get("USERID").toString();
                 //则用户存在，验证密码
                 session.setAttribute("loginUser",userid);
                 //如果密码验证成功， 获取用户的token
                 token = sqlService.queryForMap("SELECT TOKEN FROM IM_USER_PAS WHERE USERID ='"+userid+"'")
                         .get("TOKEN").toString();
                 //response.setHeader("Access-Control-Allow-Credentials", String.valueOf(true));
                 //Cookie cookie = new Cookie("token",token);
                 //response.addCookie(cookie);
                 //response.setHeader("Http-Only", "true");
                 log = "1";   //登录成功
             }else{
                 log = "3";   //用户未注册
             }
        }
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("status",log);
        jsonObject.put("token",token);
        response.getWriter().write(jsonObject.toJSONString());
    }

    /**
     * 创建账号
     * @param response
     * @param request
     */
    @CrossOrigin
    @RequestMapping("/createZh")
    public void createZh(HttpServletResponse response,HttpServletRequest request) throws IOException {
        String yhm = request.getParameter("yhm");
        String email = request.getParameter("email");
        String log = "";
        if(!"".equals(yhm) && !"".equals(email)){
            int count = Integer.parseInt(sqlService.queryForMap("select count(1) as count from im_user").get("count").toString());
            List<Map<String,Object>> list =  sqlService.queryForList("SELECT USERID FROM im_user WHERE loginame = '"+yhm+"'");
            String userid = 100000 + count + "";
            if(list.size() == 0){
                try {
                    //插入用户表
                    sqlService.update("insert into im_user (userid,loginame,email) values (?,?,?)"
                            ,new Object[]{userid,yhm,email});
                    //生成随机token
                    String token = UUID.randomUUID().toString().replace("-","");
                    sqlService.update("insert into im_user_pas (userid, token) values (?,?)"
                            , new Object[]{userid,token});
                    log = "1";   //创建成功
                }catch (Exception e){
                    e.printStackTrace();
                    log = "0";   //服务器错误
                }
            }else{
                log = "2";   //该用户名已注册
            }
        }
        response.getWriter().write(log);
    }

    /**
     * token认证
     * @param request
     * @param response
     */
    //@CrossOrigin(allowCredentials = "true", allowedHeaders = "*",origins="*")
    @RequestMapping("/tokenrz")
    public void tokenrz(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(cookieService.tokenyz(request).toJSONString());
        response.getWriter().write(cookieService.tokenyz(request).toJSONString());
    }



}
