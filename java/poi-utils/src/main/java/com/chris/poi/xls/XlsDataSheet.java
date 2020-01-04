package com.chris.poi.xls;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chris Chen
 * 2018/09/17
 * Explain: 工作表数据信息
 */
public class XlsDataSheet<T> implements Serializable {
    private String title = "sheet";//工作表名称
    private int index = -1;//工作表索引号
    private List<T> dataList;//工作表中每行的数据
    private transient Class<T> clazz;

    private static transient final int MAXLINES = 65534;

    private transient XlsBusinessSheetCallback xlsBusinessSheetCallback;

    private XlsDataSheet() {

    }

    public static <T> XlsDataSheet get(Class<T> clazz) {
        XlsDataSheet<T> xlsDataSheet = new XlsDataSheet<>();
        xlsDataSheet.clazz = clazz;
        return xlsDataSheet;
    }

    public String getTitle() {
        return title;
    }

    public XlsDataSheet setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public XlsDataSheet setIndex(int index) {
        this.index = index;
        return this;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public XlsDataSheet setDataList(List<T> dataList) {
        if (dataList != null && dataList.size() > getMaxLines()) {
            throw new RuntimeException("One page can not contains data than " + MAXLINES + "lines.");
            //大量数据的分拆在工作簿数据中操作
        }
        this.dataList = dataList;
        return this;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public XlsDataSheet setClazz(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }

    public XlsBusinessSheetCallback getXlsBusinessSheetCallback() {
        return this.xlsBusinessSheetCallback;
    }

    public void setXlsBusinessSheetCallback(XlsBusinessSheetCallback callback) {
        this.xlsBusinessSheetCallback = callback;
    }

    /**
     * 获取设定的最大行数
     *
     * @return
     */
    private int getMaxLines() {
        XlsSheet xlsSheet = getClazz().getAnnotation(XlsSheet.class);
        if (xlsSheet == null) {
            return MAXLINES;
        }
        int ml = xlsSheet.maxLines();
        if (ml > 0 && ml < 65534) {
            return ml;
        }
        return MAXLINES;
    }

    //导出
//    public void export(String fileName, HttpServletResponse response) {
//        //构建响应头
//        XlsIOUtils.buildeExportResponse(fileName, response);
//        OutputStream os = null;
//        try {
//            if (os == null) {
//                os = response.getOutputStream();
//            }
//            XlsUtils.exportToXlsOutputStream(this, os);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
