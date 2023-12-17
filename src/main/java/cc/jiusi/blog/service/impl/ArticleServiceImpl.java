package cc.jiusi.blog.service.impl;

import cc.jiusi.blog.common.constants.ArticleConstants;
import cc.jiusi.blog.common.utils.UserContext;
import cc.jiusi.blog.entity.dto.ArticleDto;
import cc.jiusi.blog.entity.po.Article;
import cc.jiusi.blog.entity.po.ArticleContent;
import cc.jiusi.blog.mapper.ArticleContentMapper;
import cc.jiusi.blog.mapper.ArticleMapper;
import cc.jiusi.blog.service.IArticleService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 九思.
 * @since 2023-11-23
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Autowired
    ArticleContentMapper contentMapper;

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:43
     * @param: ArticleDto articleDto
     * @return: void
     * @description: 保存文章内容
     * 没有对应的 id，则说明是新创建文章，如果有id，则说明是修改已有文章内容
     */
    @Override
    public void saveArticle(ArticleDto articleDto) {
        // 判断操作类型：新增/修改
        String articleId = articleDto.getId();

        if(StrUtil.isBlank(articleId)){
            // 是新增，先创建对应的文章基本信息
            Article article = new Article();
            // 填充必要的默认信息
            fillArticleDefaultInfo(article);
            // 保存文章信息
            this.save(article);

            // 获取文章id
            articleId = article.getId();
        }
        // 保存文章内容信息，建立和文章的关联关系
        // TODO: 对文章内容中的图片进行转存
        ArticleContent contentEntity = new ArticleContent();
        contentEntity.setArticleContent(articleDto.getArticleContent());
        contentEntity.setArticleId(articleId);
        contentMapper.insert(contentEntity);
    }

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:57
     * @param:  Article article
     * @return: void
     * @description: 填充文章的默认信息
     */
    private void fillArticleDefaultInfo(Article article) {
        article.setUid(UserContext.getUserId());
        article.setTitle("未命名的文章");
        article.setVisibility(ArticleConstants.ARTICLE_VISIBILITY_ENABLE);
        article.setStatus(ArticleConstants.ARTICLE_STATUS_DRAFT);
        article.setCommentsStatus(ArticleConstants.ARTICLE_COMMENTS_ENABLE);
    }
}
