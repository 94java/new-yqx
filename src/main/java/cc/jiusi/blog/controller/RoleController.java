package cc.jiusi.blog.controller;


import cc.jiusi.blog.common.res.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author HelloCode.
 * @since 2023-10-05
 */
@RestController
@RequestMapping("/role")
@Api(tags = "角色管理")
public class RoleController {


    /***
     * @author: HelloCode.
     * @date: 2023/11/17 10:50
     * @return: cc.jiusi.blog.common.res.Result
     * @description: 查看角色详情
     */
    @GetMapping("/{id}")
    @ApiOperation("查看角色详情")
    public Result info() {
        return Result.success();
    }

    /***
     * @author: HelloCode.
     * @date: 2023/11/17 10:50
     * @return: cc.jiusi.blog.common.res.Result
     * @description: 分页查看角色列表（带条件查询）
     */
    @PostMapping("/{pageNum}/{pageSize}")
    @ApiOperation("分页查看角色列表")
    public Result<Page> list() {
        return Result.success();
    }

    /***
     * @author: HelloCode.
     * @date: 2023/11/17 10:50
     * @return: cc.jiusi.blog.common.res.Result
     * @description: 新增角色信息
     */
    @PostMapping
    @ApiOperation("新增角色信息")
    public Result save() {
        return Result.success();
    }

    /***
     * @author: HelloCode.
     * @date: 2023/11/17 10:50
     * @return: cc.jiusi.blog.common.res.Result
     * @description: 新增角色信息
     */
    @PutMapping
    @ApiOperation("修改角色信息")
    public Result update() {
        return Result.success();
    }

    /***
     * @author: HelloCode.
     * @date: 2023/11/17 10:50
     * @return: cc.jiusi.blog.common.res.Result
     * @description: 新增角色信息
     */
    @DeleteMapping
    @ApiOperation("删除角色信息")
    public Result delete() {
        return Result.success();
    }

}

