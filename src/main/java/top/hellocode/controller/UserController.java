package top.hellocode.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.hellocode.common.res.Result;
import top.hellocode.entity.User;
import top.hellocode.entity.dto.LoginDto;
import top.hellocode.entity.dto.UserSearchDto;
import top.hellocode.entity.vo.UserVo;
import top.hellocode.service.IUserService;

/**
 * <p>
 *  前端控制器
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
    public Result<UserVo> info(@PathVariable("id")Long id){
        UserVo user = userService.getUserInfoById(id);
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
    public Result<Page> list(
            @PathVariable("pageNum")Integer pageNum,
            @PathVariable("pageSize")Integer pageSize,
            @RequestBody UserSearchDto user){
        // 默认值设置
        pageNum = ObjectUtil.isNotNull(pageNum) ? pageNum : 1;
        pageSize = ObjectUtil.isNotNull(pageSize) ? pageSize : 10;

        Page<User> userPage = userService.list(pageNum, pageSize, user);
        return Result.success(userPage);
    }

    @PostMapping
    @ApiOperation("用户注册")
    public Result register(@RequestBody LoginDto user){
        userService.register(user);
        return Result.success();
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<String> login(@RequestBody LoginDto loginDto){
        String token = null;
        // 账号登录
        if(StringUtils.isNotBlank(loginDto.getUsername())){
            token = userService.loginByUsername(loginDto);
        }else if(StringUtils.isNotBlank(loginDto.getEmail())){
            // 快捷登录
            token = userService.loginByEmail(loginDto);
        }

        return Result.success(token);
    }

    @PostMapping("/code")
    @ApiOperation("验证码发送")
    public Result<String> sendCode(@RequestBody LoginDto loginDto){
        userService.sendEmailCode(loginDto);
        return Result.success();
    }

    @PostMapping("/logout")
    @ApiOperation("用户退出")
    public Result logout(@RequestHeader("token") String token){
        userService.logout(token);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改用户信息")
    public Result update(@RequestBody User user){
        userService.update(user);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除用户信息")
    public Result delete(Long id){
        userService.removeById(id);
        return Result.success();
    }
}

