package cn.sgnxotsmicf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootTest
class SpringSecurityStudyApplicationTests {

	@Autowired
	private PasswordEncoder passwordEncoder;

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


	@Test
	void contextLoads2() {
		String encode = passwordEncoder.encode("123456");
		boolean matches = passwordEncoder.matches(encode, passwordEncoder.encode("123456"));
		System.out.println(matches);
		System.out.println(encode);
	}


	@Test
	public void maxSlidingWindow() {
		int k = 1;
		int[] nums = new int[]{1,-1};
		int[] result = new int[nums.length - k + 1];
		Deque<Integer> deque = new ArrayDeque<>();
		for(int i = 0; i < nums.length; i++){
			//入队列
			while(!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]){
				deque.pollLast();
			}
			deque.addLast(i);
			//队首元素出队列
			if (i - deque.peekFirst() >= k){
				deque.pollFirst();
			}
			//添加结构
			if (i >= k - 1){
				result[i - k + 1] = nums[deque.peekFirst()];
			}
		}
		System.out.println(Arrays.toString(result));
	}

	public int subarraySum(int[] nums, int k) {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(0, 1);
		int count = 0;
		int pre = 0;
        for (int num : nums) {
            pre += num;
            if (map.containsKey(pre - k)) {
                count += map.get(pre - k);
            }
            map.put(pre, map.getOrDefault(pre, 0) + 1);
        }
		return count;
	}
}