package top.lrshuai.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
		printfUrl(applicationContext);
	}

	/**
	 * 打印地址
	 * @param application
	 */
	public static void printfUrl(ConfigurableApplicationContext application){
		Environment env = application.getEnvironment();
		String ip = "127.0.0.1";
		String port = env.getProperty("server.port");
		String property = env.getProperty("server.servlet.context-path");
		String path = property == null ? "" :  property;
		log.info("启动成功,{}:{}{}", ip,port,path);
	}
}
