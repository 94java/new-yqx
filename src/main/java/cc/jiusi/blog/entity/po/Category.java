package cc.jiusi.blog.entity.po;

import cc.jiusi.blog.entity.BaseBean;
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
@ApiModel(value="分类对象", description="")
public class Category extends BaseBean {
    @ApiModelProperty(value = "分类名")
    @TableField(value = "`name`")
    private String name;

    @ApiModelProperty(value = "缩略名")
    private String shortName;

    @ApiModelProperty(value = "父级分类（-1为顶级分类）")
    private Long parentId;

    @ApiModelProperty(value = "排序（越小越前）")
    @TableField(value = "`order`")
    private Integer order;

}
