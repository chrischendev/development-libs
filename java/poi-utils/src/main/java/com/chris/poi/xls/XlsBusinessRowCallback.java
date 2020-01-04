package com.chris.poi.xls;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Created by Chris Chen
 * 2018/09/27
 * Explain: 电子表工作簿设置
 */

public interface XlsBusinessRowCallback<T> {

    /**
     * 在正式处理行数据之前执行
     *
     * @param row 行
     * @param data 行数据
     */
    void preHandleRow(Row row, T data);

    /**
     * 在正式处理完行数据之后执行
     *
     * @param row 行
     * @param data 行数据
     */
    void postHandleRow(Row row, T data);
}
