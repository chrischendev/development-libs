package com.chris.framework.builder.model;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * ObjectBuilder
 * com.chris.framework.builder.model
 * Created by Chris Chen
 * 2018/2/24
 * Explain:用于构建实体类的参数
 */
public class BuildParams {
    private Connection connection;//数据库连接
    private String tableName;//表名
    private String ormPackageName;//orm包名
    private String ormExt;//Orm实体类后缀
    private String ormName;//Orm类名 这个主要用在扩展的时候
    private String xPackageName;//扩展类包名
    private String xClassExt;//扩展实体类后缀 用于创建类的后缀
    private String xExt;//扩展实体类后缀 用于外键字段扩展
    private Map<String, String> dbTypeMap;//用于进行数据库表列类型转换的映射表
    private boolean parseTimeStamp = true;//在列类型转换的时候是否解析TimeStamp，即转换为为Long

    //附加信息
    private String appName = "ChrisApplication";//应用程序名称
    private String author = "Chris Chen";//代码提供者 或者类创建者
    private String time = new SimpleDateFormat("yyyy/MM/dd").format(new Date());//创建时间
    private String explain = "";//类相关说明

    private BuildParams() {
    }

    public BuildParams(Connection connection, String tableName, String ormPackageName, String ormExt) {
        this.connection = connection;
        this.tableName = tableName;
        this.ormPackageName = ormPackageName;
        this.ormExt = ormExt;
    }

    public BuildParams(Connection connection, String tableName, String ormPackageName, String ormExt, String ormName, String xPackageName, String xClassExt, String xExt) {
        this.connection = connection;
        this.tableName = tableName;
        this.ormPackageName = ormPackageName;
        this.ormExt = ormExt;
        this.ormName = ormName;
        this.xPackageName = xPackageName;
        this.xClassExt = xClassExt;
        this.xExt = xExt;
    }

    public BuildParams addInfo(String appName, String author, String time, String explain) {
        if (appName != null) this.appName = appName;
        if (author != null) this.author = author;
        if (time != null) this.time = time;
        if (explain != null) this.explain = explain;
        return this;
    }

    public static BuildParams get() {
        return new BuildParams();
    }

    public Connection getConnection() {
        return connection;
    }

    public BuildParams setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public BuildParams setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public String getOrmName() {
        return ormName;
    }

    public BuildParams setOrmName(String ormName) {
        this.ormName = ormName;
        return this;
    }

    public String getOrmPackageName() {
        return ormPackageName;
    }

    public BuildParams setOrmPackageName(String ormPackageName) {
        this.ormPackageName = ormPackageName;
        return this;
    }

    public String getOrmExt() {
        return ormExt;
    }

    public BuildParams setOrmExt(String ormExt) {
        this.ormExt = ormExt;
        return this;
    }

    public String getxPackageName() {
        return xPackageName;
    }

    public BuildParams setxPackageName(String xPackageName) {
        this.xPackageName = xPackageName;
        return this;
    }

    public String getxClassExt() {
        return xClassExt;
    }

    public BuildParams setxClassExt(String xClassExt) {
        this.xClassExt = xClassExt;
        return this;
    }

    public String getxExt() {
        return xExt;
    }

    public BuildParams setxExt(String xExt) {
        this.xExt = xExt;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public BuildParams setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public BuildParams setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getTime() {
        return time;
    }

    public BuildParams setTime(String time) {
        this.time = time;
        return this;
    }

    public String getExplain() {
        return explain;
    }

    public BuildParams setExplain(String explain) {
        this.explain = explain;
        return this;
    }

    public Map<String, String> getDbTypeMap() {
        return dbTypeMap;
    }

    public BuildParams setDbTypeMap(Map<String, String> dbTypeMap) {
        this.dbTypeMap = dbTypeMap;
        return this;
    }

    public boolean isParseTimeStamp() {
        return parseTimeStamp;
    }

    public BuildParams setParseTimeStamp(boolean parseTimeStamp) {
        this.parseTimeStamp = parseTimeStamp;
        return this;
    }
}
