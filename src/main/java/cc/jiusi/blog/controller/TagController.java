package cc.jiusi.blog.controller;


import cc.jiusi.blog.common.res.Result;
import cc.jiusi.blog.entity.Tag;
import cc.jiusi.blog.entity.vo.TagVo;
import cc.jiusi.blog.service.ITagService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 九思.
 * @since 2023-11-17
 */
@RestController
@RequestMapping("/tag")
@Api(tags = "标签管理")
public class TagController {
    @Autowired
    private ITagService tagService;

    @ApiOperation("查询标签详情")
    @GetMapping("/{id}")
    public Result<TagVo> info(@PathVariable("id") Long id) {
        TagVo tag = tagService.findById(id);
        return Result.success(tag);
    }

    @ApiOperation("查询标签列表")
    @GetMapping("/{pageNum}/{pageSize}")
    public Result<Page<TagVo>> list(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        // 规范参数
        pageNum = pageNum == null? 1 : pageNum;
        pageSize = pageSize == null? 10 : pageSize;
        // 查询
        Page<TagVo> page = tagService.list(pageNum,pageSize);
        return Result.success(page);
    }

    @ApiOperation("新增标签")
    @PostMapping
    public Result save(@RequestBody TagVo tag) {
        tagService.saveTag(tag);
        return Result.success();
    }

    @ApiOperation("修改标签")
    @PutMapping
    public Result update(@RequestBody TagVo tag) {
        tagService.updateTag(tag);
        return Result.success();
    }

    @ApiOperation("删除标签")
    @DeleteMapping
    public Result delete(@RequestBody List<Long> ids) {
        tagService.removeTags(ids);
        return Result.success();
    }

}

