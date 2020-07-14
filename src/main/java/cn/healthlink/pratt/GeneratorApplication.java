package cn.healthlink.pratt;

import cn.healthlink.pratt.controller.SysGeneratorController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class GeneratorApplication {


    public static void main(String[] args) {

        ConfigurableApplicationContext appletContext = SpringApplication.run(GeneratorApplication.class, args);
        SysGeneratorController sysGeneratorController = appletContext.getBean("sysGeneratorController", SysGeneratorController.class);
        try {
            sysGeneratorController.auto();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
