package com.chris.poi.utils;


import com.chris.poi.xls.XlsDataSheet;
import com.chris.poi.xls.XlsUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Chris Chen
 * 2018/10/18
 * Explain: XLS导入导出工具
 */

public class XlsIOUtils {

    /**
     * 把工作表信息写入输出流
     *
     * @param response 响应对象
     * @param workSheetData 工作表数据
     * @param os 输出流
     * @return 导出是否成功
     */
    public static boolean writeToOutputStream(HttpServletResponse response, XlsDataSheet workSheetData, ServletOutputStream os) {
        try {
            if (os == null) {
                os = response.getOutputStream();
            }
            XlsUtils.exportToXlsOutputStream(workSheetData, os);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    //构建一个用于导出的响应体
    public static HttpServletResponse buildeExportResponse(String fileName, HttpServletResponse response) {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        return response;
    }
}
