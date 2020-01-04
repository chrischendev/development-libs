package com.chris.framework.builder.utils;

import com.chris.framework.builder.annotation.persistence.Persistence;
import com.chris.framework.builder.annotation.persistence.PersistenceField;
import com.chris.framework.builder.annotation.persistence.SaveMethod;
import com.chris.framework.builder.core.manager.ProviderClassObject;
import com.chris.framework.builder.core.manager.ProviderFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.utils
 * Created by Chris Chen
 * 2018/1/20
 * Explain:数据持久化工具
 */
public class PersistenceUtils {
    /**
     * 保存一个对象
     * 这个对象只是单一对应某一个基本数据类
     *
     * @param object
     * @return
     */
    public static Object getBaseEntity(Object object) {
        if (object == null) {
            return null;
        }
        //1. 获取类
        Class<?> objectClass = object.getClass();
        //2. 获取类上面的持久化类注解
        Persistence persistenceAnno = objectClass.getDeclaredAnnotation(Persistence.class);
        if (persistenceAnno == null) {
//            try {
//                throw new Exception("没有找到自动持久化注解@Persistence");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            return object;//将本身返回
        }
        //3. 找到基本类
        Class<?> baseClass = persistenceAnno.value();
        if (baseClass == null) {
            try {
                throw new Exception("没找到这个基本类");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //4. 把对象转换为基本数据类对象
        //先判断有没有主键id，或者这个id是不是0
        Field idField = null;
        try {
            idField = objectClass.getDeclaredField("id");
            if (idField != null) {
                Object idValue = idField.get(object);//取到id的值
                if (idValue != null && (Integer) idValue != 0) {//如果id不为空又不为0，则说明是修改，对象要查出来
                    Object baseEntity1 = ExpandUtils.getObject(baseClass, "id", idValue);
                    EntityUtils.copyData(object, baseEntity1);
                    return baseEntity1;
                }
            }
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();

        } catch (IllegalAccessException e) {
//            e.printStackTrace();
        }

        Object baseEntity = EntityUtils.copyData(object, baseClass);
        return baseEntity;
    }

    /**
     * 保存基本数据对象
     *
     * @param baseEntity
     * @return
     */
    public static Object saveBaseEntity(Object baseEntity) {
        if (baseEntity == null) {
            return null;
        }
        //1. 获取基本数据类
        Class<?> baseClass = baseEntity.getClass();
        //2. 获取基本类的@SaveMethod注解
        SaveMethod saveMethodAnno = baseClass.getDeclaredAnnotation(SaveMethod.class);
        if (saveMethodAnno == null) {
            try {
                throw new Exception("没找到这个注解");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //3. 获取执行保存的类
        Class<?> saveClass = saveMethodAnno.providerClass();
        if (saveClass == null) {
            try {
                throw new Exception("没找到这个执行保存操作的方法");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //4. 获取执行保存的方法名
        String saveMethodName = saveMethodAnno.methodName();
        if (saveMethodName == null) {
            try {
                throw new Exception("缺少指定的方法名");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //5. 获取的执行保存的方法
        try {
            Method saveMethod = saveClass.getDeclaredMethod(saveMethodName, baseClass);
            //6. 获取提供者类的实例
            //MsgUtils.println("找到提供者类名为: " + saveClass.getName());
            ProviderClassObject providerClassObject = ProviderFactory.getProviderClass(saveClass);
            if (providerClassObject == null) {
                return null;
            }
            Object providerObject = providerClassObject.getProviderObject();
            //MsgUtils.println("找到提供者类的实例为: " + providerObject);
            //7. 执行方法
            Object result = saveMethod.invoke(providerObject, baseEntity);
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            try {
                throw new Exception("没有找到相匹配的方法");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            MsgUtils.println("找不到执行方法的对象");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存一个对象
     * 这个对象有可能是一个复杂的内聚对象
     *
     * @param object
     * @return
     */
    public static Object save(Object object) {
        //1. 获取类
        Class<?> objectClass = object.getClass();
        //判断这个对象是符合数据对象还是一个纯粹内聚的对象
        boolean isCohesion = false;//是不是纯内聚对象
        Persistence persistenceAnno = objectClass.getDeclaredAnnotation(Persistence.class);//获取注解进行判断
        if (persistenceAnno == null) {//没有这个注解就是纯内聚
            isCohesion = true;
        }
        //获取基本数据类型
        Object baseEntity = getBaseEntity(object);
        //2. 获取所有字段
        Field[] fields = objectClass.getDeclaredFields();
        //3. 遍历
        for (Field field : fields) {
            //4. 获取字段上面的注解@PersistenceField
            PersistenceField persistenceFieldAnno = field.getDeclaredAnnotation(PersistenceField.class);
            //如果这个注解没有就跳过
            if (persistenceFieldAnno == null) {
                continue;
            }
            //获取注解的参数值
            Class<?> baseEntityClass = persistenceFieldAnno.baseEntity();
            String keyFieldName = persistenceFieldAnno.keyField();
            //如果对应的类都没有，则跳过，不过这个注解参数时不能为空的
            if (baseEntityClass == null) {
                continue;
            }
            //5. 获得这个字段的对象
            Object fieldObj = null;
            try {
                field.setAccessible(true);
                fieldObj = field.get(object);
                field.setAccessible(false);
                MsgUtils.println(fieldObj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (fieldObj == null) {
                continue;
            }
            //把这个对象复制为基本数据对象
            Object fieldBaseEntity = EntityUtils.copyData(fieldObj, baseEntityClass);
            //保存这个基本数据对象
            Object fieldResult = saveBaseEntity(fieldBaseEntity);
            //如果是复合数据对象把这个返回值赋给keyField所指向的字段
            if (!isCohesion) {
                //如果这时候缺少keyFieldName，则无法给基本数据对象对应外键字段赋值
                if (StringUtils.isEmpty(keyFieldName)) {
                    continue;
                }
                try {
                    Class<?> baseEntity1Class = baseEntity.getClass();
                    if (baseEntity1Class == null) {
                        continue;
                    }
                    Field keyField = baseEntity1Class.getDeclaredField(keyFieldName);//从基本数据类型取字典
                    keyField.setAccessible(true);
                    keyField.set(baseEntity, fieldResult);//把这个结果值赋给基本数据类型的对应
                    keyField.setAccessible(false);
                    continue;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (isCohesion) {//如果是纯内聚对象，则不对对象进行保存
            return true;
        }
        //6. 保存整个对象
        return saveBaseEntity(baseEntity);
    }

    /**
     * 更新一个对象
     * 这个对象有可能是一个复杂的内聚对象
     *
     * @param object
     * @return
     */
    public static Object update(Object object) {
        //1. 获取类
        Class<?> objectClass = object.getClass();
        //判断这个对象是符合数据对象还是一个纯粹内聚的对象
        boolean isCohesion = false;//是不是纯内聚对象
        Persistence persistenceAnno = objectClass.getDeclaredAnnotation(Persistence.class);//获取注解进行判断
        if (persistenceAnno == null) {//没有这个注解就是纯内聚
            isCohesion = true;
        }
        //获取基本数据类型 这里有点区别 这个基本数据对象是按照主键id查出来再复制的
        Object baseEntity = getBaseEntity(object);
        //如果不是纯内聚，就直接保存，否则不保存
        if (!isCohesion) {
            saveBaseEntity(baseEntity);
        }
        //2. 获取所有字段
        Field[] fields = objectClass.getDeclaredFields();
        //3. 遍历
        for (Field field : fields) {
            //4. 获取字段上面的注解@PersistenceField
            PersistenceField persistenceFieldAnno = field.getDeclaredAnnotation(PersistenceField.class);
            //如果这个注解没有就跳过
            if (persistenceFieldAnno == null) {
                continue;
            }
            //获取注解的参数值
            Class<?> baseEntityClass = persistenceFieldAnno.baseEntity();
            String keyFieldName = persistenceFieldAnno.keyField();
            //如果对应的类都没有，则跳过，不过这个注解参数时不能为空的
            if (baseEntityClass == null) {
                continue;
            }
            //5. 获得这个字段的对象
            Object fieldObj = null;
            try {
                field.setAccessible(true);
                fieldObj = field.get(object);
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (fieldObj == null) {
                continue;
            }
            //把这个对象复制为基本数据对象
            Object fieldBaseEntity = getBaseEntity(fieldObj);
            //保存这个基本数据对象
            saveBaseEntity(fieldBaseEntity);
        }
        return true;
    }
}
