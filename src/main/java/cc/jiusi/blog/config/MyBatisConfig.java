package cc.jiusi.blog.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cc.jiusi.blog.common.utils.UserContext;

import java.util.Date;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-10-05  21:19
 * @Description: MyBatis-plus配置
 */

@Configuration
public class MyBatisConfig implements MetaObjectHandler {

    // MyBatis-Plus 分页插件配置
    @Bean
    public MybatisPlusInterceptor paginationInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", new Date());
        metaObject.setValue("createBy", UserContext.getUserId().toString());
        metaObject.setValue("updateTime", new Date());
        metaObject.setValue("updateBy",UserContext.getUserId().toString());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",new Date());
        metaObject.setValue("updateBy",UserContext.getUserId().toString());
    }
}
