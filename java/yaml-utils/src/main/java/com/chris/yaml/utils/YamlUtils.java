package com.chris.yaml.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.net.URL;
import java.util.Map;

/**
 * Create by Chris Chan
 * Create on 2019/4/2 10:58
 * Use for:
 */
public class YamlUtils {
    private static String resourceFileName;
    private static Class<?> contextClass;

    public static void init(Class<?> clazz,String yamlFileName) {
        YamlUtils.contextClass = clazz;
        YamlUtils.resourceFileName = yamlFileName;
    }

    public static String get(String key) {
        try {
            Yaml yaml = new Yaml();
            URL url = YamlUtils.contextClass.getClassLoader().getResource(YamlUtils.resourceFileName);
            if (null != url) {
                Map map = (Map) yaml.load(new FileInputStream(url.getFile()));
                //System.out.println(((Map) map.get("env")).get("name"));

                //分割key
                if (key.indexOf(".")>0) {
                    String[] splitKeys = key.split("\\.");
                    int length = splitKeys.length;
                    //遍历循环 到倒数第二个
                    for (int i = 0; i < length - 1; i++) {
                        map = (Map) map.get(splitKeys[i]);
                    }
                    return String.valueOf(map.get(splitKeys[length - 1]));//取最后一个
                }else{
                    return String.valueOf(map.get(key));
                }

            }
        } catch (Exception e) {

        }
        return null;
    }
}
