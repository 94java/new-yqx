package top.hellocode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.hellocode.common.res.Result;
import top.hellocode.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import top.hellocode.entity.dto.UserDto;

import java.util.List;

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
    public User getUserInfoById(Long id);

    public Page<User> list(Integer pageNum, Integer pageSize, UserDto user);

    void register(User user);

    void update(User user);
}
