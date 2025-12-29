package cn.sgnxotsmicf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.sgnxotsmicf.mapper")
@SpringBootApplication
public class SpringSecurityStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityStudyApplication.class, args);
	}

}
