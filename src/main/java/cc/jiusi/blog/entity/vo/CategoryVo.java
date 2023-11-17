package cc.jiusi.blog.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2023-11-17  19:55
 * @Description: 分类 Vo
 */
@Data
public class CategoryVo {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "分类名")
    private String name;

    @ApiModelProperty(value = "缩略名")
    private String shortName;

    @ApiModelProperty(value = "父级分类（-1为顶级分类）")
    private Long parentId;

    @ApiModelProperty(value = "排序（越小越前）")
    private Integer order;
}
