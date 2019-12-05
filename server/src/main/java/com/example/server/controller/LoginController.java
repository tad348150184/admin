package com.example.server.controller;


import com.alibaba.fastjson.JSONObject;

import com.example.server.dao.UserDao;
import com.example.server.pojo.ServerResponse;
import com.example.server.pojo.User;
import com.example.server.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "user")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Value("${server.port}")
    private String port;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject abc(@RequestBody String user) {
        User newUser = JSONObject.parseObject(user, User.class);
        newUser = userDao.findByUserNameAndPassword(newUser.getUserName(), newUser.getPassword());
        if (newUser != null) {
            redisUtil.set(newUser.getUserName(), newUser);
            //给前端作为请求头返回后端方便验证
            String token=newUser.getUserName()+ "_"+DigestUtils.md5DigestAsHex((newUser.getUserName() +newUser.getPassword()).getBytes());
            return ServerResponse.responseResultSuccess(token);
        } else {
            return ServerResponse.responseResultCusError("用户名或密码错误");
        }
    }
}

