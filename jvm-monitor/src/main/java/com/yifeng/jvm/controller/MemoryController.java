package com.yifeng.jvm.controller;

import com.yifeng.jvm.pojo.User;
import com.yifeng.jvm.util.Metaspace;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author kevin
 * @version v1.0
 * @description
 * @date 2019-10-31 16:42
 **/
@Controller
public class MemoryController {

    private List<User> userList = new ArrayList<>();
    private List<Class> classList = new ArrayList<>();
    /**
     * -Xmx32M -Xms32M
     **/
    @GetMapping("/heap")
    public String heap(){
        System.out.println("堆内存溢出进入");
        int i = 0;
        while (true){
            userList.add(new User(i++, UUID.randomUUID().toString()));
        }
    }


    /**
     * -XX:MetaspaceSize=32M -XX:MaxMetaspaceSize=32M
     **/

    @GetMapping("/nonheap")
    public String nonHeap(){
        System.out.println("非堆内存溢出进入");
        int i = 0;
        while (true){
            classList.addAll(Metaspace.createClasses());
        }
    }

}
