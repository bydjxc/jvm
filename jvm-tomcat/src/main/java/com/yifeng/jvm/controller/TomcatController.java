package com.yifeng.jvm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kevin
 * @version v1.0
 * @description
 * @date 2019-11-12 15:20
 **/
@RestController
@RequestMapping("/tomcat")
public class TomcatController {

    @RequestMapping("/hello")
    public String hello(){
        String str = "";
        for (int i = 0; i < 10; i++) {
            str += i;
        }
        return str;
    }
}
