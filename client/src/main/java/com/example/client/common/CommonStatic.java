package com.example.client.common;

import com.alibaba.fastjson.JSONObject;
import com.example.client.aspect.RoleAspect;
import com.example.client.dao.AdminLogDao;
import com.example.client.pojo.AdminLog;
import com.example.client.pojo.ServerResponse;
import com.example.client.pojo.User;
import com.example.client.utils.RedisUtil;
import com.example.client.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@Component
public class CommonStatic {

    private static final Logger logger = LoggerFactory.getLogger(CommonStatic.class);

    private static  RedisUtil redisUtil;


    private static AdminLogDao adminLogDao;


    @Autowired
    public  void setRedisUtil(RedisUtil redisUtil) {
        CommonStatic.redisUtil = redisUtil;
    }

    @Autowired
    public  void setAdminLogDao(AdminLogDao adminLogDao) {
        CommonStatic.adminLogDao = adminLogDao;
    }

    public static JSONObject IfRole(HttpServletRequest request) {
        String[] token=request.getHeader("token").split("_");
        String userName = token[0];
        User newUser = JSONObject.parseObject(redisUtil.get(userName).toString(), User.class);
        if(null==newUser.getRoleId()||1!=newUser.getRoleId()){
            return ServerResponse.responseResultCusError("你没有权限");
        }
        return ServerResponse.responseResultSuccess("有权限");
    }


    public static JSONObject IfLogin(HttpServletRequest request){
        String[] token=request.getHeader("token").split("_");
        String userName = token[0];
        String md5=token[1];
        //未登录拦截
        if(null==redisUtil.get(userName)){
            return ServerResponse.nologin();
        }
        User newUser = JSONObject.parseObject(redisUtil.get(userName).toString(), User.class);
        if(!md5.equalsIgnoreCase(DigestUtils.md5DigestAsHex((newUser.getUserName() +newUser.getPassword()).getBytes()))){
            return ServerResponse.responseResultCusError("你不是该用户");
        }
        return ServerResponse.responseResultSuccess("已登录");
    }

    public static void saveLog(HttpServletRequest request) {
        String[] token=request.getHeader("token").split("_");
        String ipAddr = RequestUtil.getIpAddress(request);
        String url = request.getRequestURL().toString();
        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("请求源IP:【{}】,请求URL:【{}】,请求参数:【{}】", ipAddr, url, sb.toString());
        AdminLog adminLog = new AdminLog();
        adminLog.setArgs(sb.toString());
        adminLog.setIp(ipAddr);
        adminLog.setUrl(url);
        adminLog.setUserName(token[0]);
        adminLogDao.save(adminLog);
    }

}
