package com.chris.hbase.utils;

import com.chris.hbase.model.*;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create by Chris Chen
 * Create on 2019-03-08 10:08 星期五
 * This class be use for: hBase操作工具 适应多实例操作
 */
public class HbaseCommonUtils {
    private Admin admin;
    private Connection connection;
    private static Gson gson = new Gson();
    private boolean preBuildRegion = true;//创建表时是否分区
    private long writeBufferSize = 1024 * 1024 * 2; //写入缓冲区大小
    private Configuration configuration = null;

    public static HbaseCommonUtils create() {
        return new HbaseCommonUtils();
    }

    public static HbaseCommonUtils create(Connection conn) {
        HbaseCommonUtils hbaseCommonUtils = new HbaseCommonUtils();
        hbaseCommonUtils.connection = conn;
        try {
            hbaseCommonUtils.admin = hbaseCommonUtils.connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hbaseCommonUtils;
    }

    public Admin getAdmin() {
        return admin;
    }

    public HbaseCommonUtils setAdmin(Admin admin) {
        this.admin = admin;
        return this;
    }

    public Connection getConnection() {
        return connection;
    }

    public HbaseCommonUtils setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public HbaseCommonUtils setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    /**
     * 创建表
     *
     * @param tableName
     * @param columnFamilies
     * @return
     */
    public boolean createTable(String tableName, Collection<String> columnFamilies) {
        if (CollectionUtils.isEmpty(columnFamilies)) {
            return createTable(tableName, "default_column_family");
        }
        String[] cfs = new String[columnFamilies.size()];
        columnFamilies.toArray(cfs);
        return createTable(tableName, cfs);
    }

    /**
     * 创建表
     *
     * @param tableName
     * @param columnFamilies
     * @return
     */
    public boolean createTable(String tableName, String... columnFamilies) {
        if (null == columnFamilies || columnFamilies.length == 0) {
            return createTable(tableName, "default_column_family");
        }
        if (this.preBuildRegion) {
            String[] s = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
            return createTable(tableName, columnFamilies, s);
        } else {
            return createTable(tableName, columnFamilies, null);
        }
    }

    /**
     * 创建表 分区
     *
     * @param tableName
     * @param columnFamilies
     * @param splitKeys
     * @return
     */
    public boolean createTable(String tableName, String[] columnFamilies, String[] splitKeys) {
        if (null == columnFamilies || columnFamilies.length == 0) {
            return createTable(tableName, "default_column_family");
        }
        TableName tableName1 = TableName.valueOf(tableName);

        if (tableExists(tableName1)) {
            System.out.println("数据表已经存在");
            close();
            return false;
        }

        HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName1);
        for (String columnFamily : columnFamilies) {
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(columnFamily);
            hTableDescriptor.addFamily(hColumnDescriptor);
        }
        try {
            if (null == splitKeys || splitKeys.length == 0) {
                this.admin.createTable(hTableDescriptor);
            } else {
                int partition = splitKeys.length;
                byte[][] splitKeyBytes = new byte[partition][];
                for (int i = 0; i < partition; i++) {
                    splitKeyBytes[i] = Bytes.toBytes(splitKeys[i]);
                }
                this.admin.createTable(hTableDescriptor, splitKeyBytes);
            }
            HTable hTable = new HTable(getConfiguration(), tableName1);//获取这张表
            hTable.setWriteBufferSize(this.writeBufferSize);
            hTable.setAutoFlush(false);//设置禁止自动清空缓存
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除表
     *
     * @param tableName
     * @return
     */
    public boolean deleteTable(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return false;
        }

        try {
            TableName tbName = TableName.valueOf(tableName);
            if (!tableExists(tbName)) {
                return false;
            }
            this.admin.disableTable(tbName);
            this.admin.deleteTable(tbName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTables(String... tableNames) {
        if (tableNames == null || tableNames.length == 0) {
            return false;
        }
        try {
            for (String tableName : tableNames) {
                TableName tbName = TableName.valueOf(tableName);
                if (!tableExists(tbName)) {
                    continue;
                }
                this.admin.disableTable(tbName);
                this.admin.deleteTable(tbName);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //插入行
    public <T> boolean insertObject(String tableName, String rowKey, String columnFamily, String column, T data) {
        return insertRow(tableName, rowKey, columnFamily, column, gson.toJson(data));
    }

    public <T> boolean insertTableObject(TableObject<T> tableObject) {
        return insertRowObjectList(tableObject.getTableName(), tableObject.getRowObjectList());
    }

    public <T> boolean insertTableData(TableObject<String> tableObject) {
        return insertRowDataList(tableObject.getTableName(), tableObject.getRowObjectList());
    }

    //批量插入行
    public <T> boolean insertRowObjectList(String tableName, List<RowObject<T>> rowObjectList) {
        try {
            TableName tbName = TableName.valueOf(tableName);
            if (!tableExists(tableName)) {
                return false;
            }
            Table table = this.connection.getTable(tbName);
            List<Put> putList = new ArrayList<>();
            Put put = null;
            for (RowObject<T> rowObject : rowObjectList) {
                List<ColumnObject<T>> columnObjectList = rowObject.getColumnObjectList();
                String rowKey = rowObject.getRowKey();
                for (ColumnObject<T> columnObject : columnObjectList) {
                    put = new Put(Bytes.toBytes(rowKey));
                    put.addColumn(Bytes.toBytes(columnObject.getColumnFamily()), Bytes.toBytes(columnObject.getColumn()), Bytes.toBytes(gson.toJson(columnObject.getValue())));
                    putList.add(put);
                }
            }
            table.put(putList);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertRowDataList(String tableName, List<RowObject<String>> rowObjectList) {
        try {
            TableName tbName = TableName.valueOf(tableName);
            if (!tableExists(tableName)) {
                return false;
            }
            Table table = this.connection.getTable(tbName);
            List<Put> putList = new ArrayList<>();
            Put put = null;
            for (RowObject<String> rowObject : rowObjectList) {
                List<ColumnObject<String>> columnObjectList = rowObject.getColumnObjectList();
                String rowKey = rowObject.getRowKey();
                for (ColumnObject<String> columnObject : columnObjectList) {
                    put = new Put(Bytes.toBytes(rowKey));
                    put.addColumn(Bytes.toBytes(columnObject.getColumnFamily()), Bytes.toBytes(columnObject.getColumn()), Bytes.toBytes(columnObject.getValue()));
                    putList.add(put);
                }
            }
            table.put(putList);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //批量插入列
    public <T> boolean insertColumnObjectList(String tableName, String rowKey, List<ColumnObject<T>> columnObjectList) {
        try {
            TableName tbName = TableName.valueOf(tableName);
            if (!tableExists(tableName)) {
                return false;
            }
            Table table = this.connection.getTable(tbName);
            List<Put> putList = new ArrayList<>();
            Put put = null;
            for (ColumnObject<T> columnObject : columnObjectList) {
                put = new Put(Bytes.toBytes(rowKey));
                put.addColumn(Bytes.toBytes(columnObject.getColumnFamily()), Bytes.toBytes(columnObject.getColumn()), Bytes.toBytes(gson.toJson(columnObject.getValue())));
                putList.add(put);
            }
            table.put(putList);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertColumnDataList(String tableName, String rowKey, List<ColumnObject<String>> columnObjectList) {
        try {
            TableName tbName = TableName.valueOf(tableName);
            if (!tableExists(tableName)) {
                return false;
            }
            Table table = this.connection.getTable(tbName);
            List<Put> putList = new ArrayList<>();
            Put put = null;
            for (ColumnObject<String> columnObject : columnObjectList) {
                put = new Put(Bytes.toBytes(rowKey));
                put.addColumn(Bytes.toBytes(columnObject.getColumnFamily()), Bytes.toBytes(columnObject.getColumn()), Bytes.toBytes(columnObject.getValue()));
                putList.add(put);
            }
            table.put(putList);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertRow(String tableName, String rowKey, String columnFamily, String column, String value) {
        try {
            TableName tbName = TableName.valueOf(tableName);
            if (!tableExists(tableName)) {
                return false;
            }
            Table table = this.connection.getTable(tbName);
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));
            table.put(put);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //扩展部分

    /**
     * 批量插入数据
     * 根据数据自身构建rowkey和列名
     * 支持分列处理
     *
     * @param tableName
     * @param columnFamily
     * @param dataList
     * @param rowKeyFieldNames
     * @param columnFieldNames
     * @param splitColumn
     * @param <T>
     * @return
     */
    public <T> boolean insertDataList(String tableName, String columnFamily, List<T> dataList, String[] rowKeyFieldNames, String[] columnFieldNames, boolean splitColumn) {
        if (!splitColumn) {
            return insertDataList(tableName, columnFamily, dataList, rowKeyFieldNames, columnFieldNames);
        }
        if (StringUtils.isBlank(tableName) ||
                StringUtils.isBlank(columnFamily) ||
                null == dataList ||
                dataList.size() == 0 ||
                null == rowKeyFieldNames ||
                rowKeyFieldNames.length == 0 ||
                null == columnFieldNames ||
                columnFieldNames.length == 0) {
            return false;
        }
        //检查表，不存在则创建
        if (!this.tableExists(tableName)) {
            this.createTable(tableName, columnFamily);
        }
        List<RowObject<String>> rowObjectList = new ArrayList<>();
        dataList.stream().forEach(data -> {
            String rowKey = RowkeyUtils.build(data, "-", rowKeyFieldNames);
            Class<?> dataClass = data.getClass();
            //分列数据
            for (String columnFieldName : columnFieldNames) {
                try {
                    Field field = dataClass.getField(columnFieldName);
                    field.setAccessible(true);
                    Object value = field.get(data);
                    field.setAccessible(false);
                    rowObjectList.add(RowObject.get()
                            .setRowKey(rowKey)
                            .addColumnObject(ColumnObject.create(columnFamily, columnFieldName, String.valueOf(value))));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            //主要数据
            rowObjectList.add(RowObject.get()
                    .setRowKey(rowKey)
                    .addColumnObject(ColumnObject.create(columnFamily, "data", gson.toJson(data))));//主要数据先构建json再写入
        });
        return this.insertRowDataList(tableName, rowObjectList);
    }

    /**
     * 批量插入数据
     * 根据数据自身和字段列表构建rowkey和列名
     *
     * @param tableName
     * @param columnFamily
     * @param dataList
     * @param rowKeyFieldNames
     * @param columnFieldNames
     * @param <T>
     * @return
     */
    public <T> boolean insertDataList(String tableName, String columnFamily, List<T> dataList, String[] rowKeyFieldNames, String[] columnFieldNames) {
        if (StringUtils.isBlank(tableName) ||
                StringUtils.isBlank(columnFamily) ||
                null == dataList ||
                dataList.size() == 0 ||
                null == rowKeyFieldNames ||
                rowKeyFieldNames.length == 0 ||
                null == columnFieldNames ||
                columnFieldNames.length == 0) {
            return false;
        }
        //检查表，不存在则创建
        if (!this.tableExists(tableName)) {
            this.createTable(tableName, columnFamily);
        }
        List<RowObject<T>> rowObjectList = new ArrayList<>();
        dataList.stream().forEach(data -> rowObjectList.add(RowObject.get()
                .setRowKey(RowkeyUtils.build(data, "-", rowKeyFieldNames))
                .addColumnObject(ColumnObject.create(columnFamily, RowkeyUtils.build(data, "-", columnFieldNames), data))));
        return this.insertRowObjectList(tableName, rowObjectList);
    }

    /**
     * 批量插入数据
     * 根据数据自身自动构建rowkey
     *
     * @param tableName
     * @param columnFamily
     * @param column
     * @param dataList
     * @param rowKeyFieldNames
     * @param <T>
     * @return
     */
    public <T> boolean insertDataList(String tableName, String columnFamily, String column, List<T> dataList, String... rowKeyFieldNames) {
        if (StringUtils.isBlank(tableName) ||
                StringUtils.isBlank(columnFamily) ||
                StringUtils.isBlank(column) ||
                null == dataList ||
                dataList.size() == 0 ||
                null == rowKeyFieldNames ||
                rowKeyFieldNames.length == 0) {
            return false;
        }
        //检查表，不存在则创建
        if (!this.tableExists(tableName)) {
            this.createTable(tableName, columnFamily);
        }
        List<RowObject<T>> rowObjectList = new ArrayList<>();
        dataList.stream().forEach(data -> rowObjectList.add(RowObject.get()
                .setRowKey(RowkeyUtils.build(data, "-", rowKeyFieldNames))
                .addColumnObject(ColumnObject.create(columnFamily, column, data))));
        return this.insertRowObjectList(tableName, rowObjectList);
    }

    /**
     * 批量插入数据
     * 根据数据自身的字段位置构建rowkey
     * 需要提前确认数据类型字段的正确顺序
     *
     * @param tableName
     * @param columnFamily
     * @param column
     * @param dataList
     * @param rowKeyFieldIndexs
     * @param <T>
     * @return
     */
    public <T> boolean insertDataList(String tableName, String columnFamily, String column, List<T> dataList, int... rowKeyFieldIndexs) {
        if (StringUtils.isBlank(tableName) ||
                StringUtils.isBlank(columnFamily) ||
                StringUtils.isBlank(column) ||
                null == dataList ||
                dataList.size() == 0 ||
                null == rowKeyFieldIndexs ||
                rowKeyFieldIndexs.length == 0) {
            return false;
        }
        //检查表，不存在则创建
        if (!this.tableExists(tableName)) {
            this.createTable(tableName, columnFamily);
        }
        List<RowObject<T>> rowObjectList = new ArrayList<>();
        dataList.stream().forEach(data -> rowObjectList.add(RowObject.get()
                .setRowKey(RowkeyUtils.buildByFieldIndexs(data, "-", rowKeyFieldIndexs))
                .addColumnObject(ColumnObject.create(columnFamily, column, data))));
        return this.insertRowObjectList(tableName, rowObjectList);
    }

    //获取数据
    public <T> T getObject(Class<T> clazz, String tableName, String rowKey, String columnFamily, String column) {
        if (!tableExists(tableName)) {
            return null;
        }
        Result result = getResult(tableName, rowKey);
        Cell latestCell = result.getColumnLatestCell(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
        String json = new String(CellUtil.cloneValue(latestCell));
        return gson.fromJson(json, clazz);
    }

    public Result getResult(String tableName, String rowKey) {
        return getResult(tableName, rowKey, null, null);
    }

    public Result getResult(String tableName, String rowKey, String columnFamily, String column) {
        if (!tableExists(tableName)) {
            return null;
        }
        try {
            Table table = this.connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            if (StringUtils.isNotBlank(columnFamily)) {
                if (StringUtils.isNotBlank(column)) {
                    get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
                } else {
                    get.addFamily(Bytes.toBytes(columnFamily));
                }
            }
            Result result = table.get(get);
            table.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //扫描批量数据
    public <T> List<T> getObjectList(Class<T> clazz, String tableName) {
        return getObjectList(clazz, tableName, null, null);
    }

    public <T> List<T> getObjectList(Class<T> clazz, String tableName, String columnFamily, String column) {
        return getObjectList(clazz, tableName, null, null, columnFamily, column);
    }

    public <T> List<T> getObjectList(Class<T> clazz, String tableName, String startRowKey, String stopRowKey, String columnFamily, String column) {
        if (!tableExists(tableName)) {
            return null;
        }
        List<T> objectList = new ArrayList<>();
        ResultScanner resultScanner = null;
        if (StringUtils.isBlank(columnFamily) && StringUtils.isBlank(column)) {
            resultScanner = getScanner(tableName);
        } else {
            resultScanner = getScanner(tableName, startRowKey, stopRowKey);
        }
        for (Result result : resultScanner) {
            //如果列族或列名为空，则遍历左右列，否则只获取匹配的列
            if (StringUtils.isBlank(columnFamily) || StringUtils.isBlank(column)) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    String json = new String(CellUtil.cloneValue(cell));
                    T t = gson.fromJson(json, clazz);
                    if (t != null) {
                        objectList.add(t);
                    }
                }
            } else {
                Cell latestCell = result.getColumnLatestCell(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
                String json = new String(CellUtil.cloneValue(latestCell));
                T t = gson.fromJson(json, clazz);
                if (t != null) {
                    objectList.add(t);
                }
            }
        }
        return objectList;
    }

    public ResultScanner getScanner(String tableName) {
        return getScanner(tableName, null, null);
    }

    public ResultScanner getScanner(String tableName, String startRowKey, String stopRowKey) {
        if (!tableExists(tableName)) {
            return null;
        }
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            if (StringUtils.isNotBlank(startRowKey) && StringUtils.isNotBlank(stopRowKey)) {
                scan.setStartRow(Bytes.toBytes(startRowKey));
                scan.setStopRow(Bytes.toBytes(stopRowKey));
            }
            ResultScanner resultScanner = table.getScanner(scan);
            table.close();
            return resultScanner;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //检查表是否存在
    public boolean tableExists(String tableName) {
        TableName tbName = TableName.valueOf(tableName);
        return tableExists(tbName);
    }

    public boolean tableExists(TableName tbName) {
        try {
            if (this.admin.tableExists(tbName)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPreBuildRegion() {
        return preBuildRegion;
    }

    public void setPreBuildRegion(boolean preBuildRegion) {
        this.preBuildRegion = preBuildRegion;
    }

    public long getWriteBufferSize() {
        return writeBufferSize;
    }

    public void setWriteBufferSize(long writeBufferSize) {
        this.writeBufferSize = writeBufferSize;
    }

    //关闭连接
    public void close() {
        try {
            this.admin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///////////////////search

    public <T> List<T> searchObjectListOfRows(SearchParamBox searchParamBox, Class<T> clazz) throws IOException {
        if (null == searchParamBox) {
            return null;
        }
        if (null == clazz) {
            return null;
        }
        List<String> jsonStringList = searchJsonStringListOfRows(searchParamBox);
        if (CollectionUtils.isEmpty(jsonStringList)) {
            return null;
        }
        return jsonStringList.stream().map(jsonStr -> gson.fromJson(jsonStr, clazz)).collect(Collectors.toList());
    }

    public List<String> searchJsonStringListOfRows(SearchParamBox searchParamBox) throws IOException {
        if (null == searchParamBox) {
            return null;
        }
        List<Cell> cellList = searchCellListOfRows(searchParamBox);
        if (CollectionUtils.isEmpty(cellList)) {
            return null;
        }
        return cellList.stream().map(cell -> new String(CellUtil.cloneValue(cell))).collect(Collectors.toList());
    }

    public List<Cell> searchCellListOfRows(SearchParamBox searchParamBox) throws IOException {
        if (null == searchParamBox) {
            return null;
        }
        ResultScanner resultScanner = searchResultScannerOfRows(searchParamBox);
        if (null == resultScanner) {
            return null;
        }
        List<Cell> cellList = new ArrayList<>();
        for (Result result : resultScanner) {
            String columnFamily = searchParamBox.getColumnFamily();
            String column = searchParamBox.getColumn();
            //如果指定了列簇和小列，则收集指定的cell，否则收集所有的cell
            if (StringUtils.isNotBlank(columnFamily) && StringUtils.isNotBlank(column)) {
                cellList.addAll(result.getColumnCells(Bytes.toBytes(columnFamily), Bytes.toBytes(column)));
            } else {
                cellList.addAll(Arrays.asList(result.rawCells()));
            }
        }
        return cellList;
    }

    public ResultScanner searchResultScannerOfRows(SearchParamBox searchParamBox) throws IOException {
        if (null == searchParamBox) {
            return null;
        }
        Table table = this.connection.getTable(TableName.valueOf(searchParamBox.getTableName()));

        Scan scan = new Scan();
        FilterList filterList = new FilterList();

        String linkSymbol = searchParamBox.getLinkSymbol();
        StringBuilder sb = new StringBuilder("^");
        searchParamBox.getSearchParamList().stream().forEach(searchParam -> {
            //int index = searchParam.getIndex();
            String keyWords = searchParam.getKeyWords();
            SearchType searchType = searchParam.getSearchType();

            switch (searchType) {
                case MATCHS:
                    sb.append(keyWords).append(linkSymbol);
                    break;
                case FUZZY:
                    sb.append("\\S*").append(keyWords).append("\\S*").append(linkSymbol);
                    break;
                case IGNORE:
                    sb.append("\\S*").append(linkSymbol);
                    break;
                default:
                    break;
            }
        });

        if (sb.length() <= 1) {
            return null;
        }
        String regStr = sb.replace(sb.lastIndexOf(linkSymbol), sb.length(), "").append("$").toString();
        if (searchParamBox.isShowSearchString()) {
            System.out.println(regStr);
        }
        Filter regexFilter = new RowFilter(
                CompareFilter.CompareOp.EQUAL,
                new RegexStringComparator(regStr));

        filterList.addFilter(regexFilter);

        scan.setFilter(regexFilter);
        Range range = searchParamBox.getRange();
        if (range != null && range.min != null && range.max != null) {
            scan.setStartRow(Bytes.toBytes(String.valueOf(range.min)));//起始行
            scan.setStopRow(Bytes.toBytes(String.valueOf(range.max)));//结束行
        }

        return table.getScanner(scan);
    }

}
