package com.example.clienttest.pojo;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;


public class ServerResponse implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(ServerResponse.class);
    public static <T> JSONObject nologin() {
        JSONObject json = new JSONObject();
        json.put("code", 100);
        json.put("message", "未登录");
        json.put("data", null);
        return json;
    }

    public static <T> JSONObject responseResultSuccess(T t) {
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("message", "成功");
        json.put("data", t);
        logger.info("result success");
        return json;
    }

    public static JSONObject responseResultCusError(Object msg) {
        JSONObject json = new JSONObject();
        json.put("code", 0);
        json.put("message", msg);
        logger.error("result error:"+msg);
        return json;
    }
}
