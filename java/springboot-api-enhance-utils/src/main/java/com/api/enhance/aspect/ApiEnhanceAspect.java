package com.api.enhance.aspect;

import com.api.enhance.ApiEnhanceManager;
import com.api.enhance.annotation.TimeLimit;
import com.api.enhance.exception.ContinRequestException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;

/**
 * Create by Chris Chan
 * Create on 2019/4/1 10:40
 * Use for:
 */
//@Aspect
//@Order(-1000)
//@Component
public abstract class ApiEnhanceAspect {
    @Before("@annotation(timeLimit)")
    public void doBefore(JoinPoint point, TimeLimit timeLimit) throws Throwable {
        /*
        需要在请求的时候缓存请求终端ip，收入map，记录时间
        收到请求在map中读取ip，比较时间，时间不够则拦截，没有找到则放行，并记录时间
        也可以考虑在过滤器中进行处理
         */
        long timeLimitValue = timeLimit.value();//设定的时间限制 默认为0直接放行
        if (timeLimitValue > 0) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();

            String sessionId = requestAttributes.getSessionId();//sessionId
            String servletPath = request.getServletPath();//请求的接口

            BASE64Encoder base64Encoder = new BASE64Encoder();
            String key = sessionId + "-" + base64Encoder.encode(servletPath.getBytes());//缓存的key

            //System.out.println("key: "+key);

            Long lastTime = ApiEnhanceManager.getTimeLimit(key);//上次记录的时间
            if (null != lastTime) {//如果有记录时间，而且时间间隔小于设定时间，则拦截
                long time = System.currentTimeMillis() - lastTime;//算出时间差
                if (time <= timeLimitValue) {
                    //return;//如果间隔时间少于设定时间，拦截返回,不重新记录时间
                    handleTimeLimitException(new ContinRequestException(time, timeLimitValue, "Requests are too fast and must be at least " + timeLimitValue + " milliseconds apart."));//抛异常处理);
                }
            }
            ApiEnhanceManager.addTimeLimit(key, System.currentTimeMillis());//记录时间并放行
        }

    }

    //处理异常
    protected abstract void handleTimeLimitException(ContinRequestException e);
}
