package top.hellocode.common.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {

    private Integer code;

    private String message;

    private T data;

    public static <T> Result<T> success(T data){
        return new Result<T>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(){
        return new Result<T>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> failed(Integer code, String message){
        return new Result<T>(code, message, null);
    }

    public static <T> Result<T> failed(ResultCodeEnum errorCodeEnum){
        return new Result<T>(errorCodeEnum.getCode(), errorCodeEnum.getMessage(), null);
    }

    public static <T> Result<T> failed(ResultCodeEnum errorCodeEnum, String message){
        return new Result<T>(errorCodeEnum.getCode(), message, null);
    }
}
