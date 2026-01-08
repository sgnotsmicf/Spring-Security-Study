package cn.sgnxotsmicf.handler;

import cn.sgnxotsmicf.domain.po.User;
import cn.sgnxotsmicf.result.R;
import cn.sgnxotsmicf.result.StateCode;
import cn.sgnxotsmicf.util.JwtUtil;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: lixiang
 * @CreateDate: 2026/1/4 16:54
 * @Version: 1.0
 * @Description: 自定义登录成功处理逻辑
 */
@Component
public class ReturnSuccessHandler implements AuthenticationSuccessHandler {

    private final StringRedisTemplate stringRedisTemplate;

    public ReturnSuccessHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String token = JwtUtil.createToken(JSON.toJSONString(user));
        stringRedisTemplate.opsForValue().set("token:user:" + user.getId(), token, 1, TimeUnit.HOURS);
        R<String> result = R.success("success", token);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(result));
        response.setStatus(HttpStatus.OK.value());
    }
}
