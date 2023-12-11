package cc.jiusi.blog.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2023-12-11  21:43
 * @Description: TODO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseVo implements Serializable {
    protected static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "创建者")
    protected String createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    protected Date createTime;

    @ApiModelProperty(value = "更新者")
    protected String updateBy;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    protected Date updateTime;
}
