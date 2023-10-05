package top.hellocode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author HelloCode.
 * @since 2023-10-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Question对象", description="")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "类型（单选、多选）")
    private String type;

    @ApiModelProperty(value = "难度")
    private String difficult;

    @ApiModelProperty(value = "图片url")
    private String img;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "题库id")
    private Long subid;

    @ApiModelProperty(value = "题目解析")
    private String parse;

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


}
