package com.chris.framework.builder.model;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ObjectBuilder
 * com.chris.framework.builder.model
 * Created by Chris Chen
 * 2018/2/24
 * Explain: 用于构建ORM实体类的参数
 */
public class EntityBuildParams {
    private Connection connection;//数据库连接
    private String ormPackageName;//orm包名
    private String ormExt;//Orm实体类后缀
    private Map<String, String> tableNameReplaces = new HashMap<>();//Orm类名需要替换的字符串集合
    private Map<String, String> iptAnnoMap = new HashMap<>();//Orm类中需要添加的注解 key为注解 value为注解类名
    private Map<String, String> dbTypeMap = new HashMap<>();//用于进行数据库表列类型转换的映射表
    private boolean isParseTimeStamp = true;//在列类型转换的时候是否解析TimeStamp，即转换为为Long
    private String fieldModifier = "public";// 字段修饰符

    //附加信息
    private String appName = "ChrisApplication";//应用程序名称
    private String author = "Chris Chen";//代码提供者 或者类创建者
    private String time = new SimpleDateFormat("yyyy/MM/dd").format(new Date());//创建时间
    private String explain = "";//类相关说明

    private EntityBuildParams() {
    }

    public static EntityBuildParams get() {
        return new EntityBuildParams();
    }

    public EntityBuildParams addInfo(String appName, String author, String time, String explain) {
        if (appName != null) this.appName = appName;
        if (author != null) this.author = author;
        if (time != null) this.time = time;
        if (explain != null) this.explain = explain;
        return this;
    }

    public Connection getConnection() {
        return connection;
    }

    public EntityBuildParams setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public String getOrmPackageName() {
        return ormPackageName;
    }

    public EntityBuildParams setOrmPackageName(String ormPackageName) {
        this.ormPackageName = ormPackageName;
        return this;
    }

    public String getOrmExt() {
        return ormExt;
    }

    public EntityBuildParams setOrmExt(String ormExt) {
        this.ormExt = ormExt;
        return this;
    }

    public Map<String, String> getTableNameReplaces() {
        return tableNameReplaces;
    }

    public EntityBuildParams setTableNameReplaces(Map<String, String> tableNameReplaces) {
        this.tableNameReplaces = tableNameReplaces;
        return this;
    }

    public EntityBuildParams addTableNameReplaces(String key, String value) {
        if (this.tableNameReplaces != null)
            this.tableNameReplaces.put(key, value);
        return this;
    }

    public Map<String, String> getIptAnnoMap() {
        return iptAnnoMap;
    }

    public EntityBuildParams addIptAnnoMap(Class clazz) {
        if (this.iptAnnoMap != null)
            this.iptAnnoMap.put(clazz.getSimpleName(), clazz.getName());
        return this;
    }

    public Map<String, String> getDbTypeMap() {
        return dbTypeMap;
    }

    public EntityBuildParams setDbTypeMap(Map<String, String> dbTypeMap) {
        this.dbTypeMap = dbTypeMap;
        return this;
    }

    public boolean isParseTimeStamp() {
        return isParseTimeStamp;
    }

    public EntityBuildParams setParseTimeStamp(boolean parseTimeStamp) {
        isParseTimeStamp = parseTimeStamp;
        return this;
    }

    public String getFieldModifier() {
        return fieldModifier;
    }

    public EntityBuildParams setFieldModifier(String fieldModifier) {
        this.fieldModifier = fieldModifier;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public EntityBuildParams setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public EntityBuildParams setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getTime() {
        return time;
    }

    public EntityBuildParams setTime(String time) {
        this.time = time;
        return this;
    }

    public String getExplain() {
        return explain;
    }

    public EntityBuildParams setExplain(String explain) {
        this.explain = explain;
        return this;
    }
}
