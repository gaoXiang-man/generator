
package cn.healthlink.pratt.utils;

import cn.healthlink.pratt.entity.ColumnEntity;
import cn.healthlink.pratt.entity.TableEntity;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 */
@Slf4j
@org.springframework.context.annotation.Configuration
public class GenUtils {

    private static final String DEFAULT_FILE_NAME = "generator.properties";

    private static String DEFAULT_DATA_TYPE="String";


    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add("template/PythonTableCreate.sql.vm");
        templates.add("template/PythonTableQuery.py.vm");
        templates.add("template/CreateJob.job.vm");
        return templates;
    }

    /**
     * 生成代码
     */
    public static void generatorCode(Map<String, String> table,
                                     List<Map<String, String>> columns,
                                     ZipOutputStream zip,
                                     Configuration config) {
        log.info("生成开始,table:" + JSONObject.toJSONString(table));
        //配置信息
        boolean hasBigDecimal = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName"));
        tableEntity.setSchema(table.get("schema"));
        tableEntity.setSchemaL(table.get("schema").toLowerCase());
        tableEntity.setComments(table.get("tableComment"));
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config.getString("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for (Map<String, String> column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setComments(column.get("columnComment"));
            columnEntity.setExtra(column.get("extra"));

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), DEFAULT_DATA_TYPE);
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        String mainPath = config.getString("mainPath");
        mainPath = StringUtils.isBlank(mainPath) ? "io.renren" : mainPath;

        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("schema", tableEntity.getSchema());
        map.put("schemaL", tableEntity.getSchemaL());
        map.put("tableName", tableEntity.getTableName().toLowerCase());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("mainPath", mainPath);
        map.put("package", config.getString("package"));
        map.put("moduleName", config.getString("moduleName"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                //添加到zip
                String fileName = getFileName(template, tableEntity.getTableName().toLowerCase(), config.getString("package"), tableEntity.getSchemaL());
                if (Objects.nonNull(fileName)) {
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zip.putNextEntry(zipEntry);
                    IOUtils.write(sw.toString(), zip, "UTF-8");
                    IOUtils.closeQuietly(sw);
                    zip.closeEntry();
                }
            } catch (IOException e) {
                System.out.println(e.toString());
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName().toLowerCase(), e);
            }
        }
        log.info("生成结束");
    }


    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig(String fileName) {
        try {
            return new PropertiesConfiguration(fileName);
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        return getConfig(DEFAULT_FILE_NAME);
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String tableName, String packageName, String schema) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + schema + File.separator;
        }

        if (template.contains("PythonTableCreate.sql.vm")) {
            return  schema + File.separator+ "sql" + File.separator + tableName + ".sql";
        }
        if (template.contains("PythonTableQuery.py.vm")) {
            return schema + File.separator + "python"+ File.separator + tableName + File.separator + tableName + ".py";
        }
        if (template.contains("CreateJob.job.vm")) {
            return schema + File.separator + "zip" + File.separator + tableName + ".job";
        }
        return null;
    }
}
