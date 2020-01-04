package com.chris.hbase;

/**
 * Create by Chris Chen
 * Create on 2019-03-08 14:18 星期五
 * This class be use for:
 */
public class User {
    public String code;
    public String name;
    public Integer age;
    public Integer gender;
    public String job;
    public String address;

    public User() {
    }

    public User(String code, String name, Integer age, Integer gender, String job, String address) {
        this.code = code;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.job = job;
        this.address = address;
    }



    @Override
    public String toString() {
        return "User{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", job='" + job + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
