package cc.jiusi.blog.service;

import cc.jiusi.blog.entity.dto.ArticleDto;
import cc.jiusi.blog.entity.po.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 九思.
 * @since 2023-11-23
 */
public interface IArticleService extends IService<Article> {

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:43
     * @param:  ArticleDto articleDto
     * @return: void
     * @description: 保存文章内容
     */
    @Transactional
    void saveArticle(ArticleDto articleDto);
}
