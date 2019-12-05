package com.example.client.aspect;


import com.alibaba.fastjson.JSONObject;
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


@Component
@Aspect
public class LoginAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoginAspect.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AdminLogDao adminLogDao;

    @Pointcut("within(com.example.client.controller..*) && !within(com.example.client.controller.LoginController)")
    public void pointCut() {
    }
    @Around("pointCut()")
    public Object trackInfo(ProceedingJoinPoint point) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("请求耗时");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String[] token=request.getHeader("token").split("_");
        String userName = token[0];
        String md5=token[1];
        //未登录拦截
        if(null==redisUtil.get(userName)){
            return ServerResponse.nologin();
        }
        User newUser = JSONObject.parseObject(redisUtil.get(userName).toString(), User.class);
        if(!md5.equalsIgnoreCase(DigestUtils.md5DigestAsHex((newUser.getUserName() +newUser.getPassword()).getBytes()))){
            return ServerResponse.responseResultCusError("你不是该用户");
        }
        String ipAddr = RequestUtil.getIpAddress(request);
        String url = request.getRequestURL().toString();
        Object[] args = point.getArgs();
        logger.info("请求源IP:【{}】,请求URL:【{}】,请求参数:【{}】",ipAddr,url,args);
        AdminLog adminLog=new AdminLog();
        adminLog.setArgs(args.toString());
        adminLog.setIp(ipAddr);
        adminLog.setUrl(url);
        adminLog.setUserId(newUser.getId());
        adminLogDao.save(adminLog);
        Object result= point.proceed();
        stopWatch.stop();
        logger.info(stopWatch.prettyPrint());
        return result;

    }

    public static void main(String[] args) throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("请求耗时");

        Thread.sleep(1000);

        stopWatch.stop();


    }
}
