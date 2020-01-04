package com.chris.poi.xls;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.lang.reflect.Field;

/**
 * Created by Chris Chen
 * 2018/10/08
 * Explain: 单元格校验助手
 */

public interface XlsCheckCellHelper {
    boolean checkCell(HSSFSheet sheet, Field field, HSSFCell cell, int rowIndex, int columnIndex);
}
