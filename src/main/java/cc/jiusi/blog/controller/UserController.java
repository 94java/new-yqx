package cc.jiusi.blog.controller;


import cc.jiusi.blog.common.annotations.Authentication;
import cc.jiusi.blog.common.constants.SysConstants;
import cc.jiusi.blog.common.res.Result;
import cc.jiusi.blog.common.utils.UserContext;
import cc.jiusi.blog.entity.po.User;
import cc.jiusi.blog.entity.dto.LoginDto;
import cc.jiusi.blog.entity.vo.UserVo;
import cc.jiusi.blog.service.IUserService;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cc.jiusi.blog.entity.dto.UserSearchDto;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author HelloCode.
 * @since 2023-10-05
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户管理")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/{id}")
    @ApiOperation("获取用户详细信息")
    // @Authentication
    public Result<UserVo> info(@PathVariable("id") String id) {
        UserVo user = userService.getUserInfoById(id);
        return Result.success(user);
    }

    @GetMapping("/currentUserInfo")
    @ApiOperation("获取当前登录用户信息")
    @Authentication
    public Result<UserVo> currentUserInfo() {
        String userId = UserContext.getUserId();
        UserVo user = userService.getUserInfoById(userId);
        return Result.success(user);
    }

    /**
     * @author: HelloCode.
     * @date: 2023/10/5 19:59
     * @param: null
     * @return: 分页用户列表
     * @description: 分页获取用户列表（含条件查询）
     */
    @PostMapping("/{pageNum}/{pageSize}")
    @ApiOperation("分页获取用户")
    @Authentication
    public Result<Page> list(
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("pageSize") Integer pageSize,
            @RequestBody UserSearchDto user) {
        // 默认值设置
        pageNum = ObjectUtil.isNotNull(pageNum) ? pageNum : 1;
        pageSize = ObjectUtil.isNotNull(pageSize) ? pageSize : 10;

        Page<User> userPage = userService.list(pageNum, pageSize, user);
        return Result.success(userPage);
    }

    @PostMapping
    @ApiOperation("用户注册")
    public Result register(@RequestBody LoginDto user) {
        userService.register(user);
        return Result.success();
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<Map> login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        String token = null;
        // 账号登录
        if (StringUtils.isNotBlank(loginDto.getUsername())) {
            token = userService.loginByUsername(loginDto, request);
        } else if (StringUtils.isNotBlank(loginDto.getEmail())) {
            // 快捷登录
            token = userService.loginByEmail(loginDto, request);
        }
        // 为方便前端操作，包装token返回
        Map<String,String> res = new HashMap<>();
        res.put("token",token);

        return Result.success(res);
    }

    @PostMapping("/code")
    @ApiOperation("验证码发送")
    public Result<String> sendCode(@RequestBody LoginDto loginDto) {
        userService.sendEmailCode(loginDto);
        return Result.success();
    }

    @PostMapping("/logout")
    @ApiOperation("用户退出")
    public Result logout(@RequestHeader(SysConstants.USER_TOKEN_HEADER) String token) {
        userService.logout(token);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改用户信息")
    @Authentication
    public Result update(@RequestBody User user) {
        userService.update(user);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除用户信息")
    @Authentication
    public Result delete(Long id) {
        userService.removeById(id);
        return Result.success();
    }
}

