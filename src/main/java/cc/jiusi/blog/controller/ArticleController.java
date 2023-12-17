package cc.jiusi.blog.controller;


import cc.jiusi.blog.common.res.Result;
import cc.jiusi.blog.entity.dto.ArticleDto;
import cc.jiusi.blog.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @description: 文章内容保存
     */
    @RequestMapping("/save")
    public Result<Void> save(@RequestBody ArticleDto articleDto) {
        articleService.saveArticle(articleDto);
        return Result.success();
    }
}

