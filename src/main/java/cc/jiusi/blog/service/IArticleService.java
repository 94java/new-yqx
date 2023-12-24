package cc.jiusi.blog.service;

import cc.jiusi.blog.entity.dto.ArticleDto;
import cc.jiusi.blog.entity.po.Article;
import cc.jiusi.blog.entity.vo.ArticleVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    String saveArticle(ArticleDto articleDto);

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:46
     * @param:  ArticleDto articleDto
     * @return: ArticleVo
     * @description: 获取文章详情
     */
    ArticleVo getArticleDetail(ArticleDto articleDto);

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:46
     * @param:  ArticleDto articleDto
     * @return: ArticleVo
     * @description: 修改文章信息（不包含内容）
     */
    @Transactional
    void updateArticle(ArticleDto articleDto);

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:46
     * @param:  ArticleDto articleDto
     * @return: ArticleVo
     * @description: 获取文章分页列表
     */
    Page<ArticleVo> getArticlePageList(ArticleDto articleDto);

    /**
     * @author: 九思.
     * @date: 2023/12/24 10:59
     * @param:  ArticleDto articleDto
     * @return: void
     * @description: 发布文章
     */
    void publishArticle(ArticleDto articleDto);
}
