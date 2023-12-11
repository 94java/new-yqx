package cc.jiusi.blog.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2023-11-17  16:15
 * @Description: TODO
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class TagVo extends BaseVo{
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "标签名")
    private String name;

    @ApiModelProperty(value = "缩略名")
    private String shortName;

}
