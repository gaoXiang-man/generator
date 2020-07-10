package cn.healthlink.pratt;
import cn.healthlink.pratt.controller.SysGeneratorController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class GeneratorApplication {


	public static void main(String[] args) {
		SpringApplication.run(GeneratorApplication.class, args);
	}
}
