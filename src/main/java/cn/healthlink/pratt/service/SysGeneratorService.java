package cn.healthlink.pratt.service;

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

    public PageUtils queryList(Query query) {
        Page<?> page = PageHelper.startPage(query.getPage(), query.getLimit());
        List<Map<String, Object>> list = generatorDao.queryList(query);

        return new PageUtils(list, (int) page.getTotal(), query.getLimit(), query.getPage());
    }

    public Map<String, String> queryTable(String tableName) {
        return generatorDao.queryTable(tableName);
    }

    public List<Map<String, String>> queryColumns(String tableName) {
        return generatorDao.queryColumns(tableName);
    }

    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            //查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            //生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    public byte[] generatorCode(List<Map<String,Object>> list) {
        log.info("循环list生成表");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        if (Objects.isNull(list)||list.size()==0){
            log.info("list中无数据");
            return null;
        }
        list.forEach((Map<String,Object> map)->{
            log.info("map:"+ JSONObject.toJSONString(map));
            Object tableNameO=map.get("tableName");
            if (Objects.nonNull(tableNameO)){
                String tableName=tableNameO.toString();
                //查询表信息
                Map<String, String> table = queryTable(tableName);
                //查询列信息
                List<Map<String, String>> columns = queryColumns(tableName);
                //生成代码
                log.info("开始生成表 ,table:"+ JSONObject.toJSONString(table));
                log.info("开始生成表 ,columns:"+ JSONObject.toJSONString(columns));
                log.info("开始生成表 ,zip:"+ JSONObject.toJSONString(zip));
                GenUtils.generatorCode(table, columns, zip);
            }
        });
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}
