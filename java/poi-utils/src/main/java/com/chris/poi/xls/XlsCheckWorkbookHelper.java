package com.chris.poi.xls;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created by Chris Chen
 * 2018/10/08
 * Explain: 工作簿检查
 */

public interface XlsCheckWorkbookHelper {
    boolean checkWorkbook(HSSFWorkbook workbook);

    default void sheetCheckHelper(XlsCheckSheetHelper xlsCheckSheetHelper) {
    }
}
