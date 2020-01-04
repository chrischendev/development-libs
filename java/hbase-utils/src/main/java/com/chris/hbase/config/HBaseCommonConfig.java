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
 * This class be use for: 配置文件 适应多实例
 */
public class HBaseCommonConfig {
    private String hadoopHomeDir;
    private String hbaseZkQuorum;
    private String hbaseZkClientPort;
    private String hbaseMaster;
    private Configuration configuration = null;
    private Connection connection = null;


    public HBaseCommonConfig create() {
        return new HBaseCommonConfig();
    }

    public HBaseCommonConfig create(String hbaseZkQuorum, String hbaseZkClientPort, String hbaseMaster) {
        return create(null, hbaseZkQuorum, hbaseZkClientPort, hbaseMaster);
    }

    public HBaseCommonConfig create(String hadoopHomeDir, String hbaseZkQuorum, String hbaseZkClientPort, String hbaseMaster) {
        HBaseCommonConfig hBaseCommonConfig = new HBaseCommonConfig();
        if (StringUtils.isNotBlank(hadoopHomeDir)) {
            System.setProperty("hadoop.home.dir", hadoopHomeDir);
        }

        BasicConfigurator.configure();//自动快速地使用缺省Log4j环境

        hBaseCommonConfig.configuration = HBaseConfiguration.create();
        hBaseCommonConfig.configuration.set("hbase.zookeeper.quorum", hbaseZkQuorum);
        hBaseCommonConfig.configuration.set("hbase.zookeeper.property.clientPort", hbaseZkClientPort);
        hBaseCommonConfig.configuration.set("hbase.master", hbaseMaster);

        try {
            hBaseCommonConfig.connection = ConnectionFactory.createConnection(configuration);
            return hBaseCommonConfig;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHadoopHomeDir() {
        return hadoopHomeDir;
    }

    public HBaseCommonConfig setHadoopHomeDir(String hadoopHomeDir) {
        this.hadoopHomeDir = hadoopHomeDir;
        return this;
    }

    public String getHbaseZkQuorum() {
        return hbaseZkQuorum;
    }

    public HBaseCommonConfig setHbaseZkQuorum(String hbaseZkQuorum) {
        this.hbaseZkQuorum = hbaseZkQuorum;
        return this;
    }

    public String getHbaseZkClientPort() {
        return hbaseZkClientPort;
    }

    public HBaseCommonConfig setHbaseZkClientPort(String hbaseZkClientPort) {
        this.hbaseZkClientPort = hbaseZkClientPort;
        return this;
    }

    public String getHbaseMaster() {
        return hbaseMaster;
    }

    public HBaseCommonConfig setHbaseMaster(String hbaseMaster) {
        this.hbaseMaster = hbaseMaster;
        return this;
    }

    public Connection buildConnection() {
        System.setProperty("hadoop.home.dir", this.hadoopHomeDir);
        BasicConfigurator.configure();//自动快速地使用缺省Log4j环境

        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", this.hbaseZkQuorum);
        conf.set("hbase.zookeeper.property.clientPort", this.hbaseZkClientPort);
        conf.set("hbase.master", this.hbaseMaster);

        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public HBaseCommonConfig setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    public Connection getConnection() {
        return connection;
    }

    public HBaseCommonConfig setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }
}
