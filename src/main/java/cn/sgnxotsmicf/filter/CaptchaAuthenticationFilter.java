package cn.sgnxotsmicf.filter;

import cn.sgnxotsmicf.result.R;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 验证码过滤器（适配 Spring Security 6+）,使用OncePerRequestFilter确保了无论请求经过多少次转发、包含，同一个过滤器都只会处理一次
 */
@Component
public class CaptchaAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 只需实现doFilterInternal，无需关心重复执行问题
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        boolean captchaValid = false;
        // 1. 仅拦截登录的 POST 请求（直接判断 URI 和请求方法，替代废弃的 AntPathRequestMatcher）
        if ("/login".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            // 2. 获取前端提交的验证码
            String submittedCaptcha = request.getParameter("captcha");
            String captchaKey = request.getParameter("captchaId");

            // 3. 获取 redis 中存储的验证码
            String storedCaptcha = stringRedisTemplate.opsForValue().get("captcha:uuid:"+captchaKey);

            // 4. 验证码校验逻辑
            if (StringUtils.hasText(submittedCaptcha) && StringUtils.hasText(storedCaptcha)) {
                captchaValid = submittedCaptcha.equalsIgnoreCase(storedCaptcha);
                // 校验后清除，防止重复使用
                stringRedisTemplate.delete("captcha:uuid:"+captchaKey);
            }
            // 5. 验证码错误：返回错误响应
            System.out.println("captchaValid = " + captchaValid);
            if (!captchaValid) {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(JSON.toJSONString(R.fail("验证码错误")));
                return;
            }
        }
        // 6. 不需要验证码校验的请求，继续执行后续过滤链
        filterChain.doFilter(request, response);
    }
}
