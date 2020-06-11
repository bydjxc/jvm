package com.yifeng.jvm.pojo;

/**
 * @author kevin
 * @version v1.0
 * @description
 * @date 2019-10-31 16:47
 **/
public class User {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
