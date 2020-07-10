
package cn.healthlink.pratt.controller;

import cn.healthlink.pratt.dao.GeneratorDao;
import cn.healthlink.pratt.service.SysGeneratorService;
import cn.healthlink.pratt.utils.PageUtils;
import cn.healthlink.pratt.utils.Query;
import cn.healthlink.pratt.utils.R;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 代码生成器
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
    @Autowired
    private SysGeneratorService sysGeneratorService;
    @Autowired
    private GeneratorDao generatorDao;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils pageUtil = sysGeneratorService.queryList(new Query(params));
        return R.ok().put("page", pageUtil);
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code")
    public void code(String tables, HttpServletResponse response) throws IOException {
        byte[] data = sysGeneratorService.generatorCode(tables.split(","));

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"python.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }


    /**
     * 生成代码
     */
    @RequestMapping("/auto")
    public void auto(Integer number, HttpServletResponse response) throws IOException {
        if (number > 0) {
            List<Map<String, Object>> list = generatorDao.queryList(new HashMap<>());
            if (Objects.nonNull(list) && list.size() == number) {
                byte[] data = sysGeneratorService.generatorCode(list);
                response.reset();
                response.setHeader("Content-Disposition", "attachment; filename=\"python.zip\"");
                response.addHeader("Content-Length", "" + data.length);
                response.setContentType("application/octet-stream; charset=UTF-8");

                IOUtils.write(data, response.getOutputStream());
            }
        }

    }
}
