package com.example.client.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.client.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;


@Controller
@RequestMapping(value = "role")

public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Value("${server.port}")
    private String port;
    //ribbon+restTemplate调用服务
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private RedisUtil redisUtil;


    @RequestMapping(value = "detail/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject roleList(@PathVariable long roleId) {
        return restTemplate.getForObject("http://server/role/detail/"+roleId,JSONObject.class);

    }




}
