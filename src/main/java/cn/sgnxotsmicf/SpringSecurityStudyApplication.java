package cn.sgnxotsmicf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@MapperScan("cn.sgnxotsmicf.mapper")
@EnableMethodSecurity(prePostEnabled = true) // 开启方法级别的安全注解
@SpringBootApplication
public class SpringSecurityStudyApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityStudyApplication.class, args);
	}

	/**
	 * 项目启动后执行一次
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {
		System.out.println("项目启动后执行一次");
	}
}
