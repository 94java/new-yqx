package cc.jiusi.blog.config;

import cc.jiusi.blog.entity.vo.UserVo;
import cc.jiusi.blog.service.IUserService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cc.jiusi.blog.common.utils.UserContext;
import org.springframework.context.annotation.Lazy;

import java.util.Date;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-10-05  21:19
 * @Description: MyBatis-plus配置
 */

@Configuration
public class MyBatisConfig implements MetaObjectHandler {
    @Autowired
    @Lazy
    private IUserService userService;

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
        metaObject.setValue("updateTime", new Date());

        String userId = UserContext.getUserId();
        // 注册登录等操作无需token（userId）
        if(StrUtil.isNotBlank(userId)){
            UserVo user = userService.getUserInfoById(userId);
            metaObject.setValue("createBy",user.getNickname());
            metaObject.setValue("updateBy",user.getNickname());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",new Date());
        String userId = UserContext.getUserId();
        // 注册登录等操作无需token（userId）
        if(StrUtil.isNotBlank(userId)){
            UserVo user = userService.getUserInfoById(userId);
            metaObject.setValue("updateBy",user.getNickname());
        }
    }
}
