package com.example.client.aspect;


import com.alibaba.fastjson.JSONObject;
import com.example.client.common.CommonStatic;
import com.example.client.dao.AdminLogDao;
import com.example.client.dao.LoginDao;
import com.example.client.pojo.AdminLog;
import com.example.client.pojo.ServerResponse;
import com.example.client.pojo.User;
import com.example.client.utils.RedisUtil;
import com.example.client.utils.RequestUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.springframework.util.DigestUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.example.client.common.CommonStatic.saveLog;


@Component
@Aspect
public class LoginAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoginAspect.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AdminLogDao adminLogDao;


    //拦截未登录、记录操作日志、记录接口耗时
    @Pointcut("within(com.example.client.controller..*) && !within(com.example.client.controller.LoginController)&& !within(com.example.client.controller.RoleController)")
    public void pointCut() {
    }
    @Around("pointCut()")
    public Object trackInfo(ProceedingJoinPoint point) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("LoginAspect");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        JSONObject ifLogin=CommonStatic.IfLogin(request);
        if(200 != ifLogin.getIntValue("code")){
            return ifLogin;
        }
        Object result= point.proceed();
        saveLog(request);
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
