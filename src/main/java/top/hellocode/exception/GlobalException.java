package top.hellocode.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.hellocode.common.res.ResultCodeEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalException extends RuntimeException{

    private Integer code;

    private String message;

    public GlobalException(ResultCodeEnum errorCodeEnum) {
        super();
        this.code = errorCodeEnum.getCode();
        this.message = errorCodeEnum.getMessage();
    }


    public GlobalException(ResultCodeEnum errorCodeEnum, String message) {
        super();
        this.code = errorCodeEnum.getCode();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
