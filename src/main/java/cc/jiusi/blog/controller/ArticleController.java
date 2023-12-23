package cc.jiusi.blog.controller;


import cc.jiusi.blog.common.res.Result;
import cc.jiusi.blog.entity.dto.ArticleDto;
import cc.jiusi.blog.entity.vo.ArticleVo;
import cc.jiusi.blog.service.IArticleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 九思.
 * @since 2023-11-23
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private IArticleService articleService;


    /**
     * @author: 九思.
     * @date: 2023/12/17 12:36
     * @return: Result
     * @description: 获取文章分页列表
     */
    @RequestMapping("/page")
    public Result<Page<ArticleVo>> page(@RequestBody ArticleDto articleDto) {
        Page<ArticleVo> list = articleService.getArticlePageList(articleDto);
        return Result.success(list);
    }

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:36
     * @return: Result
     * @description: 文章内容保存，返回文章id
     */
    @RequestMapping("/detail")
    public Result<ArticleVo> detail(@RequestBody ArticleDto articleDto) {
        ArticleVo articleVo = articleService.getArticleDetail(articleDto);
        return Result.success(articleVo);
    }

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:36
     * @return: Result
     * @description: 文章内容保存，返回文章id
     */
    @RequestMapping("/save")
    public Result<String> save(@RequestBody ArticleDto articleDto) {
        String articleId = articleService.saveArticle(articleDto);
        return Result.success(articleId);
    }

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:36
     * @return: Result
     * @description: 修改文章/文章设置
     */
    @RequestMapping("/edit")
    public Result<String> edit(@RequestBody ArticleDto articleDto) {
        articleService.updateArticle(articleDto);
        return Result.success();
    }
}

