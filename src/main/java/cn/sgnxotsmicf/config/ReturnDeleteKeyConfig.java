package cn.sgnxotsmicf.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Author: lixiang
 * @CreateDate: 2026/1/7 15:03
 * @Version: 1.0
 * @Description: 应用关闭时清理Redis中验证码和Token的配置类
 */
@Component
@Slf4j
public class ReturnDeleteKeyConfig implements ApplicationListener<ContextClosedEvent> {

    // Redis键前缀常量
    private static final String CAPTCHA_KEY_PREFIX = "captcha:uuid:";
    private static final String TOKEN_KEY_PREFIX = "token:user:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("开始执行应用关闭收尾工作，清理Redis中的临时数据");

        try {
            // 删除所有验证码相关的键
            deleteKeysByPattern(CAPTCHA_KEY_PREFIX);

            // 删除所有用户Token相关的键
            deleteKeysByPattern(TOKEN_KEY_PREFIX);

            log.info("应用关闭收尾工作完成，Redis临时数据清理完毕");
        } catch (Exception e) {
            log.error("清理Redis临时数据时发生异常: {}", e.getMessage(), e);
            // 不影响应用关闭流程
        }
    }

    /**
     * 根据键模式批量删除Redis中的键
     * @param pattern 键模式
     */
    private void deleteKeysByPattern(String pattern) {
        try {
            Set<String> keys = stringRedisTemplate.keys(pattern + "*");
            if (!keys.isEmpty()) {
                long deletedCount = stringRedisTemplate.delete(keys);
                log.info("成功删除Redis中键模式为[{}]的键，共删除{}个", pattern, deletedCount);
            } else {
                log.info("Redis中不存在键模式为[{}]的键", pattern);
            }
        } catch (Exception e) {
            log.error("删除Redis中键模式为[{}]的键时发生异常: {}", pattern, e.getMessage(), e);
            // 继续执行其他清理操作
        }
    }
}