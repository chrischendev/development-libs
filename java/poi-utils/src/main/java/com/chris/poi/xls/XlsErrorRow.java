package com.chris.poi.xls;


import com.chris.poi.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris Chen
 * 2018/10/09
 * Explain: XLS导入校验行数据错误信息
 */

public class XlsErrorRow implements Serializable {
    private int rowIndex;//行号
    private transient List<XlsErrorCell> cellErrorList;//单元格错误信息
    private List<String> rowErrorInfo;//行数据错误集合

    public XlsErrorRow() {
    }

    public XlsErrorRow(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public XlsErrorRow(int rowIndex, List<XlsErrorCell> cellErrorList) {
        this.rowIndex = rowIndex;
        this.cellErrorList = cellErrorList;
    }

    public XlsErrorRow(int rowIndex, List<XlsErrorCell> cellErrorList, List<String> rowErrorInfo) {
        this.rowIndex = rowIndex;
        this.cellErrorList = cellErrorList;
        this.rowErrorInfo = rowErrorInfo;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public List<XlsErrorCell> getCellErrorList() {
        return cellErrorList;
    }

    public void setCellErrorList(List<XlsErrorCell> cellErrorList) {
        this.cellErrorList = cellErrorList;
    }

    public List<String> getRowErrorInfo() {
        return rowErrorInfo;
    }

    public void setRowErrorInfo(List<String> rowErrorInfo) {
        this.rowErrorInfo = rowErrorInfo;
    }


    //增加单元格校验错误信息
    public void addCellErrorInfo(XlsErrorCell xlsErrorCell) {
        if (xlsErrorCell == null) {
            return;
        }
        if (this.cellErrorList == null) {
            this.cellErrorList = new ArrayList<>();
        }
        this.cellErrorList.add(xlsErrorCell);
    }

    //增加行数据错误校验信息
    public void addErrorInfo(String errorInfo) {
        if (StringUtils.isEmpty(errorInfo)) {
            return;
        }
        if (this.rowErrorInfo == null) {
            this.rowErrorInfo = new ArrayList<>();
        }
        this.rowErrorInfo.add(errorInfo);
    }
}
