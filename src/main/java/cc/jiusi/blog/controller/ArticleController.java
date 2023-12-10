package cc.jiusi.blog.controller;


import cc.jiusi.blog.common.res.Result;
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
    @RequestMapping("/test")
    public Result test() {
        return Result.success("hello");
    }
}

