package com.chris.servlet.poi;


import com.chris.poi.enums.XlsType;
import com.chris.poi.xls.XlsDataWorkBook;
import com.chris.poi.xls.XlsUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Chris Chen
 * 2018/10/18
 * Explain: XLS导入导出工具
 */

public class XlsIOUtils {

    /**
     * 把工作表信息写入输出流
     *
     * @param response        响应对象
     * @param xlsDataWorkBook 工作表数据
     * @param fileName        文件名
     * @return 导出是否成功
     */
    public static boolean writeToResponse(String fileName, HttpServletResponse response, XlsDataWorkBook xlsDataWorkBook) throws IOException, IllegalAccessException {
        return writeToResponse(fileName, XlsType.XLS, response, xlsDataWorkBook);
    }

    /**
     * 把工作表信息写入输出流
     *
     * @param response        响应对象
     * @param xlsType         版本格式 xls xlsx
     * @param xlsDataWorkBook 工作表数据
     * @param fileName        文件名
     * @return 导出是否成功
     */
    public static boolean writeToResponse(String fileName, XlsType xlsType, HttpServletResponse response, XlsDataWorkBook xlsDataWorkBook) throws IOException, IllegalAccessException {
        //如果没有传格式字符，或者传递字符为xls，就输出为xls格式
        String fileFullName = null;
        if (xlsType == null || xlsType == XlsType.XLS) {
            fileFullName = fileName + XlsType.XLS.getExt();
        } else {
            if (xlsType == XlsType.XLSX) {
                fileFullName = fileName + XlsType.XLSX.getExt();
            } else {
                //如果后缀有错误，则统一按照xls处理
                fileFullName = fileName + XlsType.XLS.getExt();
            }
        }
        buildExportResponse(fileFullName, response);
        ServletOutputStream os = response.getOutputStream();
        XlsUtils.exportToXlsOutputStream(xlsDataWorkBook, os);
        return true;
    }

    //构建一个用于导出的响应体
    public static HttpServletResponse buildExportResponse(String fileFullName, HttpServletResponse response) throws UnsupportedEncodingException {
        //response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileFullName.getBytes(), "iso-8859-1"));
        if (fileFullName.endsWith(XlsType.XLS.getExt())) {
            response.setContentType("application/octet-stream");
        } else if (fileFullName.endsWith(XlsType.XLSX.getExt())) {
            //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            //response.setContentType("application/msexcel");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");

        } else {
            throw new RuntimeException("File ext is invalid.");
        }
        return response;
    }

}
