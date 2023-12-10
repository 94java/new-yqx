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
@ApiModel(value="Source对象", description="")
public class Source extends BaseBean {

    @ApiModelProperty(value = "素材名")
    private String name;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "素材类型（图片、文件、视频、pdf等）")
    private String type;

    @ApiModelProperty(value = "md5值")
    private String md5;

    @ApiModelProperty(value = "所属用户id")
    private Long uid;

    @ApiModelProperty(value = "素材真实名（存放在minio 中的命名）")
    private String realName;


}
