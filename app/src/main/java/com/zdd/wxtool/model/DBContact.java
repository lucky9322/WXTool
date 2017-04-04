package com.zdd.wxtool.model;

import org.litepal.crud.DataSupport;

/**
 * @CreateDate: 2017/4/4 下午5:41
 * @Author: lucky
 * @Description:
 * @Version: [v1.0]
 */
public class DBContact extends DataSupport {
    private int id;
    private String name;

    public DBContact() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
