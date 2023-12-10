package cc.jiusi.blog.entity.po;

import cc.jiusi.blog.entity.BaseBean;
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
@ApiModel(value="Role对象", description="")
public class Role extends BaseBean {
    @ApiModelProperty(value = "角色名")
    private String roleName;

    @ApiModelProperty(value = "备注")
    private String remark;

}
