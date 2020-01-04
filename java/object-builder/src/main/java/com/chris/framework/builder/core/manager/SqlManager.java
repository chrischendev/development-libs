package com.chris.framework.builder.core.manager;

import com.chris.framework.builder.utils.MsgUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * YdxApiWebApp
 * com.ydx.app.manager
 * Created by Chris Chen
 * 2018/4/6
 * Explain:
 */
public class SqlManager {
    private static Map<String, Object> sqlMap = new HashMap<>();

    public static Map<String, Object> getSqlMap() {
        return SqlManager.sqlMap;
    }

    public static void setSqlMap(Map<String, Object> sqlMap) {
        SqlManager.sqlMap = sqlMap;
    }

    public static boolean putSql(String key, String sql) {
        SqlManager.sqlMap.put(key, sql);
        return true;
    }

    public static String getSql(String key) {
        String sql = (String) SqlManager.sqlMap.get(key);
        MsgUtils.println(sql);
        return sql;
    }
}
