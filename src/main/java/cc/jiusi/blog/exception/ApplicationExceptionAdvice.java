package cc.jiusi.blog.exception;

import cc.jiusi.blog.common.res.Result;
import cc.jiusi.blog.common.res.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionAdvice {

    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public Result<String> globalException(GlobalException e){
        log.error("自定义异常：{}",e.getMessage());
        e.printStackTrace();
        return Result.failed(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> exception(Exception e){
        log.error("系统错误：{}",e.getMessage());
        e.printStackTrace();
        return Result.failed(ResultCodeEnum.FAILED,ResultCodeEnum.FAILED.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public Result<String> duplicateKeyException(DuplicateKeyException e){
        log.error("数据重复：{}",e.getMessage());
        e.printStackTrace();
        return Result.failed(ResultCodeEnum.DOUBLE_PARAMS);
    }
}
