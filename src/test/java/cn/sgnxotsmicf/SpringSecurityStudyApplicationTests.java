package cn.sgnxotsmicf;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SpringSecurityStudyApplicationTests {

	@Test
	void contextLoads() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String plainPassword = "123456";
		// 生成完整的 BCrypt 加密密文（包含 $2a$ 开头）
		String encryptedPassword = encoder.encode(plainPassword);
		// 拼接 {bcrypt} 前缀，得到可直接存入数据库的完整密码
		String fullValidPassword = "{bcrypt}" + encryptedPassword;
		System.out.println("完整规范密码：" + fullValidPassword);
	}

}
