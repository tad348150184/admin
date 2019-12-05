package com.example.client.controller;


import com.example.client.ClientApplication;
import com.example.client.dao.LoginDao;
import com.alibaba.fastjson.JSONObject;
import com.example.client.pojo.ServerResponse;
import com.example.client.utils.RedisUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@Controller
@RequestMapping(value = "user")

public class UserController {
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

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject userList() {
        //通过服务名/接口地址调用
        //return restTemplate.getForObject("http://server/getUser?param="+port,JSONObject.class);
        //return loginDao.login(port);
        return restTemplate.getForObject("http://server/user/list",JSONObject.class);

    }




}
