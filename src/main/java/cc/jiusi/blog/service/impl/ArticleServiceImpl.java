package cc.jiusi.blog.service.impl;

import cc.jiusi.blog.common.constants.ArticleConstants;
import cc.jiusi.blog.common.res.ResultCodeEnum;
import cc.jiusi.blog.common.utils.UserContext;
import cc.jiusi.blog.entity.dto.ArticleDto;
import cc.jiusi.blog.entity.po.*;
import cc.jiusi.blog.entity.vo.ArticleVo;
import cc.jiusi.blog.entity.vo.CategoryVo;
import cc.jiusi.blog.entity.vo.TagVo;
import cc.jiusi.blog.entity.vo.UserVo;
import cc.jiusi.blog.exception.GlobalException;
import cc.jiusi.blog.mapper.*;
import cc.jiusi.blog.service.IArticleService;
import cc.jiusi.blog.service.ITagService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 九思.
 * @since 2023-11-23
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Autowired
    ArticleContentMapper contentMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ArticleTagMapper articleTagMapper;
    @Autowired
    TagMapper tagMapper;
    @Autowired
    ITagService tagService;

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:43
     * @param: ArticleDto articleDto
     * @return: void
     * @description: 保存文章内容
     * 没有对应的 id，则说明是新创建文章，如果有id，则说明是修改已有文章内容
     */
    @Override
    public String saveArticle(ArticleDto articleDto) {
        // 判断操作类型：新增/修改
        String articleId = articleDto.getId();
        Article dbArticle = articleMapper.selectById(articleId);
        if (dbArticle == null) {
            // 是新增，先创建对应的文章基本信息
            Article article = new Article();
            // 填充必要的默认信息
            fillArticleDefaultInfo(article);
            // 保存文章信息
            this.save(article);
            // 获取文章id
            articleId = article.getId();
        }
        // 根据文章id查询文章内容，如果存在，则修改即可
        LambdaQueryWrapper<ArticleContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotEmpty(articleId), ArticleContent::getArticleId, articleId);
        ArticleContent articleContent = contentMapper.selectOne(queryWrapper);
        // 保存文章内容信息，建立和文章的关联关系
        // TODO: 对文章内容中的图片进行转存
        if (articleContent != null) {
            // 修改内容
            articleContent.setArticleContent(articleDto.getArticleContent());
            contentMapper.updateById(articleContent);
            return articleId;
        }
        // 新增内容
        articleContent = new ArticleContent();
        articleContent.setArticleContent(articleDto.getArticleContent());
        articleContent.setArticleId(articleId);
        contentMapper.insert(articleContent);
        return articleId;
    }

    /**
     * @param articleDto
     * @author: 九思.
     * @date: 2023/12/23 15:46
     * @param: ArticleDto articleDto
     * @return: ArticleVo
     * @description: 获取文章详情
     */
    @Override
    public ArticleVo getArticleDetail(ArticleDto articleDto) {
        String articleId = articleDto.getId();
        ArticleVo articleVo = new ArticleVo();
        if (StrUtil.isEmpty(articleId)) {
            return articleVo;
        }
        // 查询文章信息
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return articleVo;
        }
        BeanUtil.copyProperties(article, articleVo);
        getAssociatedInfo(articleVo, article);
        return articleVo;
    }

    /**
     * @author: 九思.
     * @date: 2023/12/23 21:41
     * @param: ArticleVo articleVo
     * @param: Article article
     * @return: void
     * @description: 填充文章关联信息
     */
    private void getAssociatedInfo(ArticleVo articleVo, Article article) {
        String articleId = article.getId();
        // 查询文章内容
        LambdaQueryWrapper<ArticleContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotEmpty(articleId), ArticleContent::getArticleId, articleId);
        ArticleContent content = contentMapper.selectOne(queryWrapper);
        articleVo.setContent(content);
        // 查询作者信息
        User user = userMapper.selectById(article.getUid());
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user, userVo);
        articleVo.setUser(userVo);
        // 查询分类信息
        Category category = categoryMapper.selectById(article.getSortId());
        CategoryVo categoryVo = new CategoryVo();
        BeanUtil.copyProperties(category, categoryVo);
        articleVo.setCategory(categoryVo);
        // 查询文章-标签中间表信息
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, articleId);
        List<ArticleTag> articleTags =
                articleTagMapper.selectList(articleTagLambdaQueryWrapper);
        List<String> tagIds = articleTags.stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());
        if (tagIds.size() == 0) {
            return;
        }
        // 查询标签信息
        List<Tag> tags = tagMapper.selectBatchIds(tagIds);
        List<TagVo> tagVos = new ArrayList<>();
        tags.forEach(item -> {
            TagVo tagVo = new TagVo();
            BeanUtil.copyProperties(item, tagVo);
            tagVos.add(tagVo);
        });
        articleVo.setTagList(tagVos);
    }

    /**
     * @param articleDto
     * @author: 九思.
     * @date: 2023/12/23 15:46
     * @param: ArticleDto articleDto
     * @return: ArticleVo
     * @description: 修改文章信息（不包含内容）
     */
    @Override
    public void updateArticle(ArticleDto articleDto) {
        String articleId = articleDto.getId();
        if (articleId == null) {
            throw new GlobalException(ResultCodeEnum.PARAMS_FAILED);
        }
        // 根据id查询文章信息
        Article article = articleMapper.selectById(articleId);
        // 拷贝内容
        BeanUtil.copyProperties(articleDto, article, CopyOptions.create().ignoreNullValue());
        // 处理标签信息
        List<String> tagIds = articleDto.getTagIds();
        if (CollUtil.isNotEmpty(tagIds)) {
            // 先删除原有的标签
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, articleId);
            articleTagMapper.delete(articleTagLambdaQueryWrapper);
            // 再保存新的标签
            tagIds.forEach(item -> {
                // 1. 根据前端的标签id查询
                Tag tag = tagService.getById(item);
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                if (tag == null) {
                    // 若不存在，说明是新标签，直接把id作为标签名进行保存
                    // 存储标签
                    TagVo tagVo = new TagVo();
                    tagVo.setName(item);
                    item = tagService.saveTag(tagVo);
                }
                // 存在，直接保存数据到中间表即可
                articleTag.setTagId(item);
                articleTagMapper.insert(articleTag);
            });
        }
        // 修改文章信息
        articleMapper.updateById(article);
    }

    /**
     * @param articleDto
     * @author: 九思.
     * @date: 2023/12/23 15:46
     * @param: ArticleDto articleDto
     * @return: ArticleVo
     * @description: 获取文章分页列表
     */
    @Override
    public Page<ArticleVo> getArticlePageList(ArticleDto articleDto) {
        // 获取分页参数
        long pageNum = articleDto.getPageNum() == null ? 1L : articleDto.getPageNum();
        long pageSize = articleDto.getPageSize() == null ? 10L : articleDto.getPageSize();
        // 查询文章
        Page<Article> page = new Page<>(pageNum, pageSize);
        // 构造查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 文章名
        queryWrapper.like(StrUtil.isNotEmpty(articleDto.getTitle()), Article::getTitle, articleDto.getTitle());
        // 文章状态
        queryWrapper.eq(StrUtil.isNotEmpty(articleDto.getStatus()), Article::getStatus, articleDto.getStatus());
        // 文章分类
        queryWrapper.eq(StrUtil.isNotEmpty(articleDto.getSortId()), Article::getSortId, articleDto.getSortId());
        // 标签
        if (CollUtil.isNotEmpty(articleDto.getTagIds())) {
            LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(ArticleTag::getTagId, articleDto.getTagIds());
            List<ArticleTag> articleTags = articleTagMapper.selectList(wrapper);
            List<String> articleIds = articleTags.stream().map(ArticleTag::getArticleId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(articleIds)) {
                queryWrapper.in(Article::getId, articleIds);
            }
        }
        // 创建时间
        if (articleDto.getCreateTimes() != null && articleDto.getCreateTimes().length == 2) {
            queryWrapper.between(Article::getCreateTime, articleDto.getCreateTimes()[0], articleDto.getCreateTimes()[1]);
        }
        // 查询
        articleMapper.selectPage(page, queryWrapper);
        // 关联信息处理
        List<ArticleVo> voList = page.getRecords().stream().map(item -> {
            ArticleVo articleVo = new ArticleVo();
            BeanUtils.copyProperties(item, articleVo);
            getAssociatedInfo(articleVo, item);
            return articleVo;
        }).collect(Collectors.toList());
        Page<ArticleVo> articleVoPage = new Page<>();
        BeanUtil.copyProperties(page, articleVoPage);
        articleVoPage.setRecords(voList);
        return articleVoPage;
    }

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:57
     * @param: Article article
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
