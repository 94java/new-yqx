package top.hellocode.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import top.hellocode.common.PasswordEncoder;
import top.hellocode.common.constants.UserConstants;
import top.hellocode.common.res.ResultCodeEnum;
import top.hellocode.entity.User;
import top.hellocode.entity.dto.UserDto;
import top.hellocode.exception.GlobalException;
import top.hellocode.mapper.UserMapper;
import top.hellocode.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author HelloCode.
 * @since 2023-10-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserInfoById(Long id) {
        return getSafeUser(userMapper.selectById(id));
    }

    @Override
    public Page<User> list(Integer pageNum, Integer pageSize, UserDto user) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(user.getUsername()), User::getUsername, user.getUsername())
                .like(StringUtils.isNotEmpty(user.getNickname()), User::getNickname, user.getNickname())
                .eq(StringUtils.isNotEmpty(user.getPhone()), User::getPhone, user.getPhone())
                .eq(StringUtils.isNotEmpty(user.getEmail()), User::getEmail, user.getEmail())
                .eq(StringUtils.isNotEmpty(user.getStatus()), User::getStatus, user.getStatus());

        userMapper.selectPage(page, queryWrapper);
        // 数据脱敏处理
        page.setRecords(
                page.getRecords().stream().map(this::getSafeUser).collect(Collectors.toList())
        );
        return page;
    }

    @Override
    public void register(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(user.getUsername()), User::getUsername, user.getUsername());
        Integer count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new GlobalException(ResultCodeEnum.DOUBLE_USERNAME, "用户名已经存在");
        }
        // 使用Hutool对密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userMapper.insert(fillDefault(user));
    }

    @Override
    public void update(User user) {
        // 不允许修改的数据进行处理
        user.setUsername(null);
        user.setDelFlag(null);
        // 如果修改密码，对密码进行加密
        if(StringUtils.isNotEmpty(user.getPassword())){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        // 修改
        userMapper.updateById(user);
    }

    /**
     * @author: HelloCode.
     * @date: 2023/10/5 19:23
     * @param: user
     * @return: top.hellocode.entity.User
     * @description: 用户信息脱敏
     */
    private User getSafeUser(User user) {
        if (user == null) {
            return null;
        }
        user.setPassword(DesensitizedUtil.password(user.getPassword()));
        user.setPhone(DesensitizedUtil.mobilePhone(user.getPhone()));
        user.setEmail(DesensitizedUtil.email(user.getEmail()));
        return user;
    }

    /**
     * @author: HelloCode.
     * @date: 2023/10/5 21:46
     * @param: null
     * @return: 填充后的User对象
     * @description: 默认信息填充
     */
    private User fillDefault(User user){
        if(StringUtils.isEmpty(user.getNickname())){
            user.setNickname("yqx_" + RandomUtil.randomString(4));
        }
        if(StringUtils.isEmpty(user.getSex())){
            user.setSex(UserConstants.SEX_MAN);
        }
        if(StringUtils.isEmpty(user.getAvatar())){
            user.setAvatar(UserConstants.DEFAULT_AVATAR);
        }
        if(StringUtils.isEmpty(user.getStatus())){
            user.setStatus(UserConstants.STATUS_OK);
        }
        if(StringUtils.isEmpty(user.getDelFlag())){
            user.setDelFlag(UserConstants.DEL_OK);
        }
        if(StringUtils.isEmpty(user.getPassword())){
            String pwd = RandomUtil.randomNumbers(6);
            user.setPassword(passwordEncoder.encode(pwd));
        }
        return user;
    }
}
