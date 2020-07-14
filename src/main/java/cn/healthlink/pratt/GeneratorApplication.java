package cn.healthlink.pratt;

import cn.healthlink.pratt.controller.SysGeneratorController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class GeneratorApplication {


    public static void main(String[] args) {

        ConfigurableApplicationContext appletContext = SpringApplication.run(GeneratorApplication.class, args);
        SysGeneratorController sysGeneratorController = appletContext.getBean("sysGeneratorController", SysGeneratorController.class);
        try {
            log.info("传入参数"+Arrays.toString(args));
            String path=null;
            String fileName=null;
            String format=null;
            if (args.length>0) {
                path =args[0];
            }
            if (args.length>1) {
                fileName =args[1];
            }
            if (args.length>2) {
                format =args[2];
            }
            sysGeneratorController.auto(path, fileName,format);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
