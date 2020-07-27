
package cn.healthlink.pratt.controller;

import cn.healthlink.pratt.config.BaseCon;
import cn.healthlink.pratt.dao.GeneratorDao;
import cn.healthlink.pratt.service.SysGeneratorService;
import cn.healthlink.pratt.utils.DateUtils;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

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
    private BaseCon baseCon;
    @Autowired
    private GeneratorDao generatorDao;

    private static final String FILE_DEFAULT_PATH1 = "./";
    private static final String FILE_DEFAULT_PATH2 = ".\\";


    public String auto() throws Exception {
        String path = baseCon.FILE_PATH;
        String fileName = baseCon.FILE_NAME;
        String format = baseCon.FILE_FORMAT;
        //        判断路径空赋值默认路径
        File directory = new File("");
        if (baseCon.FILE_PATH.equals(FILE_DEFAULT_PATH1) || baseCon.FILE_PATH.equals(FILE_DEFAULT_PATH2)) {
            path = directory.getCanonicalPath();
        }
        log.info("输出文件路径:" + path);
        File file = new File(path);
        // 压缩文件的路径不存在
        if (!file.exists()) {
            throw new Exception("输出文件路径不存在 path:" + path);
        }
        //给文件名追加日期和时间
        fileName = fileName + "-" + baseCon.DATABASE_SCHEMA + "-" + DateUtils.format(new Date(), "yyyyMMdd-HHmmss");
        String generateFileName = path + File.separator + fileName + "." + format;
        // 输出流
        FileOutputStream outputStream = new FileOutputStream(generateFileName);
        // 压缩输出流
        log.info(generateFileName);

        Map<String, Object> map = new HashMap<>();
        if (Objects.nonNull(baseCon.getDBType())) {
            if (Objects.equals(baseCon.getDBType().toUpperCase(), "ORACLE"))
                map.put("owner", baseCon.DATABASE_SCHEMA);
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
