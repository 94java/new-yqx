package cc.jiusi.blog.service.impl;

import cc.jiusi.blog.entity.Article;
import cc.jiusi.blog.mapper.ArticleMapper;
import cc.jiusi.blog.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 九思.
 * @since 2023-11-17
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
