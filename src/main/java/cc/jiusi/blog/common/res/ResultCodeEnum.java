package cc.jiusi.blog.common.res;

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
    EMAIL_SEND_FAILED(5001, "邮件发送失败，请稍后再试"),
    LOGIN_FAILED(4011, "登录失败，用户名或密码不正确"),

    DOUBLE_USERNAME(403,"用户名已经存在"),
    DOUBLE_PARAMS(4033,"数据已经存在"),

    FILE_UPLOAD_FAILED(5002, "文件上传失败"),

    FILE_DELETE_FAILED(5003, "文件删除失败"),
    FILE_NOT_FOUND(4004, "文件不存在"),
    CATEGORY_DELETE_FAILED(5004, "该分类下还关联有子分类，无法删除"),
    ;

    private Integer code;

    private String message;

}
