package top.hellocode.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-16  14:25
 * @Description: 用户登录 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("验证码")
    private String code;
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("头像")
    private String avatar;
}
