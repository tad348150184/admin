package com.example.server.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.server.dao.UserDao;
import com.example.server.pojo.ServerResponse;
import com.example.server.utils.RedisUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;


@Controller
@RequestMapping(value = "user")

public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Value("${server.port}")
    private String port;


    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject userList() {
        //通过服务名/接口地址调用
        //return restTemplate.getForObject("http://server/getUser?param="+port,JSONObject.class);
        //return loginDao.login(port);
        return ServerResponse.responseResultSuccess(userDao.findAll());

    }


}
