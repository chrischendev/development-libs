package com.chris.poi.sample.model;

import com.chris.poi.xls.XlsColumn;
import com.chris.poi.xls.XlsSheet;

/**
 * Created by Chris Chen
 * 2018/11/07
 * Explain:
 */
@XlsSheet(value = "用户", maxLines = 65533)
public class UserXeo {
    @XlsColumn(value = "姓名")
    private String name;
    @XlsColumn(value = "年龄")
    private Integer age;
    @XlsColumn(value = "地址")
    private String address;

    public UserXeo() {
    }

    public UserXeo(String name, Integer age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserXeo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }
}
