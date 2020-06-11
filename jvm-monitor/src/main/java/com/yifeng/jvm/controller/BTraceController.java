package com.yifeng.jvm.controller;

import com.yifeng.jvm.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kevin
 * @version v1.0
 * @description
 * @date 2019-11-07 16:11
 **/
@RestController
public class BTraceController {

    @RequestMapping("/arg1")
    public String arg1(@RequestParam("name") String name){
        return "hello " + name;
    }

    @RequestMapping("/arg2")
    public User arg2(User user){
        return user;
    }

    @RequestMapping("/constructor")
    public User constructor(User user){
        return user;
    }

    @RequestMapping("/same1")
    public String same(@RequestParam("name") String name){
        return "hello " + name;
    }

    @RequestMapping("/same2")
    public String same(@RequestParam("name") String name, @RequestParam("id") Integer id){
        return "hello " + name + "," + id;
    }

    @RequestMapping("/exception")
    public String exception() {
        try {
            System.out.println("start...");
            System.out.println(1/0);
            System.out.println("end...");
        }catch(Exception e) {
            //
        }
        return "success";
    }

}
