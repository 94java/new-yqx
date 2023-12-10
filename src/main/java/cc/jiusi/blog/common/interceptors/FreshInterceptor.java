package cc.jiusi.blog.common.interceptors;

import cc.jiusi.blog.common.constants.SysConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import cc.jiusi.blog.common.constants.RedisConstants;
import cc.jiusi.blog.common.utils.UserContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-17  09:03
 * @Description: token 刷新拦截器
 */
public class FreshInterceptor implements HandlerInterceptor {
    private StringRedisTemplate redisTemplate;
    public FreshInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(SysConstants.USER_TOKEN_HEADER);
        if(StringUtils.isNotBlank(token)){
            // token 不为空
            String userId = redisTemplate.opsForValue().get(RedisConstants.LOGIN_TOKEN + token);
            if(StringUtils.isNotBlank(userId)){
                // 存在 token
                UserContext.setUserId(Long.valueOf(userId));
                // 刷新过期时间 (30 minutes)
                redisTemplate.expire(RedisConstants.LOGIN_TOKEN + token, 30, TimeUnit.MINUTES);

            }
        }
        // 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除用户信息，防止内存溢出
        UserContext.remove();
    }
}
