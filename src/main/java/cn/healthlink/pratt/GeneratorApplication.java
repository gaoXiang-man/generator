package cn.healthlink.pratt;
import cn.healthlink.pratt.controller.SysGeneratorController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.applet.AppletContext;

@SpringBootApplication
public class GeneratorApplication {


	public static void main(String[] args) {

		ConfigurableApplicationContext appletContext=SpringApplication.run(GeneratorApplication.class, args);
		SysGeneratorController sysGeneratorController=appletContext.getBean("sysGeneratorController",SysGeneratorController.class);
		try {
			sysGeneratorController.auto(null,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
