package com.yifeng.jvm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author kevin
 * @version v1.0
 * @description
 * @date 2019-11-12 15:19
 **/
@SpringBootApplication
public class JvmTomcatApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(JvmTomcatApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(JvmTomcatApplication.class);
    }
}
