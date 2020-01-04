package com.chris.framework.builder.utils;

import java.io.*;

/**
 * YdxApiWebApp
 * com.ydx.app.utils
 * Created by Chris Chen
 * 2018/1/26
 * Explain:
 */
public class IoUtils {
    /**
     * 复制文件
     *
     * @param fromFileName 完整的文件路径
     * @param toFileName
     */
    public static void copyFile(String fromFileName, String toFileName) {
        File fromFile = new File(fromFileName);
        File toFile = new File(toFileName);
        copyFile(fromFileName, toFileName);
    }

    /**
     * 复制文件
     *
     * @param fromFile
     * @param toFile
     * @throws IOException
     */
    public static void copyFile(File fromFile, File toFile) {
        try {
            FileInputStream fis = new FileInputStream(fromFile);
            FileOutputStream fos = new FileOutputStream(toFile);
            byte[] b = new byte[1024];
            int n = 0;
            while ((n = fis.read(b)) != -1) {
                fos.write(b, 0, n);
            }

            fis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文本文件
     *
     * @param filePath
     * @return
     */
    public static String readTxtFile(String filePath) {

        try {
            File readFile = new File(filePath);
            if (readFile.isFile() && readFile.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(readFile), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                StringBuilder sb = new StringBuilder();
                while ((lineTxt = br.readLine()) != null) {
                    sb.append(lineTxt).append("\r\n");
                }
                br.close();
                return sb.toString();
            } else {
                System.out.println("readFile is not found!");
            }
        } catch (Exception e) {
            System.out.println("file read error!");
        }
        return null;
    }

    /**
     * 写入文件
     *
     * @param filePath
     * @return
     */
    public static File writeTxtFile(String filePath, String content) {
        File saveFile = new File(filePath); // 相对路径，如果没有则要建立一个新的output。txt文件
        try {
            saveFile.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(saveFile));
            out.write(content); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
            return saveFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在一个包下面创建一个文件，并且写入内容
     *
     * @param packageName
     * @param fileName
     * @param content
     * @return
     */
    public static File createFileInPackage(String packageName, String fileName, String content) {
        String filePath = packagePath(packageName) + "/" + fileName;//文件存放路径
        return writeTxtFile(filePath, content);
    }

    /**
     * 获取源码文件夹
     *
     * @return
     */
    public static String sourcePath() {
        String rootPath = new File("").getAbsolutePath();
        return rootPath + "/src/main/java";//源码文件夹
    }

    /**
     * 获取包文件夹
     *
     * @param packageName
     * @return
     */
    public static String packagePath(String packageName) {
        return sourcePath() + "/" + packageName.replace(".", "/");
    }
}
