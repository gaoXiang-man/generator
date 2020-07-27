
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
public class DbConfig extends BaseCon{
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
        if ("mysql".equalsIgnoreCase(this.getDBType())) {
            return mySQLGeneratorDao;
        } else if ("oracle".equalsIgnoreCase(this.getDBType())) {
            return oracleGeneratorDao;
        } else if ("sqlserver".equalsIgnoreCase(this.getDBType())) {
            return sqlServerGeneratorDao;
        } else if ("postgresql".equalsIgnoreCase(this.getDBType())) {
            return postgreSQLGeneratorDao;
        } else {
            throw new RRException("不支持当前数据库：" + this.getDBType());
        }
    }
}
