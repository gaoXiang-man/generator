package cn.healthlink.pratt.service;

import cn.healthlink.pratt.config.BaseCon;
import cn.healthlink.pratt.dao.GeneratorDao;
import cn.healthlink.pratt.utils.GenUtils;
import cn.healthlink.pratt.utils.PageUtils;
import cn.healthlink.pratt.utils.Query;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 */
@Service
@Slf4j
public class SysGeneratorService {

    @Autowired
    private GeneratorDao generatorDao;
    @Autowired
    private BaseCon baseCon;


    public String getProFile() {
        String path = "";
        String name = "";
        if (Objects.nonNull(baseCon.PRO_FILE_PATH)) {
            path = baseCon.PRO_FILE_PATH;
        }
        if (Objects.nonNull(baseCon.PRO_FILE_NAME)) {
            name = baseCon.PRO_FILE_NAME;
        }
        return path + name;
    }


    public Map<String, String> queryTable(String tableName) {
        return generatorDao.queryTable(tableName);
    }

    public List<Map<String, String>> queryColumns(String tableName) {
        return generatorDao.queryColumns(tableName);
    }


    public byte[] generatorCode(List<Map<String, Object>> list) {
        log.info("循环list生成表");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        if (Objects.isNull(list) || list.size() == 0) {
            log.info("list中无数据");
            return null;
        }
        list.forEach((Map<String, Object> map) -> {
            log.info("map:" + JSONObject.toJSONString(map));
            Object tableNameO = map.get("tableName");
            if (Objects.nonNull(tableNameO)) {
                String tableName = tableNameO.toString();
                //查询表信息
                Map<String, String> table = queryTable(tableName);
                //查询列信息
                List<Map<String, String>> columns = queryColumns(tableName);
                //生成代码
                log.info("table 信息:" + JSONObject.toJSONString(table));
                log.info("columns 信息:" + JSONObject.toJSONString(columns));
                log.info("zip 信息:" + JSONObject.toJSONString(zip));
                if (Objects.isNull(table)) {
                    log.error("没有生成 表 tableName :" + tableName + ",因为没有获取到参数");
                } else {
                    GenUtils.generatorCode(table, columns, zip, GenUtils.getConfig(getProFile()), baseCon.getDBType());
                }
            }
        });
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}
