# ObjectBuilder
这个框架应用于在Springboot项目中快速获取数据和快书构建数据结构返回给前端，而不用很繁琐地逐字段处理，逐层次封装。

# 依赖
最新版本号依据发行版本

        <dependency>
            <groupId>com.github.kalychen</groupId>
            <artifactId>ObjectBuilder</artifactId>
            <version>1.0.6</version>
        </dependency>

# 创建实体类的方法
public class CreateTest {

    public static void main(String[] args) {
        String dbDriver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://192.168.1.101:3306/db_chris_mall";
        String username = "root";
        String password = "gk123456";
        Connection connection = DatabaseUtils.getConnection(dbDriver, url, username, password);
        createEntity(connection);
    }

    //创建实体类
    private static void createEntity(Connection connection) {
        EntityBuildParams buildParams = EntityBuildParams.get() //获取实例
                .setConnection(connection) //网络连接
                .setOrmPackageName("com.chris.mall.model.orm") //存放实体类的包名
                .setOrmExt("Entity") //给实体类设定的后缀
                .setFieldModifier("private") //字段的访问修饰符，默认为public
                .addTableNameReplaces("tb_", "") //数据表名多余字符的替换集合
                .addIptAnnoMap(Data.class) //加于类的注解
                .addIptAnnoMap(NoArgsConstructor.class)
                .addIptAnnoMap(AllArgsConstructor.class)
                .setParseTimeStamp(false); //是否把TimeStamp类型解析为Long
        DatabaseUtils.createOrms(buildParams); //生成
    }

}
