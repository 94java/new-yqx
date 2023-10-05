package top.hellocode.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.hellocode.common.res.Result;
import top.hellocode.common.res.ResultCodeEnum;

@ControllerAdvice
public class ApplicationExceptionAdvice {

    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public Result<String> globalException(GlobalException e){
        return Result.failed(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> exception(Exception e){
        return Result.failed(ResultCodeEnum.FAILED,e.getMessage());
    }
}
