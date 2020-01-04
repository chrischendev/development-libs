package com.chris.poi.sample;

import com.chris.poi.sample.model.UserXeo;
import com.chris.poi.xls.XlsDataWorkBook;
import com.chris.poi.xls.XlsUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris Chen
 * 2018/11/07
 * Explain:
 */

public class MainTest {
    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        test1();
    }

    private static void test2() throws IllegalAccessException, IOException, InstantiationException, InvalidFormatException {
        List<UserXeo> userXeoList = XlsUtils.readFromXlsFile("I:/test-xlsx-01.xls", UserXeo.class);
        userXeoList.stream().forEach(userXeo->System.out.println(userXeo.toString()));
    }

    private static void test1() throws IOException, IllegalAccessException {
        List<UserXeo> userXeoList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            userXeoList.add(new UserXeo("姓名 " + i, i * 3, "地址 " + i));
        }

        XlsDataWorkBook xlsDataWorkBook = XlsDataWorkBook.get()
                .addDataList(UserXeo.class, "用户表 ", userXeoList);
        XlsUtils.exportToXls(xlsDataWorkBook, "I:/test-xlsx-01.xls");
    }
}
