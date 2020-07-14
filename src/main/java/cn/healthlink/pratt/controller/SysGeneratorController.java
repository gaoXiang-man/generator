
package cn.healthlink.pratt.controller;

import cn.healthlink.pratt.dao.GeneratorDao;
import cn.healthlink.pratt.service.SysGeneratorService;
import cn.healthlink.pratt.utils.DateUtils;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * 代码生成器
 */
@Configuration
@Slf4j
public class SysGeneratorController {
    @Autowired
    private SysGeneratorService sysGeneratorService;

    @Autowired
    private GeneratorDao generatorDao;

    private static final String FILE_DEFAULT_PATH1 = "./";
    private static final String FILE_DEFAULT_PATH2 = ".\\";

    @Value("${file.path}")
    private String FILE_PATH;
    @Value("${file.name}")
    private String FILE_NAME;
    @Value("${file.format}")
    private String FILE_FORMAT;
    @Value("${heathlink.database.schema}")
    private String DATABASE_SCHEMA;
    @Value("${heathlink.database.type}")
    private String DATABASE_TYPE;


    public String auto() throws Exception {
        String path=FILE_PATH;
        String fileName=FILE_NAME;
        String format=FILE_FORMAT;
        //        判断路径空赋值默认路径
        File directory = new File("");
        if (FILE_PATH.equals(FILE_DEFAULT_PATH1) || FILE_PATH.equals(FILE_DEFAULT_PATH2)) {
            path = directory.getCanonicalPath();
        }
        log.info("输出文件路径:" + path);
        File file = new File(path);
        // 压缩文件的路径不存在
        if (!file.exists()) {
            throw new Exception("输出文件路径不存在 path:" + path);
        }
        //给文件名追加日期和时间
        fileName = fileName + "-" + DateUtils.format(new Date(), "yyyyMMdd-HHmmss");
        String generateFileName = path + File.separator + fileName + "." + format;
        // 输出流
        FileOutputStream outputStream = new FileOutputStream(generateFileName);
        // 压缩输出流
        log.info(generateFileName);

        Map<String, Object> map =new HashMap<>();
        if (Objects.nonNull(DATABASE_TYPE)){
            if (Objects.equals(DATABASE_TYPE.toUpperCase(),"ORACLE"))
                map.put("owner",DATABASE_SCHEMA);
        }
        List<Map<String, Object>> list = generatorDao.queryList(map);
        if (Objects.nonNull(list)) {
            if (list.size() > 0) {
                log.info("表集合:" + JSONArray.toJSONString(list));
                byte[] data = sysGeneratorService.generatorCode(list);
                outputStream.write(data);
            }
        } else {
            log.info("查出的表集合为null");
        }
        outputStream.close();
        log.info(generateFileName);
        log.info("生成结束");
        return "";
    }


}
