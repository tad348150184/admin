package com.example.server;

import com.example.server.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServerApplicationTests {
    @Autowired
    RedisUtil redisUtil;
    @Test
    void contextLoads() {
//        redisUtil.del("t");
        System.out.println("redis:"+   redisUtil.get("t"));
    }

}
