package com.chris.framework.builder.core.manager;

import com.chris.framework.builder.utils.EntityUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.core
 * Created by Chris Chen
 * 2018/1/22
 * Explain:
 */
public class JdbcManager {
    private static JdbcTemplate jdbcTemplate;

    public static void init(JdbcTemplate jdbcTemplate) {
        JdbcManager.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 用jdbc查询数据
     *
     * @param sql
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> query(String sql, Class<T> clazz) {
        RowMapper<T> rowMapper = new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet resultSet, int i) throws SQLException {
                return EntityUtils.dataToEntity(resultSet, clazz);
            }
        };
        return jdbcTemplate.query(sql, rowMapper);
    }

}
