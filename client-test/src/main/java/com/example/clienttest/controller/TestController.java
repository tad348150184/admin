package com.example.clienttest.controller;



import com.alibaba.fastjson.JSONObject;
import com.example.clienttest.pojo.ServerResponse;
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
@RequestMapping(value = "test")

public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    @Value("${server.port}")
    private String port;
    //ribbon+restTemplate调用服务
    @Autowired
    RestTemplate restTemplate;
//    @Autowired
//    LoginDao loginDao;  fegin调用服务代码复杂，jar包版本对不上，弃用

    @RequestMapping(value = "login", method = RequestMethod.GET)
    @ResponseBody
    @HystrixCommand(fallbackMethod = "loginError")
    public JSONObject home() {
//        logger.info("param:"+param);
        //通过服务名/接口地址调用
//        return restTemplate.getForObject("http://server/getUser?param="+port,JSONObject.class);
        return ServerResponse.responseResultSuccess("我是test zuul");
//        return loginDao.login(port);

    }

    public JSONObject loginError() {
        return ServerResponse.responseResultCusError("服务器挂啦");
    }


}
