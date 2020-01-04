package com.chris.poi.xls;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Created by Chris Chen
 * 2018/10/08
 * Explain: 行校验助手
 */

public interface XlsCheckRowHelper<T> {

    boolean checkRow(HSSFSheet sheet, Class<T> clazz, HSSFRow row, int rowIndex);

    default void cellCheckHelper(XlsCheckCellHelper xlsCheckCellHelper) {
    }
}
