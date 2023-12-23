package cc.jiusi.blog.entity.vo;

import cc.jiusi.blog.entity.po.ArticleContent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2023-12-23  15:38
 * @Description: 文章 VO 类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleVo extends BaseVo{
    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 文章id
     */
    private String id;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 作者信息
     */
    private UserVo user;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 标题
     */
    private String title;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 文章内容
     */
    private ArticleContent content;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 封面图
     */
    private String img;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 可见性（隐藏、公开）
     */
    private String visibility;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 摘要
     */
    private String remark;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 状态（草稿、已发布）
     */
    private String status;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 分类信息
     */
    private CategoryVo category;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 标签信息
     */
    private List<TagVo> tagList;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 浏览量
     */
    private String browse;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 点赞量
     */
    private Integer likes;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 收藏量
     */
    private Integer stars;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 评论量
     */
    private Integer comments;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 封面格式（大图/小图）
     */
    private String imgType;

    /**
     * @author: 九思.
     * @date: 2023/12/23 15:41
     * @description: 评论开关
     */
    private String commentsStatus;
}
