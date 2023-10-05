package top.hellocode.common.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResultCodeEnum {
    SUCCESS(200, "操作成功"),

    PARAMS_FAILED(400, "请求参数异常"),

    UNAUTHORIZED(401, "没有权限访问"),

    NOT_FOUND(404, "数据找不到"),

    FAILED(500, "操作失败，请稍后再试"),

    DOUBLE_USERNAME(400,"用户名已经存在");

    ;

    private Integer code;

    private String message;

}
