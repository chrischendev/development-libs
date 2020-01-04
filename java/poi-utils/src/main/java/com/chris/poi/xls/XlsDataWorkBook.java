package com.chris.poi.xls;


import com.chris.poi.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris Chen
 * 2018/09/27
 * Explain: 工作簿数据信息
 */

public class XlsDataWorkBook implements Serializable {
    private String name;//工作簿名称 即文件名
    private transient Map<Class<?>, Integer> sheetIndexMap;//表索引当前最大重复序号
    private transient Integer maxLines = 65534;//每张表的最大行数

    private List<XlsDataSheet> xlsDataSheetList;
    private transient XlsBusinessWorkBookCallback xlsBusinessWorkBookCallback;


    public XlsDataWorkBook() {
    }

    public static XlsDataWorkBook get() {
        return new XlsDataWorkBook();
    }

    public String getName() {
        return name;
    }

    public XlsDataWorkBook setName(String name) {
        this.name = name;
        return this;
    }

    public List<XlsDataSheet> getXlsDataSheetList() {
        return xlsDataSheetList;
    }

    public XlsDataWorkBook setXlsDataSheetList(List<XlsDataSheet> xlsDataSheetList) {
        this.xlsDataSheetList = xlsDataSheetList;
        return this;
    }

    public XlsDataWorkBook addXlsWorkSheetData(XlsDataSheet xlsDataSheet) {
        if (xlsDataSheet == null) {
            return this;
        }
        if (this.xlsDataSheetList == null) {
            this.xlsDataSheetList = new ArrayList<>();
        }
        this.xlsDataSheetList.add(xlsDataSheet);
        return this;
    }

    public XlsBusinessWorkBookCallback getXlsBusinessWorkBookCallback() {
        return this.xlsBusinessWorkBookCallback;
    }

    public Integer getMaxLines() {
        return maxLines;
    }

    public XlsDataWorkBook setMaxLines(Integer maxLines) {
        this.maxLines = maxLines;
        return this;
    }

    public XlsDataWorkBook setXlsBusinessWorkBookCallback(XlsBusinessWorkBookCallback callback) {
        this.xlsBusinessWorkBookCallback = callback;
        return this;
    }

    //同类索引自增
    public XlsDataWorkBook indexIncrease(Class<?> clazz) {
        if (this.sheetIndexMap == null) {
            sheetIndexMap = new HashMap<>();
        }
        if (!sheetIndexMap.containsKey(clazz)) {
            this.sheetIndexMap.put(clazz, 1);
            return this;
        }
        Integer sheetIndex = sheetIndexMap.get(clazz);
        if (sheetIndex == null) {
            this.sheetIndexMap.put(clazz, 1);
            return this;
        }
        //加一
        this.sheetIndexMap.put(clazz, sheetIndex + 1);
        return this;
    }

    public <T> XlsDataWorkBook addDataList(Class<T> clazz, String baseSheetName, List<T> dataList) {
        return addDataList(clazz, baseSheetName, dataList, null);
    }

    //增加数据
    public <T> XlsDataWorkBook addDataList(Class<T> clazz, String baseSheetName, List<T> dataList, XlsBusinessSheetCallback callback) {
        if (clazz == null || StringUtils.isEmpty(baseSheetName) || dataList == null) {
            return this;
        }
        //判断数据长度
        int size = dataList.size();
        if (size > maxLines) {
            //数据拆两部分
            ////符合要求的数据
            List<T> list1 = dataList.subList(0, maxLines);
            List<T> list2 = dataList.subList(maxLines, size);

            //递归添加数据
            addDataList(clazz, baseSheetName, list1, callback);
            //索引自增
            indexIncrease(clazz);
            addDataList(clazz, baseSheetName, list2, callback);
        } else {
            //创建表数据
            Integer sheetIndex = getSheetIndex(clazz);
            XlsDataSheet sheetData = XlsDataSheet.get(clazz)
                    .setTitle(baseSheetName + ((sheetIndex == null || sheetIndex == 0) ? "" : sheetIndex))
                    .setDataList(dataList);
            if (callback != null) {
                sheetData.setXlsBusinessSheetCallback(callback);
            }
            addXlsWorkSheetData(sheetData);
        }
        return this;
    }

    private Integer getSheetIndex(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        if (this.sheetIndexMap == null) {
            this.sheetIndexMap = new HashMap<>();
        }
        return this.sheetIndexMap.get(clazz);
    }

    //导出
//    public void export(String fileName, HttpServletResponse response) {
//        //构建响应头
//        XlsIOUtils.buildeExportResponse(fileName, response);
//        ServletOutputStream os = null;
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
