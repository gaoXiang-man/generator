package cn.healthlink.pratt.config;

import cn.healthlink.pratt.utils.GenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseCon {
    @Value("${spring.datasource.driverClassName}")
    public String DRIVER_CLASSNAME;
    @Value("${heathlink.properties.path}")
    public String PRO_FILE_PATH;
    @Value("${heathlink.properties.name}")
    public String PRO_FILE_NAME;
    @Value("${heathlink.file.path}")
    public String FILE_PATH;
    @Value("${heathlink.file.name}")
    public String FILE_NAME;
    @Value("${heathlink.file.format}")
    public String FILE_FORMAT;
    @Value("${heathlink.database.schema}")
    public String DATABASE_SCHEMA;

    public String getDBType(){
        return GenUtils.getDBTypeByDriverClassname(DRIVER_CLASSNAME);
    }



}
