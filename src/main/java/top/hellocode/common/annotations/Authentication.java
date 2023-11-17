package top.hellocode.common.annotations;

import java.lang.annotation.*;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-17  09:19
 * @Description: 自定义注解，标记需要登录认证的接口
 * 加载类上，所有接口都需要鉴权，加在对应方法上则该方法需要鉴权
 * 无需参数，标记作用，默认添加该注解就需要鉴权
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Authentication {
}
