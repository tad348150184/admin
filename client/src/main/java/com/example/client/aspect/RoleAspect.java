package com.example.client.aspect;


import com.alibaba.fastjson.JSONObject;
import com.example.client.common.CommonStatic;
import com.example.client.dao.AdminLogDao;
import com.example.client.pojo.AdminLog;
import com.example.client.pojo.ServerResponse;
import com.example.client.pojo.User;
import com.example.client.utils.RedisUtil;
import com.example.client.utils.RequestUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.example.client.common.CommonStatic.saveLog;


@Component
@Aspect
public class RoleAspect {
    private static final Logger logger = LoggerFactory.getLogger(RoleAspect.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AdminLogDao adminLogDao;


    //角色权限拦截、记录操作日志、记录接口耗时
    @Pointcut("within(com.example.client.controller.RoleController) ")
    public void pointCut() {
    }

    //
    @AfterReturning("pointCut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        saveLog(request);

    }

    @Around("pointCut()")
    public Object trackInfo(ProceedingJoinPoint point) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("RoleAspect");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        if(200 != CommonStatic.IfLogin(request).getIntValue("code")){
            return CommonStatic.IfLogin(request);
        }
        if(200 != CommonStatic.IfRole(request).getIntValue("code")){
            return CommonStatic.IfRole(request);
        }
        Object result= point.proceed();
        stopWatch.stop();
        logger.info(stopWatch.prettyPrint());
        logger.info("耗时："+   stopWatch.getTotalTimeMillis()+"ms");
        return result;

    }

    public static void main(String[] args) throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("请求耗时");
        Thread.sleep(1000);
        stopWatch.stop();

    }
}
