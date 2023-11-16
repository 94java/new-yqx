package top.hellocode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.hellocode.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import top.hellocode.entity.dto.LoginDto;
import top.hellocode.entity.dto.UserSearchDto;
import top.hellocode.entity.vo.UserVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HelloCode.
 * @since 2023-10-05
 */
public interface IUserService extends IService<User> {
    /**
     * @author: HelloCode.
     * @date: 2023/10/5 19:21
     * @param: id
     * @return: top.hellocode.entity.User
     * @description: 获取单个用户信息
     */
    public UserVo getUserInfoById(Long id);

    public Page<User> list(Integer pageNum, Integer pageSize, UserSearchDto user);

    void register(LoginDto user);

    void update(User user);

    /**
     * @author: HelloCode.
     * @date: 2023/11/16 15:16
     * @param: loginDto
     * @return: java.lang.String  Token
     * @description: 账号密码登录
     */
    String loginByUsername(LoginDto loginDto);

    /**
     * @author: HelloCode.
     * @date: 2023/11/16 15:31
     * @param: loginDto
     * @return: java.lang.String
     * @description: 邮箱快捷登录
     */
    String loginByEmail(LoginDto loginDto);

    /**
     * @author: HelloCode.
     * @date: 2023/11/16 15:31
     * @param: loginDto
     * @return: void
     * @description: 邮箱验证码发送
     */
    void sendEmailCode(LoginDto loginDto);

    /**
     * @author: HelloCode.
     * @date: 2023/11/16 16:48
     * @return: void
     * @description: 用户退出
     */
    void logout(String token);
}
