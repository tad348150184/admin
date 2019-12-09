package com.example.client.controller;


import com.example.client.ClientApplication;
import com.example.client.dao.LoginDao;
import com.alibaba.fastjson.JSONObject;
import com.example.client.pojo.ServerResponse;
import com.example.client.pojo.User;
import com.example.client.utils.RedisUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.catalina.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "user")

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Value("${server.port}")
    private String port;
    //ribbon+restTemplate调用服务
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private RedisUtil redisUtil;
//    @Autowired
//    LoginDao loginDao;  fegin调用服务代码复杂，jar包版本对不上，弃用

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
//    @HystrixCommand(fallbackMethod = "loginError")
    public JSONObject home(@RequestBody  String user, HttpServletRequest request) {
        User newUser = JSONObject.parseObject(user, User.class);
        String token=newUser.getUserName()+ "_"+ DigestUtils.md5DigestAsHex((newUser.getUserName() +newUser.getPassword()).getBytes());
        //通过服务名/接口地址调用
        //return restTemplate.getForObject("http://server/getUser?param="+port,JSONObject.class);
        //return loginDao.login(port);
        if(redisUtil.hasKey(newUser.getUserName())){
            return ServerResponse.responseResultCusError("用户已登录");
        }
        return restTemplate.postForObject("http://server/user/login",user,JSONObject.class);

    }



//    熔断,服务端挂掉调用这个方法
//    public JSONObject loginError(String user, HttpServletRequest request) {
//        return ServerResponse.responseResultCusError("服务器挂啦");
//    }


}
