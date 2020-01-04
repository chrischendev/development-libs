package com.chris.hbase;

import java.sql.Timestamp;

/**
 * Create by Chris Chen
 * Create on 2019-03-08 14:18 星期五
 * This class be use for:
 */
public class UserTest {
    public String name;
    public Timestamp age;

    public UserTest(String name, Timestamp age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getAge() {
        return age;
    }

    public void setAge(Timestamp age) {
        this.age = age;
    }
}
