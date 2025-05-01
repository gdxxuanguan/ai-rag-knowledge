package com.xuanguan.dev;


import com.xuanguan.dev.api.IAiService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
@Configurable
public class Application {


    public static void main(String[] args) {
        log.info("启动");
        SpringApplication.run(Application.class);

    }

}
