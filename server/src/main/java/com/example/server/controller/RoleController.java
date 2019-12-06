package com.example.server.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.server.dao.RoleDao;
import com.example.server.dao.UserDao;
import com.example.server.pojo.Role;
import com.example.server.pojo.ServerResponse;
import com.example.server.utils.RedisUtil;
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

import java.util.List;


@Controller
@RequestMapping(value = "role")

public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Value("${server.port}")
    private String port;
    //ribbon+restTemplate调用服务
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RoleDao roleDao;

    @RequestMapping(value = "detail/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject roleList(@PathVariable long roleId) {
        List<Role> roleList;
        if(roleId==0){
            roleList=roleDao.findAll();
        }else{
            roleList=roleDao.findById(roleId);
        }
        return ServerResponse.responseResultSuccess(roleList);

    }




}
