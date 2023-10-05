package top.hellocode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.hellocode.mapper")
public class BootDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootDevApplication.class, args);
    }

}
