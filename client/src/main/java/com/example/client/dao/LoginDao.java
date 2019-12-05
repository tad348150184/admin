package com.example.client.dao;


import com.alibaba.fastjson.JSONObject;
//import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


//@FeignClient(value = "server")
public interface LoginDao {
    @RequestMapping(value = "/getUser",method = RequestMethod.GET)
    public JSONObject login(@RequestParam(value = "param") String param);
}
