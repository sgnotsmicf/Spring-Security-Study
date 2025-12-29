package cn.sgnxotsmicf.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 验证码过滤器（适配 Spring Security 6+）,使用OncePerRequestFilter确保了无论请求经过多少次转发、包含，同一个过滤器都只会处理一次
 */
public class CaptchaAuthenticationFilter extends OncePerRequestFilter {

    /**
     * 只需实现doFilterInternal，无需关心重复执行问题
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 1. 仅拦截登录的 POST 请求（直接判断 URI 和请求方法，替代废弃的 AntPathRequestMatcher）
        if ("/login".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            HttpSession session = request.getSession();
            // 2. 获取前端提交的验证码
            String submittedCaptcha = request.getParameter("captcha");
            // 3. 获取 Session 中存储的验证码
            String storedCaptcha = (String) session.getAttribute("captchaCode");

            // 4. 验证码校验逻辑
            boolean captchaValid = false;
            if (StringUtils.hasText(submittedCaptcha) && StringUtils.hasText(storedCaptcha)) {
                captchaValid = submittedCaptcha.equalsIgnoreCase(storedCaptcha);
                session.removeAttribute("captchaCode"); // 校验后清除，防止重复使用
            }

            // 5. 验证码错误：跳转到登录页并携带错误标识
            if (!captchaValid) {
                response.sendRedirect("/login?captchaError=true");
                return;
            }
        }

        // 6. 验证码通过：继续执行后续过滤链（用户名密码认证）
        filterChain.doFilter(request, response);
    }
}