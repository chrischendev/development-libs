package com.chris.framework.builder.utils;

import com.chris.framework.builder.model.OneKeyParams;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * YdxApiWebApp
 * com.ydx.app.utils
 * Created by Chris Chen
 * 2018/1/26
 * Explain:关于一键操作的工具
 */
public class OneKeyUtils {

    /**
     * 一键生成
     *
     * @param params
     */
    public static void onkey(OneKeyParams params) {
        //构建必要参数
        String rootPath = new File("").getAbsolutePath();//项目根文件夹
        String srcPath = rootPath + "/src/main/java";//源码文件夹
        String templeteFilePath = srcPath + "/" + params.getTempletePackage().replace(".", "/") + "/" + params.getTempleteFileName();//模版文件全路径
        String targetPath = srcPath + "/" + params.getTargetPackage().replace(".", "/");//目标存放全路径

        String tempContent = IoUtils.readTxtFile(templeteFilePath);//读取模板文件内容
        //获取包下的所有类
        List<Class<?>> classList = ClassUtils.getClasses(params.getOrmPackageName());
        Map<String, String> classTagMap = params.getClassTagMap();
        String targetFilePath = null;
        String targetContent = null;
        for (Class<?> clazz : classList) {
            String classSimpleName = clazz.getSimpleName().replace(params.getOrmExt(), "");

            targetFilePath = targetPath + "/" + classSimpleName + params.getTargetFileExt() + ".java";//目标文件全名

            //替换
            Map<String, String> replaceSchemeMap = params.getReplaceSchemeMap();
            //添加替换类的
            replaceSchemeMap.put(params.getClassPlaceHolder(), classSimpleName);
            replaceSchemeMap.put(StringUtils.getLowerCamel(params.getClassPlaceHolder()), StringUtils.getLowerCamel(classSimpleName));//lowerCamel替换方案
            Set<String> replaceKeySet = replaceSchemeMap.keySet();
            targetContent = new String(tempContent);
            //替换数据类标记
            String replacement = classTagMap.get(classSimpleName.toLowerCase().trim());
            if (StringUtils.isEmpty(replacement)) {
                replacement = classSimpleName;//如果集合中没有对应的标记，就用简短类名来代替
            }
            targetContent = targetContent.replace(params.getClassTagPlaceHolder(), replacement);
            for (String placeHolder : replaceKeySet) {
                targetContent = targetContent.replace(placeHolder, replaceSchemeMap.get(placeHolder));
            }
            //放大招
            IoUtils.writeTxtFile(targetFilePath, targetContent);
        }
    }

    /**
     * 构建ServiceManager文件的主体部分
     *
     * @param packageName service类所在的包名
     * @return
     */
    public static String buildSManBody(String packageName) {
        List<Class<?>> classList = ClassUtils.getClasses(packageName);

        StringBuffer smanBody = new StringBuffer();
        for (Class<?> clazz : classList) {
            smanBody.append("    @Autowired\n    public ")
                    .append(clazz.getSimpleName())
                    .append(" ")
                    .append(StringUtils.lowerFirstLetter(clazz.getSimpleName()))
                    .append(";\n");
        }
        return smanBody.toString();
    }

    /**
     * 替换文件内容
     * 逻辑是把文件内容全部读出来，替换相应内容后再写回去
     *
     * @param fullFileName
     * @param oldstr
     * @param newStr
     * @return
     */
    public static boolean replaceFileContent(String fullFileName, String oldstr, String newStr) {
        String content = IoUtils.readTxtFile(fullFileName);
        content = content.replace(oldstr, newStr);
        return IoUtils.writeTxtFile(fullFileName, content) != null;
    }
}
