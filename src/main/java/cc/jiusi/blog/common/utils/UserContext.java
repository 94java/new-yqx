package cc.jiusi.blog.common.utils;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-17  08:52
 * @Description: TODO
 */
public class UserContext {
    public static final ThreadLocal<String> local = new ThreadLocal<>();

    // 获取用户id
    public static String getUserId() {
        return local.get();
    }

    // 设置用户id
    public static void setUserId(String userId) {
        local.set(userId);
    }

    // 清除用户id
    public static void remove() {
        local.remove();
    }
}
