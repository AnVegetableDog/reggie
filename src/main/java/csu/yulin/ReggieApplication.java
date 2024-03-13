package csu.yulin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
//@ServletComponentScan 注解的作用是启用 Spring Boot
//对 Servlet、Filter 和 Listener 的自动扫描和注册
@ServletComponentScan
@EnableTransactionManagement

public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("项目启动成功!!");
    }
}