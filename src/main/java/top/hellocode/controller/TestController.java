package top.hellocode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/test")
@Slf4j
@Api(tags = "测试 Swagger")
public class TestController {

    @ApiOperation("测试")
    @GetMapping("/hello")
    public String hello() {
        log.info("Hello");
        return "hello springboot";
    }
}
