package com.chris.poi.xls;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris Chen
 * 2018/10/09
 * Explain: XLS导入数据校验数据表错误信息
 */

public class XlsErrorSheet implements Serializable {
    private String sheetName;//表名称
    private Integer sheetindex;//表index
    private List<XlsErrorRow> rowErrorList;//行错误信息集合
    private List<String> sheetErrorInfo;//数据表错误

    public XlsErrorSheet() {
    }

    public XlsErrorSheet(String sheetName, Integer sheetindex) {
        this.sheetName = sheetName;
        this.sheetindex = sheetindex;
    }


    public XlsErrorSheet(String sheetName, Integer sheetindex, List<XlsErrorRow> rowErrorList, List<String> sheetErrorInfo) {
        this.sheetName = sheetName;
        this.sheetindex = sheetindex;
        this.rowErrorList = rowErrorList;
        this.sheetErrorInfo = sheetErrorInfo;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Integer getSheetindex() {
        return sheetindex;
    }

    public void setSheetindex(Integer sheetindex) {
        this.sheetindex = sheetindex;
    }

    public List<XlsErrorRow> getRowErrorList() {
        return rowErrorList;
    }

    public void setRowErrorList(List<XlsErrorRow> rowErrorList) {
        this.rowErrorList = rowErrorList;
    }

    public List<String> getSheetErrorInfo() {
        return sheetErrorInfo;
    }

    public void setSheetErrorInfo(List<String> sheetErrorInfo) {
        this.sheetErrorInfo = sheetErrorInfo;
    }

    //添加一条行错误信息
    public void addRowErrorInfo(XlsErrorRow xlsErrorRow) {
        if (this.rowErrorList == null) {
            this.rowErrorList = new ArrayList<>();
        }
        this.rowErrorList.add(xlsErrorRow);
    }

    //添加一条数据表错误
    public void addErrorInfo(String errorInfo) {
        if (this.sheetErrorInfo == null) {
            this.sheetErrorInfo = new ArrayList<>();
        }
        this.sheetErrorInfo.add(errorInfo);
    }

    //构建为txt结果
    public String buildText() {
        List<XlsErrorRow> rowErrorList = getRowErrorList();
        if (rowErrorList == null || rowErrorList.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("导入校验错误有：\r\n");
        rowErrorList.stream().forEach(rowErr -> {
            sb.append("第 ").append(rowErr.getRowIndex()).append(" 行: ");
            List<String> rowErrInfoList = rowErr.getRowErrorInfo();
            if (rowErrInfoList != null && rowErrInfoList.size() > 0) {
                rowErrInfoList.stream().forEach(rowErrInfo -> sb.append(rowErrInfo).append(";"));
            }
            sb.append("\r\n");
        });
        return sb.toString();
    }

    //构建为json结果
    public String buildJson() {
        return new Gson().toJson(this).replace("\"", "'");
    }

    //构建为html结果
    public String buildHtml() {
        List<XlsErrorRow> rowErrorList = getRowErrorList();
        if (rowErrorList == null || rowErrorList.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("<p>导入校验错误有：<br/>");
        rowErrorList.stream().forEach(rowErr -> {
            sb.append("第 ").append(rowErr.getRowIndex()).append(" 行: ");
            List<String> rowErrInfoList = rowErr.getRowErrorInfo();
            if (rowErrInfoList != null && rowErrInfoList.size() > 0) {
                rowErrInfoList.stream().forEach(rowErrInfo -> sb.append(rowErrInfo).append(";"));
            }
            sb.append("<br/>");
        });
        sb.append("</p>");
        return sb.toString();
    }
}
