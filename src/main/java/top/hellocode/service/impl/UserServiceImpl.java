package top.hellocode.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.hellocode.common.PasswordEncoder;
import top.hellocode.common.constants.RedisConstants;
import top.hellocode.common.constants.UserConstants;
import top.hellocode.common.res.ResultCodeEnum;
import top.hellocode.common.utils.EmailUtils;
import top.hellocode.entity.User;
import top.hellocode.entity.dto.LoginDto;
import top.hellocode.entity.dto.UserSearchDto;
import top.hellocode.entity.vo.UserVo;
import top.hellocode.exception.GlobalException;
import top.hellocode.mapper.UserMapper;
import top.hellocode.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.github.biezhi.ome.OhMyEmail.SMTP_QQ;

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
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public UserVo getUserInfoById(Long id) {
        return getSafeUser(userMapper.selectById(id));
    }

    @Override
    public Page<User> list(Integer pageNum, Integer pageSize, UserSearchDto user) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 构造查询条件
        queryWrapper.eq(StringUtils.isNotEmpty(user.getUsername()), User::getUsername, user.getUsername())
                .like(StringUtils.isNotEmpty(user.getNickname()), User::getNickname, user.getNickname())
                .eq(StringUtils.isNotEmpty(user.getPhone()), User::getPhone, user.getPhone())
                .eq(StringUtils.isNotEmpty(user.getEmail()), User::getEmail, user.getEmail())
                .eq(StringUtils.isNotEmpty(user.getStatus()), User::getStatus, user.getStatus());
        // 分页查询
        userMapper.selectPage(page, queryWrapper);
        // 构造 page vo
        Page<UserVo> userVo = new Page<>();
        BeanUtils.copyProperties(page, userVo);
        // 数据脱敏处理
        userVo.setRecords(
                page.getRecords().stream().map(this::getSafeUser).collect(Collectors.toList())
        );
        return page;
    }

    @Override
    public void register(LoginDto dto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 用户名重复检验
        queryWrapper.eq(StringUtils.isNotEmpty(dto.getUsername()), User::getUsername, dto.getUsername());
        Integer count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new GlobalException(ResultCodeEnum.DOUBLE_USERNAME, "用户名已经存在");
        }
        // 使用Hutool对密码加密
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        // 拷贝内容
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        // 注册信息
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
     * @param loginDto
     * @author: HelloCode.
     * @date: 2023/11/16 15:16
     * @param: loginDto
     * @return: java.lang.String  Token
     * @description: 账号密码登录
     */
    @Override
    public String loginByUsername(LoginDto loginDto) {
        // 参数校验
        if(StringUtils.isEmpty(loginDto.getUsername()) || StringUtils.isEmpty(loginDto.getPassword())){
            throw new GlobalException(ResultCodeEnum.PARAMS_FAILED);
        }
        // 查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDto.getUsername());
        User user = userMapper.selectOne(wrapper);
        if(user == null){
            // 用户不存在
            throw new GlobalException(ResultCodeEnum.NOT_FOUND,"用户不存在");
        }
        boolean match = passwordEncoder.match(loginDto.getPassword(), user.getPassword());
        if(!match){
            throw new GlobalException(ResultCodeEnum.LOGIN_FAILED);
        }
        // 生成token，并保存至redis
        String token = UUID.randomUUID().toString();
        // hash 结构-》  token：userId
        // 设置hash结构的某一个field有效期30分钟
        redisTemplate.opsForValue().set(RedisConstants.LOGIN_TOKEN + token,user.getId().toString(),30, TimeUnit.MINUTES);
        return token;
    }

    /**
     * @param loginDto
     * @author: HelloCode.
     * @date: 2023/11/16 15:31
     * @param: loginDto
     * @return: java.lang.String
     * @description: 邮箱快捷登录
     */
    @Override
    public String loginByEmail(LoginDto loginDto) {
        // 参数校验
        if(StringUtils.isEmpty(loginDto.getEmail()) || StringUtils.isEmpty(loginDto.getCode())){
            throw new GlobalException(ResultCodeEnum.PARAMS_FAILED);
        }
        // 验证码校验
        String code = redisTemplate.opsForValue().get(RedisConstants.EMAIL_CODE + loginDto.getEmail());
        if(StringUtils.isEmpty(code) || !code.equals(loginDto.getCode())){
            throw new GlobalException(ResultCodeEnum.LOGIN_FAILED,"邮箱或验证码不正确");
        }
        // 查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, loginDto.getEmail());
        User user = userMapper.selectOne(wrapper);

        if(user == null){
            // 用户不存在，创建新用户
            user = new User();
            user.setUsername(loginDto.getEmail());
            user.setEmail(loginDto.getEmail());
            userMapper.insert(fillDefault(user));
        }
        // 生成token，并保存至redis
        String token = UUID.randomUUID().toString();
        // hash 结构-》  token：userId
        // 设置hash结构的某一个field有效期30分钟
        redisTemplate.opsForValue().set(RedisConstants.LOGIN_TOKEN + token,user.getId().toString(),30, TimeUnit.MINUTES);

        return token;
    }

    /**
     * @param loginDto
     * @author: HelloCode.
     * @date: 2023/11/16 15:31
     * @param: loginDto
     * @return: void
     * @description: 邮箱验证码发送
     */
    @Override
    public void sendEmailCode(LoginDto loginDto) {
        // 参数校验
        if(StringUtils.isEmpty(loginDto.getEmail())){
            throw new GlobalException(ResultCodeEnum.PARAMS_FAILED);
        }
        // 发送邮箱
        String code = RandomStringUtils.randomNumeric(6);
        try {
            EmailUtils.sendEmailCode(loginDto.getEmail(),code);
            String etst = RedisConstants.EMAIL_CODE + loginDto.getEmail();
            // 保存验证码至redis(5分钟有效)
            redisTemplate.opsForValue().set(RedisConstants.EMAIL_CODE + loginDto.getEmail(),code,5, TimeUnit.MINUTES);
        } catch (SendMailException e) {
            throw new GlobalException(ResultCodeEnum.EMAIL_SEND_FAILED);
        }
    }

    /**
     * @author: HelloCode.
     * @date: 2023/11/16 16:48
     * @return: void
     * @description: 用户退出
     */
    @Override
    public void logout(String token) {
        // 获取token
        // 清理redis记录
        redisTemplate.delete(RedisConstants.LOGIN_TOKEN + token);
    }

    /**
     * @author: HelloCode.
     * @date: 2023/10/5 19:23
     * @param: user
     * @return: top.hellocode.entity.User
     * @description: 用户信息脱敏
     */
    private UserVo getSafeUser(User user) {
        if (user == null) {
            return null;
        }
        // 信息脱敏
        user.setPassword(DesensitizedUtil.password(user.getPassword()));
        user.setPhone(DesensitizedUtil.mobilePhone(user.getPhone()));
        user.setEmail(DesensitizedUtil.email(user.getEmail()));
        // 构造 vo 对象
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    /**
     * @author: HelloCode.
     * @date: 2023/10/5 21:46
     * @param: null
     * @return: 填充后的User对象
     * @description: 默认信息填充
     */
    private User fillDefault(User user){
        // 随机用户名
        if(StringUtils.isEmpty(user.getNickname())){
            user.setNickname("yqx_" + RandomUtil.randomString(4));
        }
        // 默认性别
        if(StringUtils.isEmpty(user.getSex())){
            user.setSex(UserConstants.SEX_MAN);
        }
        // 默认头像
        if(StringUtils.isEmpty(user.getAvatar())){
            user.setAvatar(UserConstants.DEFAULT_AVATAR);
        }
        // 默认状态
        if(StringUtils.isEmpty(user.getStatus())){
            user.setStatus(UserConstants.STATUS_OK);
        }
        // 默认逻辑删除标记
        if(StringUtils.isEmpty(user.getDelFlag())){
            user.setDelFlag(UserConstants.DEL_NO);
        }
        // 默认密码
        if(StringUtils.isEmpty(user.getPassword())){
            String pwd = RandomUtil.randomNumbers(6);
            user.setPassword(passwordEncoder.encode(pwd));
        }
        return user;
    }
}
