
package cn.healthlink.pratt.controller;

import cn.healthlink.pratt.dao.GeneratorDao;
import cn.healthlink.pratt.service.SysGeneratorService;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 代码生成器
 */
@Controller
@Slf4j
public class SysGeneratorController {
    @Autowired
    private SysGeneratorService sysGeneratorService;
    @Autowired
    private GeneratorDao generatorDao;

    public String auto(String path,String format) throws Exception {
        if (Objects.isNull(path)){
            File directory = new File("");
            path = directory.getCanonicalPath() ;
        }
        if (Objects.isNull(format)){
            format="zip";
        }
        log.info("输出文件路径:"+path);
        File file = new File(path);
        // 压缩文件的路径不存在
        if (!file.exists()) {
            throw new Exception("输出文件路径不存在 path:"+path);
        }
        String generateFileName = path + File.separator  + "python" + "." + format;
        // 输出流
        FileOutputStream outputStream = new FileOutputStream(generateFileName);
        // 压缩输出流
        log.info(generateFileName);
        List<Map<String, Object>> list = generatorDao.queryList(new HashMap<>());
        if (Objects.nonNull(list)) {
            if (list.size() > 0) {
                log.info("表集合:"+JSONArray.toJSONString(list));
                byte[] data = sysGeneratorService.generatorCode(list);
                outputStream.write(data);
            }
        } else {
            log.info("查出的表集合为null");
        }
        outputStream.close();
        log.info("生成结束");
        return "";
    }


    public String getPath()
    {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if(System.getProperty("os.name").contains("dows"))
        {
            path = path.substring(1,path.length());
        }
        if(path.contains("jar"))
        {
            path = path.substring(0,path.lastIndexOf("."));
            return path.substring(0,path.lastIndexOf("/"));
        }
        return path.replace("target/classes/", "");
    }
}
