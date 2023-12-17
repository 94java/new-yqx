package cc.jiusi.blog.entity.po;

import cc.jiusi.blog.entity.BaseBean;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@ApiModel(value = "ArticleContent对象", description = "")
public class ArticleContent {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    protected String id;

    @ApiModelProperty(value = "文章id")
    private String articleId;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

}
