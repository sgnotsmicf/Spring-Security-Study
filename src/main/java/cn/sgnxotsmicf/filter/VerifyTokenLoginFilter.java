package cn.sgnxotsmicf.filter;

import cn.sgnxotsmicf.domain.po.User;
import cn.sgnxotsmicf.result.R;
import cn.sgnxotsmicf.util.JwtUtil;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Generated;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author: lixiang
 * @CreateDate: 2026/1/7 15:19
 * @Version: 1.0
 * @Description: 验证token登录过滤器
 */
@Component
public class VerifyTokenLoginFilter extends OncePerRequestFilter {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 0. 如果是登录、注销、验证码请求，继续过滤
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/login")  || requestURI.equals("/captcha")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 1. 从请求头中获取token
        String tokenHeader = request.getHeader("Authorization");
        response.setContentType("application/json;charset=UTF-8");
        // 2. 如果token为空，说明不是登录请求，继续过滤
        if (tokenHeader != null) {
            if (!tokenHeader.startsWith("Bearer ")){
                //没有携带token,也不是去登录，直接失败
                response.getWriter().write(JSON.toJSONString(R.fail("No token")));
                return;
            }
            // 3. 如果token不为空，说明是登录请求，需要验证token
            String token = tokenHeader.replaceFirst("Bearer ", "");
            // 4. 验证token是否有效
            try {
                Boolean isValid = JwtUtil.verifyToken(token);
                if (!isValid){
                    response.getWriter().write(JSON.toJSONString(R.fail("Invalid token")));
                    return;
                }
                String UserString = JwtUtil.parseToken(token);
                User user = JSON.parseObject(UserString, User.class);
                String userId = user.getId().toString();
                String storeToken = stringRedisTemplate.opsForValue().get("token:user:" + userId);
                if (storeToken == null || !storeToken.equals(token)){
                    response.getWriter().write(JSON.toJSONString(R.fail("Invalid token")));
                    return;
                }
                // 6. 如果token有效，说明登录成功，将用户信息存入SecurityContextHolder中SecurityContext的Authentication对象中，继续过滤
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            }catch (Exception e){
                // 5. 如果token无效，说明登录失败，返回错误信息
                response.getWriter().write(JSON.toJSONString(R.fail("Invalid token")));
            }
        }
    }
}
