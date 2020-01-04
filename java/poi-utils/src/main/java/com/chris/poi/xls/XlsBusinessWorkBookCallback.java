package com.chris.poi.xls;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created by Chris Chen
 * 2018/09/27
 * Explain: 电子表工作簿设置
 */

public interface XlsBusinessWorkBookCallback {
    /**
     * 处理工作簿数据之前执行
     *
     * @param workbook 工作簿
     */
    void preHandleWorkBook(HSSFWorkbook workbook);

    /**
     * 处理工作簿数据之后执行
     *
     * @param workbook 工作簿
     */
    void postHandleWorkBook(HSSFWorkbook workbook);

}
