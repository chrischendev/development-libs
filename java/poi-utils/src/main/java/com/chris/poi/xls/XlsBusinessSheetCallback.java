package com.chris.poi.xls;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Created by Chris Chen
 * 2018/09/27
 * Explain: 电子表工作簿设置
 */

public interface XlsBusinessSheetCallback<T> {

    /**
     * 处理工作表数据之前执行
     *
     * @param workSheet 工作表
     * @param sheetData 工作表数据
     */
    void preHandleSheet(Sheet workSheet, XlsDataSheet<T> sheetData);

    /**
     * 处理工作表数据之后执行
     *
     * @param workSheet 工作表
     * @param sheetData 工作表数据
     */
    void postHandleSheet(Sheet workSheet, XlsDataSheet<T> sheetData);

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
