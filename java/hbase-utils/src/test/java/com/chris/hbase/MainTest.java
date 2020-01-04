package com.chris.hbase;

import com.chris.hbase.utils.HbaseUtils;
import com.chris.hbase.utils.RowkeyUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

/**
 * Create by Chris Chen
 * Create on 2019年03月08日 09时47分
 * This class be use for: 主要测试
 */
public class MainTest {
    public static void main(String[] args) throws NoSuchFieldException {
        testBuildRowkey1();
    }

    private static void testBuildRowkey1() {
        String[] rowKeyFieldNames = {"tenantId", "vin", "batteryPackCode", "vehicleStatus", "chargeStatus", "time"};
        Vehicle vehicle=new Vehicle("tenantId001","vin001","bat001","1","1",new Timestamp(System.currentTimeMillis()),2.0);

        System.out.println(RowkeyUtils.build(vehicle,"_",rowKeyFieldNames));

    }

    private static void testType() throws NoSuchFieldException {
        UserTest userTest=new UserTest("kalychen",new Timestamp(System.currentTimeMillis()));
        System.out.println(userTest.getAge().getTime());
        Class<? extends UserTest> aClass = userTest.getClass();
        Field field = aClass.getField("age");
        System.out.println(field.getType().getTypeName());
    }

    private static void testBuildRowkey() {
        String rowkey = RowkeyUtils.buildRowkey("tenantId", "vin", "batteryPackCode",new Timestamp(System.currentTimeMillis()));
        System.out.println(rowkey);
    }

    /*
    private static void testInsertDataListSplitColumn() throws IllegalAccessException, IOException, InstantiationException {
        List<Vehicle> vehicleList = XlsUtils.readFromXlsFile("g:/temp/vehicle_data_list_1.xls", 0, Vehicle.class);

        String[] rowKeyFieldNames = {"tenantId", "vin", "batteryPackCode", "vehicleStatus", "chargeStatus", "time"};
        String[] columnFieldNames = {"tenantId", "vin", "batteryPackCode", "vehicleStatus", "chargeStatus", "time"};
        String tableName = "vehicle_test_01";
        System.out.println(vehicleList.size());
        vehicleList.stream().forEach(veh-> System.out.println(RowkeyUtils.build(veh,"",rowKeyFieldNames)));

        HbaseUtils.insertDataList(tableName, "columnFamily", vehicleList, rowKeyFieldNames, columnFieldNames, true);

        HbaseUtils.close();
    }
    */


}
