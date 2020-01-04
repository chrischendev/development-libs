package com.chris.hbase;

import com.chris.hbase.config.HBaseConfig;
import com.chris.hbase.utils.HbaseUtils;
import com.chris.hbase.utils.RowkeyUtils;
import com.google.gson.Gson;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Create by Chris Chen
 * Create on 2019-03-08 10:22 星期五
 * This class be use for: HBaseUtils工具测试
 */
public class HBaseUtilsTest {
    private static final String HADOOP_HOME_DIR = "D:\\softs\\hadoop-2.8.5";
    private static final String HBASE_ZK_QUORUM = "10.100.81.177,10.100.81.178,10.100.81.179";
    private static final String HBASE_ZK_PROP_CLIENT_PORT = "2181";
    private static final String HBASE_MASTER = "10.100.81.174:60000";

    private static Connection connection;
    private static Gson gson;

    public static void main(String[] args) throws IOException {
        connection = HBaseConfig.createConnection(HADOOP_HOME_DIR, HBASE_ZK_QUORUM, HBASE_ZK_PROP_CLIENT_PORT, HBASE_MASTER);
        HbaseUtils.init(connection);

        testScan1();
    }

    private static void testExistTable() {
        try {
            Admin admin = connection.getAdmin();
            boolean exists = HbaseUtils.tableExists("vehicle");
            System.out.println(exists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testDeleteTable() {
        HbaseUtils.deleteTables("chris_test_1", "chris_test_01");
    }

    private static void testGetObject() {
        User user = HbaseUtils.getObject(User.class, "hbase_utils_test_tab", RowkeyUtils.buildRowkey("code0001", "chris", 40), "test_fam", "user");
        System.out.println(user.toString());
    }

    private static void testInsertObject() {
        User user = new User("code0001", "chris", 40, 1, "java", "上海杨浦");
        HbaseUtils.insertObject("hbase_utils_test_tab", RowkeyUtils.buildRowkey(user.code, user.name, user.age), "test_fam", "user", user);
    }

    private static void testScan() {
        ResultScanner resultScanner = HbaseUtils.getScanner("vehicle");
        for (Result result : resultScanner) {
            Cell[] rawCells = result.rawCells();
            for (Cell cell : rawCells) {
                System.out.println("RowKey: " + new String(CellUtil.cloneRow(cell)));
                System.out.println("Time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getTimestamp()));
                System.out.println("ColumnFamily: " + new String(CellUtil.cloneFamily(cell)));
                System.out.println("Column: " + new String(CellUtil.cloneQualifier(cell)));
                System.out.println("Value: " + new String(CellUtil.cloneValue(cell)));
            }
        }
    }

    private static void testScan1() {

    }


    private static void testGetResult() {
        Result result = HbaseUtils.getResult("hbase_utils_test_tab", RowkeyUtils.buildRowkey("chris", "hbase", "rowkey"));
        Cell[] rawCells = result.rawCells();
        for (Cell cell : rawCells) {
            System.out.println("RowKey: " + new String(CellUtil.cloneRow(cell)));
            System.out.println("Time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getTimestamp()));
            System.out.println("ColumnFamily: " + new String(CellUtil.cloneFamily(cell)));
            System.out.println("Column: " + new String(CellUtil.cloneQualifier(cell)));
            System.out.println("Value: " + new String(CellUtil.cloneValue(cell)));
        }
    }

    private static void testInsertRow() throws IOException {
        //HbaseUtils.insertRow("hbase_utils_test_tab", RowkeyUtils.buildRowkey("chris", "hbase", String.format("%05d", new Random().nextInt(10000))), "test_fam", "col1", "hahahbase");
        HbaseUtils.insertRow("hbase_utils_test_tab", RowkeyUtils.buildRowkey("chris", "hbase", "rowkey"), "test_fam", "col2", "hahahbase4");
    }

    private static void testCreateTable() throws IOException {
        HbaseUtils.createTable("chris_test_01", "test_fam");
    }

}
