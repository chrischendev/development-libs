package com.chris.framework.builder.core.manager;

import com.chris.framework.builder.annotation.query.*;
import com.chris.framework.builder.annotation.query.compare.*;
import com.chris.framework.builder.core.exception.PageParamsNotFoundException;
import com.chris.framework.builder.model.*;
import com.chris.framework.builder.utils.*;
import com.google.gson.reflect.TypeToken;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.core
 * Created by Chris Chen
 * 2018/1/17
 * Explain:用于查询的处理器
 */
public class QueryManager {
    private static EntityManager entityManager;

    /**
     * 初始化
     *
     * @param entityManager
     * @return
     */
    public static Class<QueryManager> init(EntityManager entityManager) {
        QueryManager.entityManager = entityManager;
        return QueryManager.class;
    }

    /**
     * 构建查询条件语句
     *
     * @param condition
     * @return
     */
    public static String buildQuery(Condition condition) {
        //1. 先构建个简单的，用*
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(condition.getTableName());
        //3. 获取条件
        String listSql = condition.createSql();
        if (!StringUtils.isEmpty(listSql)) {
            sql.append(" WHERE ").append(listSql);
        }
        MsgUtils.println(sql);
        return sql.toString();
    }

    /**
     * 构建查询条件语句(取部分字段 todo 临时)
     *
     * @param condition
     * @return
     */
    public static String buildQuery(Condition condition, String... fieldNames) {
        if (condition == null) {
            return null;
        }
        Class<?> baseClass = condition.getaClass();
        if (baseClass == null || fieldNames == null || fieldNames.length == 0) {
            return buildQuery(condition);
        }
        //1. 先构建个简单的，用*
        StringBuilder sql = new StringBuilder("SELECT ");
        for (String fieldName : fieldNames) {
            sql.append(getColumnNameByOrmClass(baseClass, fieldName)).append(",");
        }
        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(" FROM ").append(condition.getTableName());
        //3. 获取条件
        String listSql = condition.createSql();
        if (!StringUtils.isEmpty(listSql)) {
            sql.append(" WHERE ").append(listSql);
        }
        MsgUtils.println(sql);
        return sql.toString();
    }

    /**
     * 构建一个查询分页的字符串(部分字段)
     *
     * @param pageable
     * @return
     */
    public static String buildPage(Pageable pageable, String... fieldNames) {
        if (pageable == null) {
            return null;
        }
        Condition condition = pageable.getCondition();
        Class<?> baseClass = condition.getaClass();
        if (baseClass == null || fieldNames == null || fieldNames.length == 0) {
            return buildQuery(condition);
        }
        //1. 先构建个简单的，用*
        StringBuilder sql = new StringBuilder("SELECT ");
        for (String fieldName : fieldNames) {
            sql.append(getColumnNameByOrmClass(baseClass, fieldName)).append(",");
        }
        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(" FROM ").append(condition.getTableName());
        //3. 获取条件
        String pageSql = pageable.create();
        if (!StringUtils.isEmpty(pageSql)) {
            sql.append(" WHERE ").append(pageSql);
        }
        MsgUtils.println(sql);
        return sql.toString();
    }

    /**
     * 构建一个查询分页的字符串
     *
     * @param pageable
     * @return
     */
    public static String buildPage(Pageable pageable) {
        //1. 先构建个简单的，用*
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(pageable.getCondition().getTableName());
        //3. 获取条件
        String pageSql = pageable.create();
        if (!StringUtils.isEmpty(pageSql)) {
            sql.append(" WHERE ").append(pageSql);
        }
        MsgUtils.println(sql);
        return sql.toString();
    }

    /**
     * 创建一个用于查询总数的sql字符串
     *
     * @param condition
     * @return
     */
    public static String buildCount(Condition condition) {
        //1. 先构建个简单的，用*
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ").append(condition.getTableName());
        //3. 获取条件
        String sql1 = condition.createSql();
        if (!StringUtils.isEmpty(sql1)) {
            sql.append(" WHERE ").append(sql1);
        }
        return sql.toString();
    }

    /**
     * 创建一个获取id数组的sql
     *
     * @param condition
     * @return
     */
    public static String buildIds(Condition condition) {
        //1. 先构建个简单的，用*
        StringBuilder sql = new StringBuilder("SELECT id FROM ").append(condition.getTableName());
        //3. 获取条件
        String sql1 = condition.createSql();
        if (!StringUtils.isEmpty(sql1)) {
            sql.append(" WHERE ").append(sql1);
        }
        return sql.toString();
    }

    /**
     * 构建查询条件
     *
     * @param object
     * @return
     */
    public static Condition buildCondition(Object object) {
        if (object == null) {
            return null;
        }
        StringBuilder condition = new StringBuilder("1=1");

        Class<?> clazz = object.getClass();
        String tableName = getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //如果这个字段被标为@OutField,表示与数据库无关，不需要处理
            if (field.getDeclaredAnnotation(Exclude.class) != null
                    || field.getDeclaredAnnotation(OutField.class) != null
                    || field.getDeclaredAnnotation(PageParam.class) != null) {
                continue;
            }
            //获取列名
            String columnName = getColumnName(clazz, field);
            //获取字段名
            String compareSymbol = getDefaultCompareSymbol(field);
            //获取注解
            QueryField queryFieldAnno = field.getDeclaredAnnotation(QueryField.class);
            //如果注解不为空，则从注解取值替代，否则使用默认参数：字段名就是本类的字段名，比较方式就是相等
            if (queryFieldAnno != null) {
                QueryCompare compare = queryFieldAnno.compare();
                if (compare != null) {
                    compareSymbol = compare.getSymbol();
                }
            }
            //取值
            field.setAccessible(true);
            try {
                Object obj = field.get(object);
                if (obj == null) {
                    continue;
                }
                String fieldValue = getValueString(field, obj);
                ////插入判断 如果是数组或者集合，这里要把字段的值转换为数组
                if (obj instanceof Object[]) {
                    fieldValue = getArrayString(obj);
                    //如果没有注解，或者比较符号不符合要求，则默认设置为IN
                    if (queryFieldAnno == null) {
                        if (compareSymbol != QueryCompare.IN.getSymbol() && compareSymbol != QueryCompare.NOT_IN.getSymbol()) {
                            compareSymbol = QueryCompare.IN.getSymbol();
                        }
                        columnName = getColumnName(clazz, TypeUtils.getFieldByNameFromClass(clazz, field.getName()));
                    } else {
                        //todo 这里需要判断一下有没有相关的注解@QueryField，否则就匹配本字段
                        if (field.isAnnotationPresent(QueryField.class)) {
                            String fieldName = queryFieldAnno.field();
                            if (!StringUtils.isEmpty(fieldName)) {
                                columnName = getColumnName(clazz, TypeUtils.getFieldByNameFromClass(clazz, fieldName));
                            }
                        }
                    }
                }
                //插入判断 如果是Range或者TimeRange，则符号必然是BETWEEN
                if (obj instanceof Range || obj instanceof TimeRange) {
                    //如果没有注解，或者比较符号不符合要求，则默认设置为BETWEEN
                    if (queryFieldAnno == null ||
                            (compareSymbol != QueryCompare.BETWEEN.getSymbol() && compareSymbol != QueryCompare.NOT_BETWEEN.getSymbol())) {
                        compareSymbol = QueryCompare.BETWEEN.getSymbol();
                    }
                }

                if (!StringUtils.isEmpty(fieldValue)) {
                    //不为空就连接
                    condition.append(" AND ")
                            .append(tableName)
                            .append(".")
                            .append(columnName);
                    //完善比较符号。如果比较符号中还有占位符，则按照规则替换
//                    if (!StringUtils.isEmpty(compareSymbol) && !StringUtils.isEmpty(columnName)) {
                    if (field.isAnnotationPresent(LikeStart.class)
                            || field.isAnnotationPresent(LikeEnd.class)
                            || field.isAnnotationPresent(LikeMiddle.class)) {

                        compareSymbol = compareSymbol.replace("${COLUMN_VALUE}", fieldValue);
                        condition.append(compareSymbol);
                    } else {
                        condition.append(compareSymbol).append(fieldValue);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;//出异常也忽略
            } finally {
                field.setAccessible(false);
            }

        }
        return new Condition(TypeUtils.getBaseEntityClass(clazz), condition).setTableName(tableName);
    }

    /**
     * 获得一个字段的默认查询比较符
     *
     * @param field
     * @return
     */
    private static String getDefaultCompareSymbol(Field field) {
        //1. 首先设置默认为相当于
        String compareSymbol = QueryCompare.EQUAL.getSymbol();
        //2. 获取字段注解，改变比较符号
        if (field.isAnnotationPresent(Equals.class)) {
            compareSymbol = "=";
        }
        if (field.isAnnotationPresent(NotEquals.class)) {
            compareSymbol = " !=";
        }
        if (field.isAnnotationPresent(GreaterThan.class)) {
            compareSymbol = ">";
        }
        if (field.isAnnotationPresent(NotGreaterThan.class)) {
            compareSymbol = "<=";
        }
        if (field.isAnnotationPresent(LessThan.class)) {
            compareSymbol = "<";
        }
        if (field.isAnnotationPresent(NotLessThan.class)) {
            compareSymbol = ">=";
        }
        if (field.isAnnotationPresent(In.class)) {
            compareSymbol = " IN ";
        }
        if (field.isAnnotationPresent(NotIn.class)) {
            compareSymbol = " NOT IN ";
        }
        if (field.isAnnotationPresent(Between.class)) {
            compareSymbol = " BETWEEN ";
        }
        if (field.isAnnotationPresent(NotBetween.class)) {
            compareSymbol = " NOT BETWEEN ";
        }
        if (field.isAnnotationPresent(Like.class)) {
            compareSymbol = " LIKE ";
        }
        if (field.isAnnotationPresent(LikeStart.class)) {
            compareSymbol = " LIKE '${COLUMN_VALUE}%'";//需要修改替换规则
        }
        if (field.isAnnotationPresent(LikeEnd.class)) {
            compareSymbol = " LIKE '%${COLUMN_VALUE}'";
        }
        if (field.isAnnotationPresent(Equals.class)) {
            compareSymbol = " LIKE '%${COLUMN_VALUE}%'";//需要修改替换规则
        }
        /*
        if (field.isAnnotationPresent(OrderByAsc.class)) {
            compareSymbol = " ORDER BY ${COLUMN_NAME} ASC ";//需要修改替换规则
        }
        if (field.isAnnotationPresent(OrderByDesc.class)) {
            compareSymbol = " ORDER BY ${COLUMN_NAME} DESC ";//需要修改替换规则
        }
        if (field.isAnnotationPresent(OrderBy.class)) {
            OrderBy orderBy = field.getDeclaredAnnotation(OrderBy.class);
            OrderType value = orderBy.value();
            if (value != null) {
                compareSymbol = " ORDER BY ${COLUMN_NAME}" + value.getSymbol();
            }
        }
        if (field.isAnnotationPresent(GroupBy.class)) {
            compareSymbol = " GROUP BY ${COLUMN_NAME} ";//需要修改替换规则
        }
        */
        return compareSymbol;
    }

    /**
     * 把值结果构建为sql中的String
     *
     * @param field
     * @param value
     * @return
     */
    private static String getValueString(Field field, Object value) {
        StringBuilder stringBuilder = new StringBuilder();
        if (value instanceof Range) {//数值范围
            stringBuilder.append(((Range) value).min)
                    .append(" AND ")
                    .append(((Range) value).max);
        } else if (value instanceof TimeRange) {//时间范围
            stringBuilder.append(((TimeRange) value).start)
                    .append(" AND ")
                    .append(((TimeRange) value).end);
        } else {
            stringBuilder.append(TypeUtils.equalsPrimitive(field.getType()) ? "" : "'")
                    .append(value)
                    .append(TypeUtils.equalsPrimitive(field.getType()) ? "" : "'");
        }
        return stringBuilder.toString();
    }

    /**
     * 获得数组字符串
     *
     * @param obj
     * @return
     */
    private static String getArrayString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof Object[])) {
            return null;
        }

        String arrayString = JsonUtils.toJson(obj);
        return arrayString.replace("[", "(").replace("]", ")");
    }

    /**
     * 获取查询类关联的表名
     *
     * @param clazz
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        //1. 获取本类注解
        Query queryAnnotation = clazz.getDeclaredAnnotation(Query.class);
        Class<?> baseClass = null;
        if (queryAnnotation != null) {
            baseClass = queryAnnotation.value();
        } else {
            baseClass = clazz;
        }
        //2. 获取ORM类的注解
        Entity entityAnnotation = baseClass.getDeclaredAnnotation(Entity.class);
        if (entityAnnotation == null) {
            return null;
        }
        return entityAnnotation.name();
    }

    /**
     * 获取数据表中的列名
     *
     * @param clazz
     * @param field
     * @return
     */
    public static String getColumnName(Class<?> clazz, Field field) {
        if (clazz == null || field == null) {
            return null;
        }
        //MsgUtils.println("getColumnName==>className: " + clazz.getCanonicalName() + ",fieldName: " + field.getName());
        //1. 先认为这个字段名就是列名
        String columnName = field.getName();
        //2. 获取字段注解
        QueryField queryFieldAnnotation = field.getDeclaredAnnotation(QueryField.class);
        if (queryFieldAnnotation != null) {
            //3. 映射在ORM类中的字段名
            String queryFieldName = queryFieldAnnotation.field();
            if (!StringUtils.isEmpty(queryFieldName)) {
                //4. 如果取到值，就拿这个字段名暂时作为列名
                columnName = queryFieldName;
            }
        }

        //5. 获取ORM类
        Query queryAnnotation = clazz.getDeclaredAnnotation(Query.class);
        if (queryAnnotation == null) {
            return columnName;
        }
        Class<?> ormClazz = queryAnnotation.value();
        if (ormClazz == null || ormClazz.getName().equals(Self.class)) {
            ormClazz = clazz;//如果找到了@Query注解却没有value值或者时默认值Self，则表示自己就是基本数据类
        }
        //7. 拿到字段以及getter和setter方法上面@Column注解的name
        String colName = getColumnNameByOrmClass(ormClazz, columnName);//有可能是注解的值，也有可能就是字段名
        if (StringUtils.isEmpty(colName)) {
            return columnName;
        }
        return colName;
    }

    /**
     * 从基本数据类上面获取数据表的列名
     *
     * @param ormClazz
     * @param fieldName
     * @return
     */
    public static String getColumnNameByOrmClass(Class<?> ormClazz, String fieldName) {
        if (ormClazz == null || StringUtils.isEmpty(fieldName)) {
            return null;
        }
        //1. 找到这个对应的字段
        Field ormField = null;
        try {
            ormField = ormClazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
            return null;//如果没有找到这个字段就返回空
        }
        if (ormField == null) {
            return null;
        }
        //2. 拿到字段以及getter和setter方法上面@Column注解的name
        return getColName(ormClazz, ormField);//有可能是注解的值，也有可能就是字段名
    }

    /**
     * 获取字段名或者他的setter和getter方法上面的@Column注解的name
     *
     * @param clazz
     * @param field
     * @return
     */
    private static String getColName(Class<?> clazz, Field field) {
        //MsgUtils.println("getColNameBysetterOrGetter==>className: " + clazz.getCanonicalName() + ",fieldName: " + field.getName());
        if (field == null || clazz == null) {
            return null;
        }
        String fieldName = field.getName();
        try {
            PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
            Method readMethod = pd.getReadMethod();
            String colName1 = getColName(readMethod);
            if (!StringUtils.isEmpty(colName1)) {
                return colName1;//getter优先
            }
            Method writeMethod = pd.getWriteMethod();
            String colName2 = getColName(writeMethod);
            if (!StringUtils.isEmpty(colName2)) {
                return colName2;//setter其次
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        //获取
        //如果getter和setter上面都没有，就取字段上面的拿到字段注解 todo 错了应该是拿到字段注解或者getter注解
        Column column = field.getDeclaredAnnotation(Column.class);
        if (column != null) {
            String colName = column.name();
            if (colName != null) {
                return colName;
            }
        }
        //如果都没有找到，那就是字段名了
        return fieldName;
    }

    /**
     * 获取方法上面的@Column注解的name，即数据表的列名
     *
     * @param method
     * @return
     */
    private static String getColName(Method method) {
        //MsgUtils.println("getColName2==>methodName: " + method.getName());
        if (method != null) {
            Column annotation = method.getDeclaredAnnotation(Column.class);
            if (annotation != null) {
                String colName = annotation.name();
                if (!StringUtils.isEmpty(colName)) {
                    return colName;
                }
            }
        }
        return null;
    }

    /**
     * 执行查询
     *
     * @param sql
     * @param <T>
     * @return
     */
    public static <T> List<T> query(String sql, Class<T> clazz) {
        if (entityManager == null) {
            try {
                throw new Exception("EntityManager is null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        javax.persistence.Query nativeQuery = entityManager.createNativeQuery(sql, clazz);
        List<T> resultList = nativeQuery.getResultList();
        return resultList;
    }

    /**
     * 执行查询
     *
     * @param condition
     * @param <T>
     * @return
     */
    public static <T> List<T> query(Condition condition, Class<T> clazz) {
        javax.persistence.Query nativeQuery = entityManager.createNativeQuery(buildQuery(condition), clazz);
        List<T> resultList = nativeQuery.getResultList();
        return resultList;
    }

    public static <T> PageModel<T> query(Pageable pageable, Class<T> clazz) {
        if (pageable == null || clazz == null) {
            return null;
        }
        //获取条件
        int page = pageable.getPage();
        int pageSize = pageable.getPageSize();
        Condition condition = pageable.getCondition();
        String sql = buildPage(pageable);
        //MsgUtils.println(sql);//打印日志
        javax.persistence.Query nativeQuery = entityManager.createNativeQuery(sql, clazz);
        List<T> resultList = nativeQuery.getResultList();
        long count = count(condition);
        boolean hasNext = page * pageSize < count;
        return new PageModel<>(page, resultList.size(), count, hasNext, resultList);
    }

    /**
     * 求总数
     *
     * @param condition
     * @return
     */
    public static long count(Condition condition) {
        //表名称
        javax.persistence.Query nativeQuery = entityManager.createNativeQuery(buildCount(condition));
        return Long.valueOf(String.valueOf(nativeQuery.getResultList().get(0)));
    }

    /**
     * 获取满足条件的id数组
     *
     * @param condition
     * @return
     */
    public static Integer[] queryIds(Condition condition) {
        //表名称
        javax.persistence.Query nativeQuery = entityManager.createNativeQuery(buildIds(condition));
        return JsonUtils.fromJson(JsonUtils.toJson(nativeQuery), new TypeToken<Integer[]>() {
        }.getType());
    }

    /**
     * 给一个查询条件增加排序
     * <p>
     * 通过类clazz找到基本数据类型，获取数据库表信息，在构建好的基本查询sql条件后面增加排序条件
     *
     * @param clazz     返回数据类
     * @param params    参数体
     * @param condition 查询条件
     */
    private static void addSortToCondition(Class<?> clazz, Object params, Condition condition) {
        //3. 获取排序数据
        Field sortField = TypeUtils.getFieldByTypeFromObject(params, Sort.class);
        if (sortField != null) {
            sortField.setAccessible(true);
            try {
                Sort sort = (Sort) sortField.get(params);
                //sort中的byField要从数据表中获取列名
                String byFieldName = sort.getByField();
                sort.setByField(getColumnNameByOrmClass(TypeUtils.getBaseEntityClass(clazz), byFieldName));
                condition.sort(sort);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            sortField.setAccessible(false);
        }
    }

    /**
     * 请求一个集合
     *
     * @param params
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> queryList(Object params, Class<T> clazz) {
        //获取基本数据类型
        Class<?> baseEntityClass = TypeUtils.getBaseEntityClass(clazz);
        Condition condition = buildCondition(params);
        addSortToCondition(clazz, params, condition);//增加排序条件
        String sql = QueryManager.buildQuery(condition);
        if (baseEntityClass == null) {
            return QueryManager.query(sql, clazz);
        }
        //否则就需要扩展
        return ExpandUtils.expandList(QueryManager.query(sql, baseEntityClass), clazz);
    }

    /**
     * 请求一个列表
     *
     * @param params
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> PageModel<T> queryPage(Object params, Class<T> clazz) {
        //1. 先从参数体中找到page数据
        Field pageField = TypeUtils.getFieldByTypeFromObject(params, PageParams.class);
        //没有分页参数，操作就不执行
        if (pageField == null) {
            //应该抛个异常出来
            throw new PageParamsNotFoundException();
        }
        //2. 构建查询条件
        Pageable pageable = null;
        Condition condition = buildCondition(params);
        //3. 增加排序条件
        addSortToCondition(clazz, params, condition);
        //增加分页条件
        pageField.setAccessible(true);
        try {
            PageParams pageParams = (PageParams) pageField.get(params);
            if (pageParams != null) {
                pageable = condition.page(pageParams);
            } else {
                pageable = condition.page(new PageParams(1, 10));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        pageField.setAccessible(false);

        //MsgUtils.println(pageable.persistence());
        PageModel<T> PageModel = QueryManager.query(pageable, (Class<T>) TypeUtils.getBaseEntityClass(clazz));
        return PageModel.expandPage(clazz);
    }
}
