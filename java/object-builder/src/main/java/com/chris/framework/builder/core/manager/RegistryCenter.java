package com.chris.framework.builder.core.manager;

import com.chris.framework.builder.annotation.Provider;
import com.chris.framework.builder.utils.MsgUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * YuedaoXingApi
 * com.ydx.api.libs.chris
 * Created by Chris Chen
 * 2018/1/12
 * Explain:提供者注册中心
 */
public class RegistryCenter {
    private static Set<Object> providerBoxs = new HashSet<>();//这个类应该给个接口
    private static RegistryCenter instance;

    public static RegistryCenter init() {
        if (null == instance) {
            synchronized (RegistryCenter.class) {
                instance = new RegistryCenter();
            }
        }
        return new RegistryCenter();
    }

    /**
     * 扫描，注册提供者
     *
     * @param modelObjects 包含提供者类的对象管理者
     */
    public RegistryCenter reg(Object... modelObjects) {
        setProviderBox(modelObjects);
        for (Object modelObject : modelObjects) {
            Class<?> modelClass = modelObject.getClass();
            Field[] fields = modelClass.getDeclaredFields();
            //遍历对象列表
            for (Field providerObject : fields) {
                Object obj = null;//包含
                try {
                    obj = providerObject.get(modelObject);//从对象管理者中获得对象
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
                Class<?> providerClass = providerObject.getType();
                /////todo 注册为提供者类
                if (providerClass != null) {
                    //List特殊处理
                    if (providerClass.getName().equals(List.class.getName())) {
                        ProviderFactory.addProviderClass(new ProviderClassObject(providerObject.getGenericType().getTypeName(), obj));
                    } else {
                        ProviderFactory.addProviderClass(new ProviderClassObject(providerClass.getName(), obj));
                    }
                }
                //获取每个对象的定义类中的方法
                Method[] methods = providerClass.getMethods();
                //遍历这些方法
                for (Method method : methods) {
                    ProviderObject provider = getProviderObject(providerClass, method);
                    if (provider != null) {
                        provider.setObject(obj);//补上参数
                        ProviderFactory.addProvider(provider);
                    }
                }
            }
        }
        return this;
    }

    /**
     * 打印提供者集合
     */
    public void showProviderObjectSet() {
        Set<ProviderObject> providerObjectSet = ProviderFactory.getProviderObjectSet();
        StringBuilder sb = null;
        for (ProviderObject provider : providerObjectSet) {
            sb = new StringBuilder();

            Object providerObject = provider.getObject();
            Type returnClass = provider.getReturnClass();
            Method method = provider.getMethod();
            Type parameterType = provider.getParameterType();
            String parameter = provider.getParameter();

            if (providerObject != null) sb.append(providerObject.toString()).append(" ==> ");
            if (returnClass != null) sb.append(returnClass.getTypeName()).append(" ==> ");
            if (method != null) sb.append(method.getName()).append(" ==> ");

            //参数列表
            if (parameterType != null) sb.append(parameterType.getTypeName()).append(" ==> ");
            if (parameter != null) sb.append(parameter);
            System.out.println(sb.toString());
        }
    }

    public static Set<Object> getProviderBoxs() {
        return RegistryCenter.providerBoxs;
    }

    public void setProviderBox(Object... providerBoxs) {
        for (Object providerBox : providerBoxs) {
            if (providerBox != null) {
                RegistryCenter.providerBoxs.add(providerBox);
            }
        }
    }

    /**
     * 从一个类和方法获取Provider对象
     *
     * @param clazz
     * @param method
     * @return
     */
    public ProviderObject getProviderObject(Class<?> clazz, Method method) {
        //1. 获取方法名
        String methodName = method.getName();
        //2. 获取参数类型表
        Class<?>[] parameterTypes = method.getParameterTypes();
        Parameter[] parameters = method.getParameters();//无实际作用
        Parameter parameter = null;
        Type parameterType = null;
        String parameterName = null;
        if (parameters != null && parameters.length > 0) {
            parameter = parameters[0];//只取第一个参数
            parameterType = parameter.getType();
            parameterName = parameter.getName();
        }
        //3. 获取返回数据类型
        Type returnType = method.getReturnType();
        //处理List类型
        if (returnType.getTypeName().equals(List.class.getName())) {
            returnType = method.getGenericReturnType();
        }
        //4. 创建ProviderObject
        ProviderObject provider = new ProviderObject(returnType, method, parameterType, parameterName);
        // 获取@Provider注解
        Provider annotation = method.getAnnotation(Provider.class);
        //判断这个注解，如果方法上有该注解，
        if (annotation != null) {
            //取得参数名 todo 或者是数组
            String parameterName1 = annotation.value();
            if (parameterName1 != null) {
                if (parameters != null && parameters.length > 0) {
                    provider.setParameter(parameterName1);
                }
                return provider;
            }
        }
        //第二顺位：去找父类中对应的方法
        Method supperMethod = getSupperMethod(clazz, method);
        if (supperMethod != null) {
            //获取注解
            Provider annotation1 = supperMethod.getDeclaredAnnotation(Provider.class);
            if (annotation1 != null) {
                //取得参数名 todo 或者是数组
                String parameterName1 = annotation1.value();
                if (parameterName1 != null) {
                    if (parameters != null && parameters.length > 0) {
                        provider.setParameter(parameterName1);
                    }
                    return provider;
                }
            }
        }
        // 第三顺位：去找父类中对应的方法
        Method ifaceMethod = getInterfaceMethod(clazz, method);
        if (ifaceMethod != null) {
            //获取注解
            Provider annotation1 = ifaceMethod.getDeclaredAnnotation(Provider.class);
            if (annotation1 != null) {
                //取得参数名 todo 或者是数组
                String parameterName1 = annotation1.value();
                if (parameterName1 != null) {
                    if (parameters != null && parameters.length > 0) {
                        provider.setParameter(parameterName1);
                    }
                    return provider;
                }
            }
        }
        return null;
    }

    /**
     * 比较两个类数组是否完全相同
     *
     * @param clazzs1
     * @param clazzs2
     * @return
     */
    public boolean equalsClassArray(Class<?>[] clazzs1, Class<?>[] clazzs2) {
        if (clazzs1.length != clazzs2.length) {
            return false;
        }
        for (int i = 0; i < clazzs1.length; i++) {
            if (clazzs1[i] != clazzs2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较两个方法是够完全相同
     * 条件：方法名相同，返回数据类型相同，参数列表类型相同
     *
     * @param method1
     * @param method2
     * @return
     */
    public boolean equalsMethod(Method method1, Method method2) {
        //比较方法名
        if (!method1.getName().equals(method2.getName())) {
            return false;
        }
        //比较返回数据类名
        if (!method1.getReturnType().getName().equals(method2.getReturnType().getName())) {
            return false;
        }
        //比较参数类型表
        Class<?>[] method1ParameterTypes = method1.getParameterTypes();
        Class<?>[] method2ParameterTypes = method1.getParameterTypes();
        if (!equalsClassArray(method1ParameterTypes, method2ParameterTypes)) {
            return false;
        }
        return true;
    }

    /**
     * 找到父类中对应的方法
     *
     * @param clazz
     * @param method
     * @return
     */
    public Method getSupperMethod(Class<?> clazz, Method method) {
        if (clazz == null || method == null) {
            return null;
        }
        //1. 获取父类
        Type superclass = clazz.getGenericSuperclass();
        if (superclass == null) {
            return null;
        }
        //2. 获取方法
        try {
            Method method1 = superclass.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            if (method1 == null) {
                return null;
            }
            if (method.getReturnType().getName().equals(method1.getReturnType().getName())) {
                return method1;
            }

        } catch (NoSuchMethodException e) {
            //e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 获取实现的接口中的方法
     *
     * @param clazz
     * @param method
     * @return
     */
    private Method getInterfaceMethod(Class<?> clazz, Method method) {
        if (clazz == null || method == null) {
            return null;
        }
        //1.获取接口
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces == null || interfaces.length == 0) {
            return null;
        }
        //遍历接口
        for (Class<?> iface : interfaces) {
            //获取方法
            try {
                Method method1 = iface.getDeclaredMethod(method.getName(), method.getParameterTypes());
                if (method1 == null) {
                    continue;
                }
                if (method.getReturnType().getName().equals(method1.getReturnType().getName())) {
                    return method1;
                }
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public void showProviderClassObjectSet() {
        for (ProviderClassObject providerClassObject : ProviderFactory.getProviderClassObjectSet()) {
            String simpleName = providerClassObject.getProviderClassName();
            MsgUtils.println(simpleName + " --> " + providerClassObject.getProviderObject());
        }
    }

    public RegistryCenter setEntityManager(EntityManager entityManager) {
        QueryManager.init(entityManager);
        Chris.setEntityManager(entityManager);
        return this;
    }

    public RegistryCenter setJdbcTemplete(JdbcTemplate jdbcTemplete) {
        JdbcManager.init(jdbcTemplete);
        Chris.setJdbcTemplate(jdbcTemplete);
        return this;
    }
}
