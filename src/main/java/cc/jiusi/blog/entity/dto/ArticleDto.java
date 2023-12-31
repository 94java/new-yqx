package cc.jiusi.blog.entity.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2023-12-10  16:35
 * @Description: 文章传输对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleDto extends PageQueryDto{
    /**
     * @author: 九思.
     * @date: 2023/12/10 16:37
     * @description: 文章id
     */
    private String id;

    /**
     * @author: 九思.
     * @date: 2023/12/10 16:37
     * @description: 作者id
     */
    private String uid;

    /**
     * @author: 九思.
     * @date: 2023/12/10 16:37
     * @description: 标题
     */
    private String title;

    /**
     * @author: 九思.
     * @date: 2023/12/10 16:37
     * @description: 封面图
     */
    private String img;

    /**
     * @author: 九思.
     * @date: 2023/12/10 16:37
     * @description: 可见性
     */
    private String visibility;

    /**
     * @author: 九思.
     * @date: 2023/12/10 16:37
     * @description: 摘要
     */
    private String remark;

    /**
     * @author: 九思.
     * @date: 2023/12/10 16:37
     * @description: 状态（草稿、已发布）
     */
    private String status;

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:38
     * @description: 分类id
     */
    private String sortId;

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:38
     * @description: 标签id
     */
    private List<String> tagIds;

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:38
     * @description: 浏览量
     */
    private String browse;

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:38
     * @description: 点赞量
     */
    private Integer likes;

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:38
     * @description: 收藏量
     */
    private Integer stars;

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:38
     * @description: 评论量
     */
    private Integer comments;

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:38
     * @description: 封面格式（大图/小图）
     */
    private String imgType;

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:38
     * @description: 评论开关
     */
    private String commentsStatus;

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:38
     * @description: 文章内容（markdown格式）
     */
    private String articleContent;

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:38
     * @description: 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date createTime;

    private Date[] createTimes;

    /**
     * @author: 九思.
     * @date: 2023/12/17 12:38
     * @description: 修改时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date updateTime;

}
