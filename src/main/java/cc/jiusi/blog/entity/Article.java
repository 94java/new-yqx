package cc.jiusi.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2023-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Article对象", description="")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "作者id")
    private Long uid;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "封面图")
    private String img;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "类型（原创、转载、翻译）")
    @TableField(value = "`type`")
    private String type;

    @ApiModelProperty(value = "摘要")
    private String remark;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "分类id")
    private Long sortId;

    @ApiModelProperty(value = "浏览量")
    private String browse;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "删除标志")
    @TableLogic
    private String delFlag;

    @ApiModelProperty(value = "点赞量")
    private Integer likes;

    @ApiModelProperty(value = "收藏量")
    private Integer stars;

    @ApiModelProperty(value = "评论量")
    @TableField(value = "`comments`")
    private Integer comments;


    /**
     * @author: 九思.
     * @date: 2023/11/19 15:42
     * @param:  null null
     * @return: null
     * @description: TODO
     *

     *
     *
     *
     *
     *
     */

}
