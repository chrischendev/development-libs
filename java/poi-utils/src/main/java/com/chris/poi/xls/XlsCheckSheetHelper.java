package com.chris.poi.xls;

import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Created by Chris Chen
 * 2018/10/08
 * Explain: 工作表检查
 */

public interface XlsCheckSheetHelper {
    boolean checkSheet(HSSFSheet sheet, int sheetIndex);

    default void rowCheckHelper(XlsCheckRowHelper xlsCheckRowHelper) {
    }
}
