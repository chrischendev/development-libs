package com.chris.framework.builder.core.manager;

import com.chris.framework.builder.annotation.Expand;
import com.chris.framework.builder.model.Column;
import com.chris.framework.builder.model.Condition;
import com.chris.framework.builder.utils.StringUtils;
import com.chris.framework.builder.utils.TypeUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ObjectBuilder
 * com.chris.framework.builder.core.manager
 * Created by Chris Chen
 * 2018/3/8
 * Explain: 框架超级武器
 */
public class Chris {
    private static EntityManager entityManager;
    private static JdbcTemplate jdbcTemplate;

    /**
     * 初始化
     *
     * @param entityManager
     * @return
     */
    public static Class<QueryManager> setEntityManager(EntityManager entityManager) {
        Chris.entityManager = entityManager;
        return QueryManager.class;
    }

    public static Class<QueryManager> setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        Chris.jdbcTemplate = jdbcTemplate;
        return QueryManager.class;
    }

    /**
     * 根据一个字段KV查询一个基本数据类包含所有字段的json集合
     *
     * @param baseClazz
     * @param id
     * @return
     */
    public static <T> List<String> queryBaseJsonWithId(Class<T> baseClazz, Integer id) {
        if (baseClazz == null || id == null || id == 0) {
            return null;
        }
        //如果这个类没有@Entity注解是不行的
        if (!baseClazz.isAnnotationPresent(Entity.class)) {
            return null;
        }

        //构建CONCAT前半部分
        StringBuilder sql = getConcatSqlStringBuilder(baseClazz);
        sql.append(" WHERE ").append("id").append("=").append(id);
        String sqlStr = sql.toString();
        try {
            List<String> jsonList = getConcatQueryResult(sqlStr);
            return jsonList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据主键和子表关联外键查询一个json集合
     *
     * @param baseClazz
     * @param subTableName
     * @param fkFieldName
     * @param <T>
     * @return
     */
    public static <T> List<String> queryBaseJsonWithFk(Class<T> baseClazz, String subTableName, String fkFieldName) {
        if (baseClazz == null || StringUtils.isEmpty(subTableName) || StringUtils.isEmpty(fkFieldName)) {
            return null;
        }
        //如果这个类没有@Entity注解是不行的
        if (!baseClazz.isAnnotationPresent(Entity.class)) {
            return null;
        }
        //获取基本数据类字段和数据库表的map
        StringBuilder sql = getConcatSqlStringBuilder(baseClazz);
        sql.append(" WHERE ").append("id").append("=");
        sql.append("\'").append(subTableName).append(".").append(fkFieldName).append("\'");

        String sqlStr = sql.toString();

        try {
            List<String> jsonList = getConcatQueryResult(sqlStr);
            return jsonList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据查询条件获得一个完全展开的数据对象json集合
     *
     * @param clazz
     * @param condition
     * @param <T>
     * @return
     */
    public static <T> List<String> queryExpandJson(Class<T> clazz, Condition condition) {
        if (clazz == null || condition == null) {
            return null;
        }
        //如果这个类没有@Entity注解是不行的
        if (!clazz.isAnnotationPresent(Entity.class)) {
            return null;
        }
        if (!clazz.isAnnotationPresent(Expand.class)){
            return null;
        }

        //构建CONCAT前半部分
        StringBuilder sql = getConcatSqlStringBuilder(clazz);
        sql.append(" WHERE ").append(condition.createSql());
        String sqlStr = sql.toString();
        try {
            List<String> jsonList = getConcatQueryResult(sqlStr);
            return jsonList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构建concat查询除条件外的前半部分
     *
     * @param baseClazz
     * @param <T>
     * @return
     */
    private static <T> StringBuilder getConcatSqlStringBuilder(Class<T> baseClazz) {
        //获取表名
        String tableName = QueryManager.getTableName(baseClazz);
        if (tableName == null) {
            return null;
        }
        Map<String, Column> columnMap = TypeUtils.getColumnNameMapFromBaseClass(baseClazz);
        StringBuilder sql = new StringBuilder("SELECT ")
                .append("CONCAT(\"{\",");
        Set<String> keySet = columnMap.keySet();
        for (String fieldName : keySet) {
            Column column = columnMap.get(fieldName);
            String columnName = column.getColumnName();
            sql.append("\"").append(fieldName).append(":\",");//字段名部分，对应json的key
            //如果列类型为字符串，则需要在值的两边添加引号
            boolean isString = String.class.getName().equals(column.getColumnClassName());
            boolean isTimestamp = Timestamp.class.getName().equals(column.getColumnClassName());
            if (isString || isTimestamp) {
                sql.append("\"\'\",").append("IFNULL(")
                        .append(columnName)
                        .append(",\'null\')").append(",\"\'\"");
            } else {
                sql.append("IFNULL(")
                        .append(columnName)
                        .append(",\'null\')");
            }
            sql.append(",");
            sql.append("\",\",");
        }
        sql.delete(sql.lastIndexOf("\",\","), sql.length());//删除最后的","
        sql.append("\"}\") AS resultJson");//添加末尾的括号
        sql.append(" FROM ").append(tableName);//添加表名称
        return sql;
    }

    /**
     * 构建一个通用数据concat查询主体部分
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> StringBuilder getConcatSqlMainStringBuilder(Class<T> clazz) {
        //判断是不是基本数据类
        if (clazz.isAnnotationPresent(Entity.class)){

        }
        //获取表名
        String tableName = QueryManager.getTableName(clazz);
        if (tableName == null) {
            return null;
        }
        Map<String, Column> columnMap = TypeUtils.getColumnNameMapFromBaseClass(clazz);
        StringBuilder sql = new StringBuilder("SELECT ")
                .append("CONCAT(\"{\",");
        Set<String> keySet = columnMap.keySet();
        for (String fieldName : keySet) {
            Column column = columnMap.get(fieldName);
            String columnName = column.getColumnName();
            sql.append("\"").append(fieldName).append(":\",");//字段名部分，对应json的key
            //如果列类型为字符串，则需要在值的两边添加引号
            boolean isString = String.class.getName().equals(column.getColumnClassName());
            boolean isTimestamp = Timestamp.class.getName().equals(column.getColumnClassName());
            if (isString || isTimestamp) {
                sql.append("\"\'\",").append("IFNULL(")
                        .append(columnName)
                        .append(",\'null\')").append(",\"\'\"");
            } else {
                sql.append("IFNULL(")
                        .append(columnName)
                        .append(",\'null\')");
            }
            sql.append(",");
            sql.append("\",\",");
        }
        sql.delete(sql.lastIndexOf("\",\","), sql.length());//删除最后的","
        sql.append("\"}\") AS resultJson");//添加末尾的括号
        sql.append(" FROM ").append(tableName);//添加表名称
        return sql;
    }

    /**
     * 根据一个sql语句查询到一个json的list结果
     *
     * @param sqlStr
     * @return
     * @throws SQLException
     */
    private static List<String> getConcatQueryResult(String sqlStr) throws SQLException {
        RowMapper<String> rowMapper = new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("resultJson");
            }
        };
        List<String> jsonList = jdbcTemplate.query(sqlStr, rowMapper);
        //便利这个集合，把"null"替换为null
        for (int i = 0, len = jsonList.size(); i < len; i++) {
            String json = jsonList.get(i);
            jsonList.set(i, json.replace("\'null\'", "null"));
        }
        return jsonList;
    }

}
