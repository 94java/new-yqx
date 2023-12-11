package cc.jiusi.blog.entity.vo;

import cc.jiusi.blog.entity.po.Category;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2023-11-17  19:55
 * @Description: 分类 Vo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryVo extends BaseVo{
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "分类名")
    private String name;

    @ApiModelProperty(value = "缩略名")
    private String shortName;

    @ApiModelProperty(value = "父级分类（-1为顶级分类）")
    private String parentId;

    @ApiModelProperty(value = "排序（越小越前）")
    private Integer order;

    @ApiModelProperty(value = "分类图标")
    private String icon;

    @ApiModelProperty(value = "分类状态")
    private String status;

    @ApiModelProperty(value = "子分类")
    private List<CategoryVo> children;
}
