package com.chris.poi.xls;

import com.chris.poi.enums.XlsType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Chris Chen
 * 2018/09/17
 * Explain: Java操作Excel表格的工具
 */

public class XlsUtils {
    public static final int CHAR_WIDTH = 256;

    /**
     * 导出工作簿数据到xls文件
     *
     * @param xlsDataWorkBook 工作簿数据
     * @param saveFileName    存储文件名
     * @return 导出是否成功
     * @throws IOException            IO异常
     * @throws IllegalAccessException IllegalAccess异常
     */
    public static Boolean exportToXls(XlsDataWorkBook xlsDataWorkBook, String saveFileName) throws IOException, IllegalAccessException {
        if (saveFileName.endsWith(XlsType.XLSX.getExt())) {
            return exportToXlsx(xlsDataWorkBook, saveFileName);
        }
        //创建一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //读取工作表数据集合
        List<XlsDataSheet> xlsDataSheetList = xlsDataWorkBook.getXlsDataSheetList();
        //遍历向工作簿添加 工作表
        for (XlsDataSheet xlsDataSheet : xlsDataSheetList) {
            addToXls(xlsDataSheet, workbook);
        }
        return saveXlsFile(workbook, saveFileName);
    }

    /**
     * 导出工作簿数据到xlsx文件
     *
     * @param xlsDataWorkBook 工作簿数据
     * @param saveFileName    存储文件名
     * @return 导出是否成功
     * @throws IOException            IO异常
     * @throws IllegalAccessException IllegalAccess异常
     */
    public static Boolean exportToXlsx(XlsDataWorkBook xlsDataWorkBook, String saveFileName) throws IOException, IllegalAccessException {
        if (saveFileName.endsWith(XlsType.XLS.getExt())) {
            return exportToXls(xlsDataWorkBook, saveFileName);
        }
        //创建一个工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //读取工作表数据集合
        List<XlsDataSheet> xlsDataSheetList = xlsDataWorkBook.getXlsDataSheetList();
        //遍历向工作簿添加 工作表
        for (XlsDataSheet xlsDataSheet : xlsDataSheetList) {
            addToXls(xlsDataSheet, workbook);
        }

        return saveXlsFile(workbook, saveFileName);
    }

    /**
     * 创建一个xls工作簿，并且添加一张表，写入数据
     *
     * @param xlsDataSheet 工作表数据
     * @param saveFileName 存储文件名
     * @param <T>          泛型：数据类型
     * @return 导出是否成功
     * @throws IOException            IO异常
     * @throws IllegalAccessException IllegalAccess异常
     */
    public static <T> Boolean exportToXls(XlsDataSheet<T> xlsDataSheet, String saveFileName) throws IOException, IllegalAccessException {
        if (saveFileName.endsWith(XlsType.XLSX.getExt())) {
            return exportToXlsx(xlsDataSheet, saveFileName);
        }
        //创建一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //向工作簿天机按工作表
        addToXls(xlsDataSheet, workbook);
        return saveXlsFile(workbook, saveFileName);
    }

    /**
     * 创建一个xlsx工作簿，并且添加一张表，写入数据
     *
     * @param xlsDataSheet 工作表数据
     * @param saveFileName 存储文件名
     * @param <T>          泛型：数据类型
     * @return 导出是否成功
     * @throws IOException            IO异常
     * @throws IllegalAccessException IllegalAccess异常
     */
    public static <T> Boolean exportToXlsx(XlsDataSheet<T> xlsDataSheet, String saveFileName) throws IOException, IllegalAccessException {
        if (saveFileName.endsWith(XlsType.XLS.getExt())) {
            return exportToXls(xlsDataSheet, saveFileName);
        }
        //创建一个工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //向工作簿天机按工作表
        addToXls(xlsDataSheet, workbook);
        return saveXlsFile(workbook, saveFileName);
    }

    /**
     * 把xls工作簿导出到输出流OutputStream
     * 目前这个工具暂时只支持XLS格式输出
     *
     * @param xlsDataWorkBook xls工作簿数据
     * @param os              输出流
     * @return 导出是否成功
     * @throws IllegalAccessException IllegalAccess异常
     */
    public static Boolean exportToXlsOutputStream(XlsDataWorkBook xlsDataWorkBook, OutputStream os) throws IllegalAccessException {
        if (xlsDataWorkBook == null) {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
        if (os == null) {
            return false;
        }

        XlsBusinessWorkBookCallback callback = xlsDataWorkBook.getXlsBusinessWorkBookCallback();
        List<XlsDataSheet> xlsDataSheetList = xlsDataWorkBook.getXlsDataSheetList();
        if (xlsDataSheetList == null || xlsDataSheetList.size() == 0) {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
        //创建一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        if (callback != null) {
            callback.preHandleWorkBook(workbook);
        }
        //循环向工作簿添加工作表
        for (XlsDataSheet xlsDataSheet : xlsDataSheetList) {
            addToXls(xlsDataSheet, workbook);
        }
        try {
            workbook.write(os);
            if (callback != null) {
                callback.postHandleWorkBook(workbook);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 导出xls工作表数据到输出流
     *
     * @param xlsDataSheet xls工作表数据
     * @param os           输出流
     * @param <T>          泛型：数据类型
     * @return 导出是否成功
     * @throws IllegalAccessException IllegalAccess异常
     */
    public static <T> Boolean exportToXlsOutputStream(XlsDataSheet<T> xlsDataSheet, OutputStream os) throws IllegalAccessException {
        //创建一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //向工作簿添加工作表
        addToXls(xlsDataSheet, workbook);
        try {
            workbook.write(os);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 把包含工作表数据集合写入到xls表格
     *
     * @param xlsDataSheetSet 工作表数据集合
     * @param saveFileName    存储文件名
     * @return 导出是否成功
     * @throws IOException            IO异常
     * @throws IllegalAccessException IllegalAccess异常
     */
    public static Boolean exportToXls(Set<XlsDataSheet> xlsDataSheetSet, String saveFileName) throws IOException, IllegalAccessException {
        //创建一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //循环向工作簿添加工作表
        for (XlsDataSheet xlsDataSheet : xlsDataSheetSet) {
            addToXls(xlsDataSheet, workbook);
        }
        return saveXlsFile(workbook, saveFileName);
    }

    /**
     * 导出xls工作表数据到输出流
     *
     * @param xlsDataSheetList 工作表数据
     * @param os               输出流
     * @return 导出是否成功
     * @throws IOException            IO异常
     * @throws IllegalAccessException IllegalAccess异常
     */
    public static Boolean exportToXlsOutputStream(List<XlsDataSheet> xlsDataSheetList, OutputStream os) throws IOException, IllegalAccessException {
        //创建一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //循环向工作簿添加工作表
        for (XlsDataSheet xlsDataSheet : xlsDataSheetList) {
            addToXls(xlsDataSheet, workbook);
        }
        workbook.write(os);
        return true;
    }

    /**
     * 向工作簿添加一张工作表数据
     *
     * @param xlsDataSheet 工作表数据
     * @param workbook     工作簿
     * @param <T>          泛型：数据类型
     * @return 添加是否成功
     * @throws IllegalAccessException IllegalAccess异常
     */
    public static <T> Boolean addToXls(XlsDataSheet<T> xlsDataSheet, Workbook workbook) throws IllegalAccessException {
        Class<T> clazz = xlsDataSheet.getClazz();
        Field[] fields = clazz.getDeclaredFields();
        XlsSheet xlsSheetAnno = clazz.getAnnotation(XlsSheet.class);
        XlsBusinessSheetCallback callback = xlsDataSheet.getXlsBusinessSheetCallback();
        //创建工作表
        Sheet sheet = workbook.createSheet(buildSheetName(xlsDataSheet));
        //正式写入工作表数据之前执行
        if (callback != null) {
            callback.preHandleSheet(sheet, xlsDataSheet);
        }
        //遍历字段填充标题行
        int titleRowIndex = 0;
        if (xlsSheetAnno != null) {
            titleRowIndex = xlsSheetAnno.titleRowIndex();
        }
        Row titleRow = sheet.createRow(titleRowIndex);
        //写入标题行之前执行
        if (callback != null) {
            callback.preHandleRow(titleRow, null);
        }
        int headColIndex = 0;
        for (Field field : fields) {
            //获取字段注解
            String colName = getXlsColumnName(field);
            //设置列宽
            int colWidth = getXlsColumnWidth(field);
            if (colWidth >= 0) {
                sheet.setColumnWidth(headColIndex, (int) ((colWidth + 0.72) * CHAR_WIDTH));
            } else {
                int columnByteLength = colName.trim().getBytes(Charset.forName("GBK")).length;
                sheet.setColumnWidth(headColIndex, (int) ((columnByteLength + 0.72) * CHAR_WIDTH));
            }
            //创建单元格
            Cell cell = titleRow.createCell(headColIndex);
            //写入标题
            cell.setCellValue(colName);
            headColIndex++;
        }
        //写入标题行之后执行
        if (callback != null) {
            callback.postHandleRow(titleRow, null);
        }
        //数据
        List<T> dataList = xlsDataSheet.getDataList();
        if (dataList != null) {
            int dataRowStart = 1;
            if (xlsSheetAnno != null) {
                dataRowStart = xlsSheetAnno.dataRowStart();
            }
            for (int rowIndex = dataRowStart, len = dataList.size(); rowIndex <= len; rowIndex++) {
                T dataObj = dataList.get(rowIndex - 1);
                Row dataRow = sheet.createRow(rowIndex);
                //写入数据之前执行
                if (callback != null) {
                    callback.preHandleRow(dataRow, dataObj);
                }
                //运用反射，把各个字段的值填充到xls表格
                int colIndex = 0;
                for (Field field : fields) {
                    //判断是否应该被排除
                    XlsColumnExclude excludeAnno = field.getAnnotation(XlsColumnExclude.class);
                    XlsColumn xlsColumnAnno = field.getAnnotation(XlsColumn.class);
                    if (excludeAnno != null || xlsColumnAnno != null && xlsColumnAnno.exclude()) {
                        continue;
                    }
                    field.setAccessible(true);
                    Object value = field.get(dataObj);
                    if (value == null) {
                        colIndex++;
                        continue;//空值不写
                    }
                    if (value instanceof Integer) {
                        dataRow.createCell(colIndex++).setCellValue(Integer.parseInt(String.valueOf(value)));
                    } else if (value instanceof Long) {
                        dataRow.createCell(colIndex++).setCellValue(Long.parseLong(String.valueOf(value)));
                    } else if (value instanceof Double) {
                        dataRow.createCell(colIndex++).setCellValue(Double.parseDouble(String.valueOf(value)));
                    } else if (value instanceof Float) {
                        dataRow.createCell(colIndex++).setCellValue(Float.parseFloat(String.valueOf(value)));
                    } else {
                        dataRow.createCell(colIndex++).setCellValue(String.valueOf(value));
                    }
                    field.setAccessible(false);
                }
                //写入数据之后执行
                if (callback != null) {
                    callback.postHandleRow(dataRow, dataObj);
                }
            }
        }
        //正式写入工作表数据之后执行
        if (callback != null) {
            callback.postHandleSheet(sheet, xlsDataSheet);
        }
        return true;
    }

    /**
     * 设置表的列宽
     *
     * @param field 数据对象的字段
     * @return 列宽
     */
    private static int getXlsColumnWidth(Field field) {
        XlsColumn xlsColumns = field.getAnnotation(XlsColumn.class);
        int width = -1;//列宽
        if (xlsColumns != null) {
            //如果注解不为空且width有值，将width作为列宽
            width = xlsColumns.width();
        }
        //如果最终返回值为-1，将不娶设置列宽，由Excel自己设置
        return width;
    }

    /**
     * 获取字段列名
     *
     * @param field 数据对象字段
     * @return 列名
     */
    public static String getXlsColumnName(Field field) {
        XlsColumn xlsColumns = field.getAnnotation(XlsColumn.class);
        String colName = null;//列名
        if (xlsColumns != null) {
            //如果注解不为空且value有值，将value作为列名
            colName = xlsColumns.value();
        }
        //如果cloName仍为空，则以字段名为列名
        if (colName == null || "".equals(colName)) {
            field.setAccessible(true);
            colName = field.getName();
            field.setAccessible(false);
        }
        return colName;
    }

    /**
     * 该字段对应的列是否规定必填
     *
     * @param field 数据对象字段
     * @return 是否必填
     */
    private static boolean isRequired(Field field) {
        XlsColumn xlsColumns = field.getAnnotation(XlsColumn.class);
        boolean required = false;//默认并未规定必填
        if (xlsColumns != null) {
            //如果注解不为空且width有值，将width作为列宽
            required = xlsColumns.required();
        }
        return required;
    }

    /**
     * 构建工作表的名称
     *
     * @param xlsDataSheet 工作表数据
     * @param <T>          泛型：工作表数据类
     * @return 工作表名称
     */
    private static <T> String buildSheetName(XlsDataSheet<T> xlsDataSheet) {
        String title = xlsDataSheet.getTitle();
        return title;
    }

    /**
     * 向工作簿添加多张工作表数据
     *
     * @param workSheetInfoSet 工作表数据集合
     * @param workbook         工作簿
     * @return 添加是否成功
     * @throws IOException            IO异常
     * @throws IllegalAccessException IllegalAccess异常
     */
    public static Boolean addToXls(Set<XlsDataSheet> workSheetInfoSet, HSSFWorkbook workbook) throws IOException, IllegalAccessException {
        for (XlsDataSheet workSheetInfo : workSheetInfoSet) {
            addToXls(workSheetInfo, workbook);
        }
        workbook.write();
        workbook.close();
        return true;
    }

    /**
     * 从文件读取xls表格内容
     * 指定表索引号
     *
     * @param xlsFileName xls文件名
     * @param sheetIndex  工作表索引
     * @param clazz       数据类
     * @param <T>         泛型：数据类型
     * @return 数据集合
     * @throws IllegalAccessException IllegalAccess异常
     * @throws InstantiationException Instantiation异常
     * @throws IOException            IO异常
     */
    public static <T> List<T> readFromXlsFile(String xlsFileName, int sheetIndex, Class<T> clazz) throws InstantiationException, IllegalAccessException, IOException, InvalidFormatException {
        File file = new File(xlsFileName);
        if (!file.exists()) {
            return null;
        }
        InputStream is = new FileInputStream(file);
        is.close();
        List<T> ts = null;
        if (xlsFileName.endsWith(XlsType.XLS.getExt())) {
            ts = readXlsFromInputStream(is, sheetIndex, clazz);
        } else {
            if (xlsFileName.endsWith(XlsType.XLSX.getExt())) {
                ts = readXlsxFromInputStream(is, sheetIndex, clazz);
            } else {
                throw new RuntimeException("file format error.");
            }
        }
        return ts;
    }

    /**
     * 从文件读取xls表格内容
     * 匹配所有表
     *
     * @param xlsFileName xls文件名
     * @param clazz       数据类
     * @param <T>         泛型：数据类型
     * @return 数据集合
     * @throws IllegalAccessException IllegalAccess异常
     * @throws InstantiationException Instantiation异常
     * @throws IOException            IO异常
     */
    public static <T> List<T> readFromXlsFile(String xlsFileName, Class<T> clazz) throws IllegalAccessException, InstantiationException, IOException, InvalidFormatException {
        File file = new File(xlsFileName);
        if (!file.exists()) {
            return null;
        }
        InputStream is = new FileInputStream(file);
        List<T> ts = null;
        if (xlsFileName.endsWith(XlsType.XLS.getExt())) {
            ts = readXlsFromInputStream(is, clazz);
        } else if (xlsFileName.endsWith(XlsType.XLSX.getExt())) {
            ts = readXlsxFromInputStream(is, clazz);
        } else {
            throw new RuntimeException("file format error.");
        }
        is.close();
        return ts;
    }

    /**
     * 从输入流中读取xls表格内容
     * 指定表索引号
     *
     * @param is         输入流
     * @param sheetIndex 工作表索引
     * @param clazz      数据类型
     * @param <T>        泛型：数据类型
     * @return 数据集合
     * @throws IllegalAccessException IllegalAccess异常
     * @throws InstantiationException Instantiation异常
     * @throws IOException            IO异常
     */
    public static <T> List<T> readXlsFromInputStream(InputStream is, int sheetIndex, Class<T> clazz) throws IllegalAccessException, InstantiationException, IOException {
        HSSFWorkbook workbook = createXlsWorkbookFromInputStream(is);
        if (workbook == null) {
            return null;
        }
        return readFromWorkbook(workbook, sheetIndex, clazz);
    }

    /**
     * 从输入流中读取xlsx表格内容
     * 指定表索引号
     *
     * @param is         输入流
     * @param sheetIndex 工作表索引
     * @param clazz      数据类型
     * @param <T>        泛型：数据类型
     * @return 数据集合
     * @throws IllegalAccessException IllegalAccess异常
     * @throws InstantiationException Instantiation异常
     * @throws IOException            IO异常
     */
    public static <T> List<T> readXlsxFromInputStream(InputStream is, int sheetIndex, Class<T> clazz) throws IllegalAccessException, InstantiationException, IOException, InvalidFormatException {
        XSSFWorkbook workbook = createXlsxWorkbookFromInputStream(is);
        if (workbook == null) {
            return null;
        }
        return readFromWorkbook(workbook, sheetIndex, clazz);
    }

    /**
     * 从输入流中读取xls表格内容
     * 匹配所有表
     *
     * @param is    输入流
     * @param clazz 数据类型
     * @param <T>   泛型：数据类型
     * @return 数据集合
     * @throws InstantiationException Instantiation异常
     * @throws IllegalAccessException IllegalAccess异常
     * @throws IOException            IO异常
     */
    public static <T> List<T> readXlsFromInputStream(InputStream is, Class<T> clazz) throws InstantiationException, IllegalAccessException, IOException {
        HSSFWorkbook workbook = createXlsWorkbookFromInputStream(is);
        if (workbook == null) {
            return null;
        }
        return readFromWorkbook(workbook, clazz);
    }

    /**
     * 从输入流中读取xlsx表格内容
     * 匹配所有表
     *
     * @param is    输入流
     * @param clazz 数据类型
     * @param <T>   泛型：数据类型
     * @return 数据集合
     * @throws InstantiationException Instantiation异常
     * @throws IllegalAccessException IllegalAccess异常
     * @throws IOException            IO异常
     */
    public static <T> List<T> readXlsxFromInputStream(InputStream is, Class<T> clazz) throws InstantiationException, IllegalAccessException, IOException, InvalidFormatException {
        XSSFWorkbook workbook = createXlsxWorkbookFromInputStream(is);
        if (workbook == null) {
            return null;
        }
        return readFromWorkbook(workbook, clazz);
    }

    /**
     * 从输入流中创建xls工作簿对象
     *
     * @param is 输入流
     * @return XLS工作簿对象
     * @throws IOException IO异常
     */
    private static HSSFWorkbook createXlsWorkbookFromInputStream(InputStream is) throws IOException {
        return new HSSFWorkbook(is);
    }

    /**
     * 从输入流中创建xlsx工作簿对象
     *
     * @param is 输入流
     * @return XLSX工作簿对象
     * @throws IOException IO异常
     */
    private static XSSFWorkbook createXlsxWorkbookFromInputStream(InputStream is) throws IOException, InvalidFormatException {
        //return new XSSFWorkbook(is);
        //return (XSSFWorkbook) XSSFWorkbookFactory.create(is);
        return XSSFWorkbookFactory.createWorkbook(is);
    }

    /**
     * 从工作簿读取指定数据匹配的数据
     * 系统遍历工作簿，将类型匹配的工作表的数据全部读取出来
     *
     * @param workbook 工作簿
     * @param clazz    数据类型
     * @param <T>      泛型：数据类型
     * @return 数据集合
     * @throws IllegalAccessException IllegalAccess异常
     * @throws InstantiationException Instantiation异常
     */
    public static <T> List<T> readFromWorkbook(Workbook workbook, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        List<T> dataList = new ArrayList<>();
        //获取工作簿中表的总数
        int count = workbook.getNumberOfSheets();
        for (int sheetIndex = 0; sheetIndex < count; sheetIndex++) {
            List<T> list = readFromWorkbook(workbook, sheetIndex, clazz);
            if (list != null) {
                dataList.addAll(list);
            }
        }
        return dataList;
    }

    /**
     * 从工作簿读取指定数据匹配的数据
     * 系统会自动根据字段名匹配数据，并将匹配到的数据全部读出来
     *
     * @param workbook   工作簿
     * @param sheetIndex 工作表索引
     * @param clazz      数据类型
     * @param <T>        泛型：数据类型
     * @return 数据集合
     * @throws InstantiationException Instantiation异常
     * @throws IllegalAccessException IllegalAccess异常
     */
    public static <T> List<T> readFromWorkbook(Workbook workbook, int sheetIndex, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        //获得工作表
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        //检验结构是否匹配
        int titleRowIndex = 0;
        XlsSheet xlsSheetAnno = clazz.getAnnotation(XlsSheet.class);
        if (xlsSheetAnno != null) {
            titleRowIndex = xlsSheetAnno.titleRowIndex();
        }
        Row headRow = sheet.getRow(titleRowIndex);
        Field[] fields = clazz.getDeclaredFields();
        //类型不匹配则退出
        if (!matchClass(fields, headRow)) {
            return null;
        }
        //头部总行数
        int dataRowStart = 1;
        if (xlsSheetAnno != null) {
            dataRowStart = xlsSheetAnno.dataRowStart();
        }
        // 遍历取出数据
        List<T> objList = new ArrayList<>();
        int length = fields.length;
        int maxLines = getMaxLines(clazz);
        for (int rowIndex = dataRowStart; rowIndex < maxLines; rowIndex++) {
            Row dataRow = sheet.getRow(rowIndex);
            //找到空行 这里要求有效数据中间不能出现空行

//            if (dataRow == null) {
            //最后一行 待测试
            if (rowIndex > sheet.getLastRowNum()) {
                //System.out.println("found " + (rowIndex - 1) + " records.");
                break;
            }
            //创建一个实例，用来接收数据
            T obj = getInstance(clazz);
            if (obj == null) {
                continue;
            }
            //如果遇到中间的空白行，则向集合添加一个空对象
            if (dataRow == null) {
                objList.add(null);
                continue;
            }
            //遍历字段赋值
            for (int colIndex = 0; colIndex < length; colIndex++) {
                Field field = fields[colIndex];
                Cell cell = dataRow.getCell(colIndex);
                field.setAccessible(true);
                setValueFromCell(obj, field, cell);
                field.setAccessible(false);
            }
            objList.add(obj);
        }
        return objList;
    }

    /**
     * 获取设定的每张表最大数据行数
     *
     * @param clazz 数据类型
     * @param <T>   泛型：数据类型
     * @return 最大行数
     */
    private static <T> int getMaxLines(Class<T> clazz) {
        int maxLines = 65534;
        XlsSheet xlsSheet = clazz.getAnnotation(XlsSheet.class);
        if (xlsSheet == null) {
            return maxLines;
        }
        int ml = xlsSheet.maxLines();
        if (ml > 0 && ml < 65534) {
            return ml;
        }
        return maxLines;
    }

    /**
     * 从一个xls单元格给一个对象的字段赋值
     *
     * @param obj   接收值的对象
     * @param field 数据对象字段
     * @param cell  单元格
     * @param <T>   泛型：数据类型
     * @throws IllegalAccessException IllegalAccess异常
     */
    private static <T> void setValueFromCell(T obj, Field field, Cell cell) throws IllegalAccessException {
        String typeName = field.getType().getName();
        if (cell == null) {
            return;
        }
        if (int.class.getName().equals(typeName) || Integer.class.getName().equals(typeName)) {
            field.set(obj, (int) cell.getNumericCellValue());
            return;
        }
        if (short.class.getName().equals(typeName) || Short.class.getName().equals(typeName)) {
            field.set(obj, (short) cell.getNumericCellValue());
            return;
        }
        if (long.class.getName().equals(typeName) || Long.class.getName().equals(typeName)) {
            field.set(obj, (long) cell.getNumericCellValue());
            return;
        }
        if (float.class.getName().equals(typeName) || Float.class.getName().equals(typeName)) {
            field.set(obj, (float) cell.getNumericCellValue());
            return;
        }
        if (double.class.getName().equals(typeName) || Double.class.getName().equals(typeName)) {
            field.set(obj, cell.getNumericCellValue());
            return;
        }

        if (boolean.class.getName().equals(typeName) || Boolean.class.getName().equals(typeName)) {
            field.set(obj, cell.getBooleanCellValue());
            return;
        }

        if (Date.class.getName().equals(typeName)) {
            field.set(obj, cell.getDateCellValue());
            return;
        }
        if (Timestamp.class.getName().equals(typeName)) {
            Date dateCellValue = cell.getDateCellValue();
            if (dateCellValue != null) {
                field.set(obj, new Timestamp(dateCellValue.getTime()));
            }
            return;
        }
        if (Instant.class.getName().equals(typeName)) {
            Date dateCellValue = cell.getDateCellValue();
            if (dateCellValue != null) {
                field.set(obj, Instant.ofEpochMilli(dateCellValue.getTime()));
            }
            return;
        }
        //如果上面都不匹配，就全部按照字符串进行读取
        cell.setCellType(CellType.STRING);//强转为字符串类型 poi 4.0
        field.set(obj, cell.getStringCellValue().trim());
    }

    /**
     * 给一个类创建一个实例
     *
     * @param clazz 数据类型
     * @param <T>   泛型：数据类型
     * @return 实例
     * @throws IllegalAccessException IllegalAccess异常
     * @throws InstantiationException Instantiation异常
     */
    private static <T> T getInstance(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        return clazz.newInstance();
    }

    /**
     * 根据一个类的字段列表和xls数据表的表头行判断导入数据类型是否匹配
     *
     * @param fields  数据对象的字段数组
     * @param headRow 工作表标题行
     * @return 匹配是否成功
     */
    private static boolean matchClass(Field[] fields, Row headRow) {
        int length = fields.length;
        for (int i = 0; i < length; i++) {
            String colName = null;
            Field field = fields[i];
            XlsColumn xlsColumn = field.getAnnotation(XlsColumn.class);
            if (xlsColumn != null) {
                colName = xlsColumn.value();
            }
            if (colName == null || "".equals(colName)) {
                colName = field.getName();
            }
            Cell cell = headRow.getCell(i);
            if (cell != null && !cell.getStringCellValue().equalsIgnoreCase(colName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据给定的类的集合，从工作簿中读取匹配的数据，并返回一个对象集合
     *
     * @param workbook 工作簿
     * @param clazz    数据类型
     * @return 数据集合
     */
    public static Set<List<Object>> readFromWorkbook(Workbook workbook, Set<Class<?>> clazz) {
        return null;
    }

    /**
     * 保存xls文件
     *
     * @param workbook     工作簿
     * @param saveFileName 存储文件名
     * @return 保存是否成功
     * @throws IOException IO异常
     */
    private static Boolean saveXlsFile(Workbook workbook, String saveFileName) throws IOException {
        //创建输出流
        File file = new File(saveFileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStream os = new FileOutputStream(file);
        workbook.write(os);
        os.close();
        return true;
    }

    /**
     * 重构字段数组
     *
     * @param fields
     * @return
     */
    private Field[] rebuildFileds(Field[] fields) {
        List<Field> fieldList = new ArrayList<>();
        for (Field field : fields) {
            //判断是否应该被排除
            XlsColumnExclude excludeAnno = field.getAnnotation(XlsColumnExclude.class);
            XlsColumn xlsColumnAnno = field.getAnnotation(XlsColumn.class);
            if (excludeAnno != null || xlsColumnAnno != null && xlsColumnAnno.exclude()) {
                continue;
            }
            fieldList.add(field);
        }
        Field[] fields1 = new Field[fieldList.size()];
        fieldList.toArray(fields1);
        return fields1;
    }
}
