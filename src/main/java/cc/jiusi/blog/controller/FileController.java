package cc.jiusi.blog.controller;

import cc.jiusi.blog.common.res.Result;
import cc.jiusi.blog.common.utils.MinioUtils;
import cc.jiusi.blog.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2023-11-23  17:30
 * @Description: 文件控制器
 */
@RestController
@RequestMapping("/upload")
@Api(tags = "文件上传")
public class FileController {
    @Autowired
    private IFileService fileService;

    /**
     * @author: 九思.
     * @date: 2023/11/23 17:37
     * @param:  MultipartFile file
     * @return: Result
     * @description: 文件上传
     */
    @PostMapping
    @ApiOperation("上传文件")
    public Result upload(MultipartFile file){
        fileService.upload(file);
        return Result.success();
    }

    /**
     * @author: 九思.
     * @date: 2023/11/23 17:37
     * @param:  MultipartFile file
     * @return: Result
     * @description: 删除文件
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除文件")
    public Result delete(@PathVariable("id") Long id){
        fileService.delete(id);
        return Result.success();
    }
}
