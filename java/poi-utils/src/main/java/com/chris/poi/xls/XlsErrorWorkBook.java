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

public class XlsErrorWorkBook implements Serializable {
    private List<XlsErrorSheet> sheetErrorList;//工作表错误信息集合
    private List<String> workbookErrorInfo;//工作簿错误
    private transient boolean errorFlag;//有错误的标记

    public XlsErrorWorkBook() {
    }

    public XlsErrorWorkBook(List<XlsErrorSheet> sheetErrorList, List<String> workbookErrorInfo) {
        this.sheetErrorList = sheetErrorList;
        this.workbookErrorInfo = workbookErrorInfo;
    }

    //添加一条数据表错误信息
    public void addSheetErrorInfo(XlsErrorSheet xlsErrorSheet) {
        if (this.sheetErrorList == null) {
            this.sheetErrorList = new ArrayList<>();
        }
        this.sheetErrorList.add(xlsErrorSheet);
    }

    public List<XlsErrorSheet> getSheetErrorList() {
        return sheetErrorList;
    }

    public void setSheetErrorList(List<XlsErrorSheet> sheetErrorList) {
        this.sheetErrorList = sheetErrorList;
    }

    public List<String> getWorkbookErrorInfo() {
        return workbookErrorInfo;
    }

    public void setWorkbookErrorInfo(List<String> workbookErrorInfo) {
        this.workbookErrorInfo = workbookErrorInfo;
    }

    //添加一条数据表错误
    public void addErrorInfo(String errorInfo) {
        if (this.workbookErrorInfo == null) {
            this.workbookErrorInfo = new ArrayList<>();
        }
        this.workbookErrorInfo.add(errorInfo);
    }

    //构建为txt结果
    public String buildText() {
        List<XlsErrorSheet> rowErrorList = getSheetErrorList();
        if (rowErrorList == null || rowErrorList.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("导入校验错误有：\r\n");
        rowErrorList.stream().forEach(rowErr -> {
            sb.append(rowErr.buildText());
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
        List<XlsErrorSheet> rowErrorList = getSheetErrorList();
        if (rowErrorList == null || rowErrorList.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("<p>导入校验错误有：<br/>");
        rowErrorList.stream().forEach(rowErr -> {
            sb.append(rowErr.buildHtml());
            sb.append("<br/>");
        });
        sb.append("</p>");
        return sb.toString();
    }
}
