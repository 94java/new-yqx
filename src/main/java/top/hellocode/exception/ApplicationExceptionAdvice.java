package top.hellocode.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.hellocode.common.res.Result;
import top.hellocode.common.res.ResultCodeEnum;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionAdvice {

    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public Result<String> globalException(GlobalException e){
        log.error("自定义异常：{}",e.getMessage());
        return Result.failed(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> exception(Exception e){
        log.error("系统错误：{}",e.getMessage());
        return Result.failed(ResultCodeEnum.FAILED,ResultCodeEnum.FAILED.getMessage());
    }
}
