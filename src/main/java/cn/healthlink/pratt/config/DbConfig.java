
package cn.healthlink.pratt.config;

import cn.healthlink.pratt.dao.*;
import cn.healthlink.pratt.utils.RRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;

/**
 * 数据库配置
 */
@Configuration
public class DbConfig {
    @Value("${heathlink.database.type}")
    private String DATABASE_TYPE;
    @Autowired
    private MySQLGeneratorDao mySQLGeneratorDao;
    @Autowired
    private OracleGeneratorDao oracleGeneratorDao;
    @Autowired
    private SQLServerGeneratorDao sqlServerGeneratorDao;
    @Autowired
    private PostgreSQLGeneratorDao postgreSQLGeneratorDao;

    @Bean
    @Primary
    public GeneratorDao getGeneratorDao() {
        if ("mysql".equalsIgnoreCase(DATABASE_TYPE)) {
            return mySQLGeneratorDao;
        } else if ("oracle".equalsIgnoreCase(DATABASE_TYPE)) {
            return oracleGeneratorDao;
        } else if ("sqlserver".equalsIgnoreCase(DATABASE_TYPE)) {
            return sqlServerGeneratorDao;
        } else if ("postgresql".equalsIgnoreCase(DATABASE_TYPE)) {
            return postgreSQLGeneratorDao;
        } else {
            throw new RRException("不支持当前数据库：" + DATABASE_TYPE);
        }
    }
}
