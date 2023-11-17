package top.hellocode.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.hellocode.common.interceptors.FreshInterceptor;
import top.hellocode.common.interceptors.LoginInterceptor;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-17  08:55
 * @Description: mvc 配置类
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 刷新token有效期
        registry.addInterceptor(new FreshInterceptor(redisTemplate)).order(0);
        // 登录拦截器
        registry.addInterceptor(new LoginInterceptor(redisTemplate)).order(1)
                .excludePathPatterns("/favicon.ico");
    }
}
