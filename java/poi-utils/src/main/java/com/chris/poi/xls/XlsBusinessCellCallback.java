package com.chris.poi.xls;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by Chris Chen
 * 2018/09/27
 * Explain: 电子表工作簿设置
 */

public interface XlsBusinessCellCallback<T> {
    /**
     * 在正式处理单元格数据之前执行
     *
     * @param cell 单元格
     * @param val 值
     */
    void preHandleCell(Cell cell, T val);

    /**
     * 在正式处理完单元格数据之后执行
     *
     * @param cell 单元格
     * @param val 值
     */
    void postHandleCell(Cell cell, T val);
}
