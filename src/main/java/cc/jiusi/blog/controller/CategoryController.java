package cc.jiusi.blog.controller;


import cc.jiusi.blog.common.res.Result;
import cc.jiusi.blog.entity.Category;
import cc.jiusi.blog.entity.vo.CategoryVo;
import cc.jiusi.blog.entity.vo.TagVo;
import cc.jiusi.blog.service.ICategoryService;
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
@RequestMapping("/category")
@Api(tags = "分类管理")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @ApiOperation("查询分类详情")
    @GetMapping("/{id}")
    public Result<CategoryVo> info(@PathVariable Long id){
        CategoryVo categoryVo = categoryService.findById(id);
        return Result.success(categoryVo);
    }

    @ApiOperation("查询分类列表")
    @GetMapping("/{pageNum}/{pageSize}")
    public Result<Page<CategoryVo>> list(@PathVariable Integer pageNum, @PathVariable Integer pageSize){
        // 规范参数
        pageNum = pageNum == null? 1 : pageNum;
        pageSize = pageSize == null? 10 : pageSize;

        Page<CategoryVo> page = categoryService.list(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("新增分类")
    @PostMapping
    public Result save(@RequestBody CategoryVo categoryVo) {
        categoryService.saveCategory(categoryVo);
        return Result.success();
    }

    @ApiOperation("修改分类")
    @PutMapping
    public Result update(@RequestBody CategoryVo categoryVo) {
        categoryService.updateCategory(categoryVo);
        return Result.success();
    }

    @ApiOperation("批量删除分类")
    @DeleteMapping
    public Result delete(@RequestBody List<Long> ids) {
        categoryService.removeCategorys(ids);
        return Result.success();
    }
}

