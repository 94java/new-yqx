package top.hellocode.common.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.hellocode.common.annotations.Authentication;
import top.hellocode.common.constants.RedisConstants;
import top.hellocode.common.utils.UserContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-17  08:47
 * @Description: 登录拦截器
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    private StringRedisTemplate redisTemplate;

    public LoginInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("当前请求路径：{}",request.getRequestURI());
        // 获取原方法
        if(!(handler instanceof HandlerMethod)){
            // 请求静态或其他资源，放行
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        // 寻找注解
        Authentication annotation = handlerMethod.getMethodAnnotation(Authentication.class);
        // 方法不存在注解，则判断对应类是否存在注解
        if(annotation == null){
            // 获取类上的注解
            annotation = handlerMethod.getBeanType().getAnnotation(Authentication.class);
        }
        if(annotation == null){
            // 类和方法都不存在鉴权注解，则放行
            return true;
        }

        // ===========需要鉴权认证===========
        // 获取token
        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)){
            // 不包含token，拦截
            response.setStatus(401);
            return false;
        }
        // 解析token
        String userId = redisTemplate.opsForValue().get(RedisConstants.LOGIN_TOKEN + token);
        // token 无效，拦截
        if(StringUtils.isBlank(userId)){
            response.setStatus(401);
            return false;
        }
        // 保存用户id
        UserContext.setUserId(Long.valueOf(userId));
        // 放行
        return true;
    }
}
