package com.chris.framework.builder.core.manager;

import com.chris.framework.builder.utils.IoUtils;
import com.chris.framework.builder.utils.MsgUtils;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * YdxApiWebApp
 * com.ydx.app.config
 * Created by Chris Chen
 * 2018/4/6
 * Explain: 专用收集sql语句的属性工厂
 * 一条sql一个文件
 */
public class SqlSourceFactroy implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String s, EncodedResource encodedResource) throws IOException {
        String rc = encodedResource.toString();

        String rootPath = (new File("")).getAbsolutePath();
        String srcPath = rootPath + "/src/main/java";
        String sqlPackage = rc.substring(rc.indexOf("[") + 1, rc.lastIndexOf("]"));
        String sqlPath = srcPath + "/" + sqlPackage.replace(".", "/");

        File dir = new File(sqlPath);
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".sql");
            }
        });

        Map<String, Object> sqlMap = new HashMap<>();
        for (File file : files) {
            MsgUtils.println(file.getAbsolutePath());
            sqlMap.put(file.getName().substring(0, file.getName().lastIndexOf(".")), IoUtils.readTxtFile(file.getAbsolutePath()));
        }
        SqlManager.setSqlMap(sqlMap);
        return new MapPropertySource("sql", sqlMap);
    }
}
