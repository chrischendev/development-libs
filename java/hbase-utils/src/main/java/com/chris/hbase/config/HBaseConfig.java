package com.chris.hbase.config;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;

/**
 * Create by Chris Chen
 * Create on 2019-03-08 09:50 星期五
 * This class be use for: 配置文件
 */
public class HBaseConfig {
    private static String hadoopHomeDir;
    private static String hbaseZkQuorum;
    private static String hbaseZkClientPort;
    private static String hbaseMaster;
    private static Configuration configuration = null;
    private static Connection connection = null;

    public static Connection createConnection(String hbaseZkQuorum, String hbaseZkClientPort, String hbaseMaster) {
        return createConnection(null, hbaseZkQuorum, hbaseZkClientPort, hbaseMaster);
    }

    public static Connection createConnection(String hadoopHomeDir, String hbaseZkQuorum, String hbaseZkClientPort, String hbaseMaster) {
        if (StringUtils.isNotBlank(hadoopHomeDir)) {
            System.setProperty("hadoop.home.dir", hadoopHomeDir);
        }

        BasicConfigurator.configure();//自动快速地使用缺省Log4j环境

        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", hbaseZkQuorum);
        configuration.set("hbase.zookeeper.property.clientPort", hbaseZkClientPort);
        configuration.set("hbase.master", hbaseMaster);

        try {
            return ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHadoopHomeDir() {
        return hadoopHomeDir;
    }

    public static void setHadoopHomeDir(String hadoopHomeDir) {
        HBaseConfig.hadoopHomeDir = hadoopHomeDir;
    }

    public static String getHbaseZkQuorum() {
        return hbaseZkQuorum;
    }

    public static void setHbaseZkQuorum(String hbaseZkQuorum) {
        HBaseConfig.hbaseZkQuorum = hbaseZkQuorum;
    }

    public static String getHbaseZkClientPort() {
        return hbaseZkClientPort;
    }

    public static void setHbaseZkClientPort(String hbaseZkClientPort) {
        HBaseConfig.hbaseZkClientPort = hbaseZkClientPort;
    }

    public static String getHbaseMaster() {
        return hbaseMaster;
    }

    public static void setHbaseMaster(String hbaseMaster) {
        HBaseConfig.hbaseMaster = hbaseMaster;
    }

    public static Connection build() {
        System.setProperty("hadoop.home.dir", HBaseConfig.hadoopHomeDir);
        BasicConfigurator.configure();//自动快速地使用缺省Log4j环境

        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", HBaseConfig.hbaseZkQuorum);
        conf.set("hbase.zookeeper.property.clientPort", HBaseConfig.hbaseZkClientPort);
        conf.set("hbase.master", HBaseConfig.hbaseMaster);

        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        HBaseConfig.configuration = configuration;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        HBaseConfig.connection = connection;
    }
}
