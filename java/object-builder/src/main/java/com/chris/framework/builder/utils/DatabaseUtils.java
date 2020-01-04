package com.chris.framework.builder.utils;

import com.chris.framework.builder.annotation.Expand;
import com.chris.framework.builder.annotation.ExpandField;
import com.chris.framework.builder.annotation.query.PageParam;
import com.chris.framework.builder.annotation.query.Query;
import com.chris.framework.builder.annotation.query.QueryCompare;
import com.chris.framework.builder.annotation.query.QueryField;
import com.chris.framework.builder.model.*;
import com.chris.framework.builder.model.object.QueryParams;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.*;
import java.util.*;

/**
 * ObjectBuilder
 * com.chris.framework.builder.utils
 * Created by Chris Chen
 * 2018/2/11
 * Explain:
 */
public class DatabaseUtils {
    /**
     * 获取一张数据表列集合
     *
     * @param conn
     * @param tableName
     * @return
     * @throws Exception
     */
    public static List<Column> getTableColumnList(Connection conn, String tableName) {
        try {
            Statement statement = conn.createStatement();
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            String sql = "SELECT * FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            ResultSet rsPK = databaseMetaData.getPrimaryKeys(conn.getCatalog(), conn.getSchema(), tableName);//主键
            ResultSet rsFK = databaseMetaData.getImportedKeys(conn.getCatalog(), conn.getSchema(), tableName);//外键

            //获取列属性
            List<Column> columnList = new ArrayList<>();
            Column column = null;
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                column = new Column();

                column.setColumnId(i);
                column.setColumnName(rsmd.getColumnName(i));
                column.setColumnType(rsmd.getColumnTypeName(i));
                column.setColumnClassName(rsmd.getColumnClassName(i));
                column.setColumnDisplaySize(rsmd.getColumnDisplaySize(i));
                column.setIsNullable(rsmd.isNullable(i));
                column.setAutoIncrement(rsmd.isAutoIncrement(i));
                column.setSearchable(rsmd.isSearchable(i));
                column.setColumnName(rsmd.getColumnName(i));
                column.setPrimaryKey(false);
                //找主键
                rsPK.beforeFirst();
                while (rsPK.next()) {
                    if (rsPK.getString("COLUMN_NAME").equals(column.getColumnName())) {
                        //设置主键标记
                        column.setPrimaryKey(true);
                    }
                }

                //外键约束
                rsFK.beforeFirst();
                while (rsFK.next()) {
                    String fkColumnName = rsFK.getString("FKCOLUMN_NAME");
                    if (fkColumnName.equals(column.getColumnName())) {
                        column.setPkTableName(rsFK.getString("PKTABLE_NAME"));
                        column.setPkColumnName(rsFK.getString("PKCOLUMN_NAME"));
                    }
                }
                columnList.add(column);
            }
            return columnList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有表名称列表
     *
     * @param connection
     * @param type       0 表和视图，1 表，2 视图
     * @return
     */
    public static List<String> getTableNameList(Connection connection, int type) {
        String[] types = {"TABLE", "VIEW"};
        switch (type) {
            case 0:
                break;
            case 1:
                types = new String[]{"TABLE"};
                break;
            case 2:
                types = new String[]{"VIEW"};
                break;
        }
        List<String> tableNameList = new ArrayList<>();
        try {
            DatabaseMetaData dmd = connection.getMetaData();
            ResultSet rsTable = dmd.getTables(null, null, null, types);
            while (rsTable.next()) {
                tableNameList.add(rsTable.getString("TABLE_NAME"));
            }
            return tableNameList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取数据库结构
     *
     * @param connection
     * @return
     */
    public static Map<String, List<Column>> getDataBaseStructMap(Connection connection) {
        Map<String, List<Column>> dbMap = new HashMap<>();
        List<String> tableNameList = getTableNameList(connection, 1);
        for (String tbName : tableNameList) {
            dbMap.put(tbName, getTableColumnList(connection, tbName));
        }
        return dbMap;
    }

    /**
     * 获取数据库连接连接
     *
     * @param dbDriver
     * @param url
     * @param username
     * @param password
     * @return
     */
    public static Connection getConnection(String dbDriver, String url, String username, String password) {
        try {
            Class.forName(dbDriver);
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据类名获取表明，前提是这个类上面有@Entity或者@Table注解
     *
     * @param entityClassName
     * @return
     */
    public static String getTableNameByEntityClassName(String entityClassName) {
        if (StringUtils.isEmpty(entityClassName)) {
            return null;
        }
        String tableName = null;
        try {
            Class<?> clazz = Class.forName(entityClassName);
            Entity entityAnno = clazz.getAnnotation(Entity.class);
            if (entityAnno != null) {
                tableName = entityAnno.name();
            }
            if (!StringUtils.isEmpty(tableName)) {
                return tableName;
            }
            Table tableAnno = clazz.getAnnotation(Table.class);
            if (tableAnno != null) {
                tableName = tableAnno.name();
            }
            return tableName;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在一个包下面根据数据库表名寻找建立映射关系的ORM实体类
     * 要求orm实体类中添加了@Entity或者@Table注解
     *
     * @param tableName
     * @param packageName
     * @return
     */
    public static Class<?> findOrmByTableNameInPackage(String tableName, String packageName) {
        List<Class<?>> classList = ClassUtils.getClasses(packageName);
        //遍历包下面的所有类
        for (Class<?> clazz : classList) {
            Entity entityAnno = clazz.getAnnotation(Entity.class);
            if (entityAnno != null && tableName.equals(entityAnno.name())) {
                return clazz;
            }
            Table tableAnno = clazz.getAnnotation(Table.class);
            if (tableAnno != null && tableName.equals(tableAnno.name())) {
                return clazz;
            }
        }
        return null;
    }

    /**
     * 构建Orm对象的内容
     *
     * @param params
     * @return
     */
    public static String buildeOrmContent(EntityBuildParams params, String tableName, String className) {
        List<Column> tableColumnList = DatabaseUtils.getTableColumnList(params.getConnection(), tableName);

        StringBuffer entityHead = new StringBuffer();//头部，包括包名定义和导入包部分
        StringBuffer entityHeadNotes = new StringBuffer();//实体类头部注释
        StringBuffer entityBody = new StringBuffer();//实体类内容
        String entityName = StringUtils.getUpperCamel(className) + params.getOrmExt();
        String ormPackageName = params.getOrmPackageName();

        //构建头部
        Set<String> entityImportSet = new HashSet<>();
        entityHead.append("package ").append(ormPackageName).append(";\n")//包名
                .append("\n");
//                .append("import javax.persistence.*;\n");

        entityImportSet.add("javax.persistence.*");
        ////在导入类中添加类注解的导入
        for (Map.Entry<String, String> entry : params.getIptAnnoMap().entrySet()) {
            entityImportSet.add(entry.getValue());
            entityBody.append("@" + entry.getKey() + "\n");//自由添加的注解
        }
        //构建头部注释
        entityHeadNotes.append("/**")
                .append("\n * App: ")
                .append(params.getAppName())//应用程序名
                .append("\n * Pkg: ")
                .append(ormPackageName)//包名
                .append("\n * Author: ")
                .append(params.getAuthor())
                .append("\n * Time: ")
                .append(params.getTime())
                .append("\n * Explain: ")
                .append(params.getExplain())
                .append("\n */\n\n");

        //构建定义头部
        entityBody.append("@Entity(name = \"").append(tableName).append("\")\n")//关联表名
                .append("public class ").append(entityName).append(" {\n");
        //添加public字段
        for (Column column : tableColumnList) {
            //注解
            if (column.isPrimaryKey()) {
                entityBody.append("    @Id\n");
            }
            if (column.getAutoIncrement()) {
                entityBody.append("    @GeneratedValue(strategy = GenerationType.AUTO)\n");
            }
            if (!column.isPrimaryKey()) {
                entityBody.append("    @Basic\n");
            }
            entityBody.append("    @Column(name = \"").append(column.getColumnName())
                    .append("\", nullable = ").append(column.getIsNullable() == 1 ? true : false)
                    .append(")\n");//字段注解

            //如果字段类型不是基本数据类型或者包装类，则需要把这个类添加到import
            String columnClassName = getFieldTypeNameFromColumnClassName(column.getColumnClassName(), params.isParseTimeStamp(), params.getDbTypeMap());
            Class<?> columnClass = TypeUtils.getClassForName(columnClassName);
            if (!(TypeUtils.equalsPrimitive(columnClass) || String.class.getName().equals(columnClass.getName()))) {
                entityImportSet.add(columnClassName);
            }

            //字段
            entityBody.append("    ")
                    .append(params.getFieldModifier())
                    .append(" ")
                    .append(StringUtils.getSimpleClassNameFromFullClassName(columnClassName))
                    .append(" ")
                    .append(StringUtils.getLowerCamel(column.getColumnName()))
                    .append(";\n\n");
        }

        //添加定义尾部
        entityBody.append("}");

        for (String ipt : entityImportSet) {
            entityHead.append("import ").append(ipt).append(";\n");
        }

        return new StringBuffer(entityHead)
                .append("\n")
                .append(entityHeadNotes)
                .append(entityBody)
                .toString();
    }

    /**
     * 构建扩展对象的内容 Xo和Lo
     *
     * @param params
     * @return
     */
    public static String buildeXObjContent(BuildParams params) {
        String ormPackageName = params.getOrmPackageName();
        String ormName = params.getOrmName();
        String tableName = DatabaseUtils.getTableNameByEntityClassName(ormName);
        String xPkgName = params.getxPackageName();
        List<Column> tableColumnList = DatabaseUtils.getTableColumnList(params.getConnection(), tableName);//这里应该扫描entity获取信息

        StringBuffer entityHead = new StringBuffer();//头部，包括包名定义和导入包部分
        StringBuffer entityHeadNotes = new StringBuffer();//实体类头部注释
        StringBuffer entityBody = new StringBuffer();//实体类内容
        String entityName = StringUtils.getSimpleClassNameFromFullClassName(ormName.replace(params.getOrmExt(), ""));

        //构建头部
        Set<String> entityImportSet = new HashSet<>();
        entityHead.append("package ").append(xPkgName).append(";\n\n");//包名
        entityImportSet.add(Expand.class.getName());
        entityImportSet.add(ormName);

        //构建头部注释
        entityHeadNotes.append("/**")
                .append("\n * App: ")
                .append(params.getAppName())//应用程序名
                .append("\n * Pkg: ")
                .append(params.getxPackageName())//包名
                .append("\n * Author: ")
                .append(params.getAuthor())
                .append("\n * Time: ")
                .append(params.getTime())
                .append("\n * Explain: ")
                .append(params.getExplain())
                .append("\n */\n\n");

        //构建定义头部
        entityBody.append("@Expand(baseEntity = ").append(StringUtils.getSimpleClassNameFromFullClassName(ormName)).append(".class)\n")//关联表名
                .append("public class ").append(entityName).append(params.getxClassExt()).append(" {\n");
        //添加public字段
        for (Column column : tableColumnList) {

            //如果字段类型不是基本数据类型或者包装类，则需要把这个类添加到import
            String columnClassName = getFieldTypeNameFromColumnClassName(column.getColumnClassName(), params.isParseTimeStamp(), params.getDbTypeMap());
            Class<?> columnClass = TypeUtils.getClassForName(columnClassName);
            //如果字段类型不是字符串，也不是基本数据类型以及包装类，就添加import
            if (!(TypeUtils.equalsPrimitive(columnClass) || String.class.getName().equals(columnClass.getName()))) {
                entityImportSet.add(columnClassName);
            }
            //如果有外键约束，则换成xo，将字段末尾的Id去掉
            String pkTableName = column.getPkTableName();
            String fieldName = StringUtils.getLowerCamel(column.getColumnName());
            if (!StringUtils.isEmpty(pkTableName)) {
                //这里也需要把类型添加到import
                Class<?> fEntityClass = DatabaseUtils.findOrmByTableNameInPackage(pkTableName, ormPackageName);
                entityImportSet.add(ExpandField.class.getName());
                entityImportSet.add(fEntityClass.getName());
                entityBody.append("    @ExpandField(baseField=\"")
                        .append(fieldName)
                        .append("\")\n")
                        .append("    public ").append(fEntityClass.getSimpleName().replace(params.getOrmExt(), params.getxExt())).append(" ").append(fieldName.replace("Id", "")).append(";\n");
            } else {

                entityBody.append("    public ").append(StringUtils.getSimpleClassNameFromFullClassName(columnClassName)).append(" ").append(fieldName).append(";\n");
            }
        }

        //添加定义尾部
        entityBody.append("}");

        for (String ipt : entityImportSet) {
            entityHead.append("import ").append(ipt).append(";\n");
        }

        return new StringBuffer(entityHead)
                .append("\n")
                .append(entityHeadNotes)
                .append(entityBody)
                .toString();
    }

    /**
     * 构建多条件查询对象的内容 Qo或者QueryParams
     *
     * @param params
     * @return
     */
    public static String buildeQueryObjContent(BuildParams params) {
        String ormPackageName = params.getOrmPackageName();
        String ormName = params.getOrmName();
        String tableName = DatabaseUtils.getTableNameByEntityClassName(ormName);
        String xPkgName = params.getxPackageName();
        List<Column> tableColumnList = DatabaseUtils.getTableColumnList(params.getConnection(), tableName);//这里应该扫描entity获取信息

        StringBuffer entityHead = new StringBuffer();//头部，包括包名定义和导入包部分
        StringBuffer entityHeadNotes = new StringBuffer();//实体类头部注释
        StringBuffer entityBody = new StringBuffer();//实体类内容
        String entityName = StringUtils.getSimpleClassNameFromFullClassName(ormName.replace(params.getOrmExt(), ""));

        //构建头部
        Set<String> entityImportSet = new HashSet<>();
        entityHead.append("package ").append(xPkgName).append(";\n\n");//包名
        entityImportSet.add(Query.class.getName());
        entityImportSet.add(QueryParams.class.getName());
        entityImportSet.add(QueryField.class.getName());
        entityImportSet.add(PageParam.class.getName());
        entityImportSet.add(PageParams.class.getName());
        entityImportSet.add(QueryCompare.class.getName());
        entityImportSet.add(ormName);

        //构建头部注释
        entityHeadNotes.append("/**")
                .append("\n * App: ")
                .append(params.getAppName())//应用程序名
                .append("\n * Pkg: ")
                .append(params.getxPackageName())//包名
                .append("\n * Author: ")
                .append(params.getAuthor())
                .append("\n * Time: ")
                .append(params.getTime())
                .append("\n * Explain: ")
                .append(entityName + " " + params.getExplain())
                .append("\n */\n\n");

        //构建定义头部
        entityBody.append("@Query(").append(StringUtils.getSimpleClassNameFromFullClassName(ormName)).append(".class)\n")//关联表名
                .append("public class ").append(entityName).append(params.getxClassExt()).append(" implements QueryParams").append(" {\n");
        //添加public字段
        String compareSymbol;
        for (Column column : tableColumnList) {
            compareSymbol = " QueryCompare.EQUAL ";
            //判断是否可以添加到查询参数中
            if (!column.getSearchable()) {
                continue;
            }
            String columnClassName1 = column.getColumnClassName();
            //判断是否时间戳，如果是，则转换为TimeRange数据类型
            if (columnClassName1.equals(Timestamp.class.getName())) {
                columnClassName1 = TimeRange.class.getName();
                compareSymbol = " QueryCompare.BETWEEN ";
            }
            //如果字段类型不是基本数据类型或者包装类，则需要把这个类添加到import
            String fieldName = StringUtils.getLowerCamel(column.getColumnName());
            String columnClassName = getFieldTypeNameFromColumnClassName(columnClassName1, params.isParseTimeStamp(), params.getDbTypeMap());
            Class<?> columnClass = TypeUtils.getClassForName(columnClassName);
            //如果字段类型不是字符串，也不是基本数据类型以及包装类，就添加import
            if (!(TypeUtils.equalsPrimitive(columnClass) || String.class.getName().equals(columnClass.getName()))) {
                entityImportSet.add(columnClassName);
            }
            //添加注解行
            entityBody.append("    @QueryField(field=\"")
                    .append(fieldName)
                    .append("\",compare =")
                    .append(compareSymbol)
                    .append(")\n");
            //添加属性行
            entityBody.append("    public ").append(StringUtils.getSimpleClassNameFromFullClassName(columnClassName)).append(" ").append(fieldName).append(";\n");
        }
        //添加分页参数
        entityBody.append("    @PageParam\n");
        entityBody.append("    public ").append("PageParams pageParams;\n");

        //添加定义尾部
        entityBody.append("}");

        for (String ipt : entityImportSet) {
            entityHead.append("import ").append(ipt).append(";\n");
        }

        return new StringBuffer(entityHead)
                .append("\n")
                .append(entityHeadNotes)
                .append(entityBody)
                .toString();
    }

    /**
     * 创建跟数据库表建立映射关系的实体类
     *
     * @param params
     */
    public static void createOrms(EntityBuildParams params) {
        Connection connection = params.getConnection();
        //获取数据库结构
        Map<String, List<Column>> dataBaseStructMap = getDataBaseStructMap(connection);
        //遍历，创建实体类
        Set<String> keySet = dataBaseStructMap.keySet();
        for (String tableName : keySet) {
            String className = getClassNameFromTableName(params, tableName);
            IoUtils.createFileInPackage(params.getOrmPackageName(), StringUtils.getUpperCamel(className) + params.getOrmExt() + ".java", buildeOrmContent(params, tableName, className));
        }
    }

    //从表明生成一个合法的类名
    private static String getClassNameFromTableName(EntityBuildParams params, String tableName) {
        Map<String, String> ormClassNameReplaces = params.getTableNameReplaces();
        String className = tableName;
        //替换类名部分
        for (Map.Entry<String, String> entry : ormClassNameReplaces.entrySet()) {
            className = className.replace(entry.getKey(), entry.getValue());
        }
        return className;
    }

    /**
     * 创建扩展数据实体类
     *
     * @param params
     */
    public static void createXObjs(BuildParams params) {
        Connection connection = params.getConnection();
        //获取orm累类集合
        List<Class<?>> classList = ClassUtils.getClasses(params.getOrmPackageName());
        //遍历，创建扩展实体类
        for (Class<?> clazz : classList) {
            params.setOrmName(clazz.getName());
            IoUtils.createFileInPackage(params.getxPackageName(), clazz.getSimpleName().replace(params.getOrmExt(), params.getxClassExt()) + ".java", buildeXObjContent(params));
        }
    }

    /**
     * 创建多条件查询数据实体类
     *
     * @param params
     */
    public static void createQueryObjs(BuildParams params) {
        Connection connection = params.getConnection();
        //获取orm累类集合
        List<Class<?>> classList = ClassUtils.getClasses(params.getOrmPackageName());
        //遍历，创建扩展实体类
        for (Class<?> clazz : classList) {
            params.setOrmName(clazz.getName());
            IoUtils.createFileInPackage(params.getxPackageName(), clazz.getSimpleName().replace(params.getOrmExt(), params.getxClassExt()) + ".java", buildeQueryObjContent(params));
        }
    }

    /**
     * 根据数据库表列的类型获得需要的类型
     * 主要逻辑包括：
     * 1. 把基本数据类型转换为对应的包装类；
     * 2. Byte这样的数据类型设置为Integer；
     * 3. 根据时间戳解析策略决定是否将TimeStamp改为Long
     * 允许用户传一个映射表进来，自己匹配
     *
     * @param columnClassName  数据库表中列在java中的数据类型
     * @param isParseTimeStamp 是否解析TimeStamp
     * @return
     */
    public static String getFieldTypeNameFromColumnClassName(String columnClassName, boolean isParseTimeStamp, Map<String, String> dbTypeMap) {
        if (dbTypeMap == null) {
            dbTypeMap = new HashMap<>();
            dbTypeMap.put(int.class.getName(), Integer.class.getName());
            dbTypeMap.put(short.class.getName(), Short.class.getName());
            dbTypeMap.put(long.class.getName(), Long.class.getName());
            dbTypeMap.put(float.class.getName(), Double.class.getName());
            dbTypeMap.put(double.class.getName(), Double.class.getName());
            dbTypeMap.put(byte.class.getName(), Byte.class.getName());
            dbTypeMap.put(char.class.getName(), Character.class.getName());
            dbTypeMap.put(boolean.class.getName(), Byte.class.getName());
            dbTypeMap.put(Boolean.class.getName(), Byte.class.getName());
        }

        Set<String> keySet = dbTypeMap.keySet();
        if (keySet.contains(columnClassName)) {
            return dbTypeMap.get(columnClassName);
        }
        if (Timestamp.class.getName().equals(columnClassName) && isParseTimeStamp) {
            return Long.class.getName();
        }
        return columnClassName;//没有找到就不符合条件，当然就用原来的啊
    }

    /**
     * 获取创建查询对象的类型转换映射
     *
     * @return
     */
    private static Map<String, String> getQueryObjTypeMap() {
        Map<String, String> dbTypeMap = new HashMap<>();
        dbTypeMap.put(int.class.getName(), Integer.class.getName());
        dbTypeMap.put(short.class.getName(), Short.class.getName());
        dbTypeMap.put(long.class.getName(), Long.class.getName());
        dbTypeMap.put(float.class.getName(), Double.class.getName());
        dbTypeMap.put(double.class.getName(), Double.class.getName());
        dbTypeMap.put(byte.class.getName(), Byte.class.getName());
        dbTypeMap.put(char.class.getName(), Character.class.getName());
        dbTypeMap.put(boolean.class.getName(), Byte.class.getName());
        dbTypeMap.put(Boolean.class.getName(), Byte.class.getName());

        return dbTypeMap;
    }
}
