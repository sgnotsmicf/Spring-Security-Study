package cn.sgnxotsmicf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@MapperScan("cn.sgnxotsmicf.mapper")
@EnableMethodSecurity(prePostEnabled = true) // 开启方法级别的安全注解
@SpringBootApplication
public class SpringSecurityStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityStudyApplication.class, args);
	}

}
