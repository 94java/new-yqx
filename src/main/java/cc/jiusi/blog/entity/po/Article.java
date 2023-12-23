package cc.jiusi.blog.entity.po;

import cc.jiusi.blog.entity.BaseBean;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 九思.
 * @since 2023-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Article对象", description="")
public class Article extends BaseBean {
    @ApiModelProperty(value = "作者id")
    private String uid;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "封面图")
    private String img;

    @ApiModelProperty(value = "可见性（隐藏、公开）")
    private String visibility;

    @ApiModelProperty(value = "摘要")
    private String remark;

    @ApiModelProperty(value = "状态（草稿、已发布）")
    private String status;

    @ApiModelProperty(value = "分类id")
    // TODO： 更改为 categoryId
    private String sortId;

    @ApiModelProperty(value = "浏览量")
    private String browse;

    @ApiModelProperty(value = "点赞量")
    private Integer likes;

    @ApiModelProperty(value = "收藏量")
    private Integer stars;

    @ApiModelProperty(value = "评论量")
    private Integer comments;

    @ApiModelProperty(value = "封面格式（大图/小图）")
    private String imgType;

    @ApiModelProperty(value = "评论开关")
    private String commentsStatus;


}
