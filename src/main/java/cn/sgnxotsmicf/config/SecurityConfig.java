package cn.sgnxotsmicf.config;

import cn.sgnxotsmicf.filter.CaptchaAuthenticationFilter;
import cn.sgnxotsmicf.filter.VerifyTokenLoginFilter;
import cn.sgnxotsmicf.handler.ReturnAccessDeniedHandler;
import cn.sgnxotsmicf.handler.ReturnFailureHandler;
import cn.sgnxotsmicf.handler.ReturnSuccessHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @Author: lixiang
 * @CreateDate: 2025/12/28 20:39
 * @Version: 1.0
 * @Description: SpringSecurity的一些配置
 */

@Configuration
public class SecurityConfig {

    /**
     * 自定义成功处理
     */
    @Resource
    private ReturnSuccessHandler returnSuccessHandler;

    /**
     * 自定义失败处理
     */
    @Resource
    private ReturnFailureHandler returnFailureHandler;

    /**
     * 自定义注销成功处理
     */
    @Resource
    private LogoutSuccessHandler logoutSuccessHandler;

    /**
     * 自定义拒绝访问处理|权限不足
     */
    @Resource
    private ReturnAccessDeniedHandler returnAccessDeniedHandler;

    /**
     * 注册自定义验证码过滤器
     */
    @Resource
    private CaptchaAuthenticationFilter captchaAuthenticationFilter;

    /**
     * 注册自定义token登录过滤器
     */
    @Resource
    private VerifyTokenLoginFilter verifyTokenLoginFilter;

    /**
     * 密码编码器，使用BCrypt加密密码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 注册CORS配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 1. 开启凭证允许（和前端withCredentials: true对应）
        config.setAllowCredentials(true);
        // 2. 允许前端所有域名访问
        config.addAllowedOriginPattern("*");//http://localhost:5174
        // 3. 允许所有请求头
        config.addAllowedHeader("*");
        // 4. 允许所有HTTP方法
        config.addAllowedMethod("*");
        // 5. 关键：暴露自定义响应头X-Captcha-Id，让前端能读取
        config.addExposedHeader("X-Captcha-Id");
        config.addExposedHeader("Authorization");
        // 6. 预检请求缓存时间
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return source;
    }


    // 配置安全过滤链，指定自定义登录页
    // 配置这bean之后，Spring Security的某些默认行为就失效了，没有配置，需要重新配置回来
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // 授权配置：放行登录页、静态资源（css/js/img）
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/captcha" ,"/css/**", "/js/**", "/img/**", "/error/**").permitAll() // 放行验证码和静态资源
                        .anyRequest().authenticated() // 其他任何后端请求都需要认证
                )
                // 表单登录配置：替换自定义登录页
                .formLogin(form -> form
                        //.loginPage("/login") // 指定自定义登录页的访问路径（对应 Controller 的 /login）
                        .loginProcessingUrl("/login") // 表单提交的目标路径（Spring Security 自动处理，无需自己写 Controller,只处理POST请求）
                        //.usernameParameter("username") // 表单中用户名的 name 属性（需与 Thymeleaf 页面一致）
                        //.passwordParameter("password") // 表单中密码的 name 属性（需与 Thymeleaf 页面一致）
                        //.defaultSuccessUrl("/index") // 认证成功后默认跳转的路径
                        //.failureUrl("/login?error=true") // 认证失败后跳转的路径（携带错误标识）
                        .successHandler(returnSuccessHandler)
                        .failureHandler(returnFailureHandler)
                )
                // 注销配置：指定注销路径
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler))
                // 注册验证码过滤器：添加到UsernamePasswordAuthenticationFilter之前
                .csrf(AbstractHttpConfigurer::disable) //禁用csrf跨站请求伪造，因为我们的登录页是一个静态页面，没有表单，所以不需要csrf跨站请求伪造，但是会有一些安全问题，后续会用jwt解决
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 开启跨域请求
                .addFilterBefore(captchaAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(verifyTokenLoginFilter, LogoutFilter.class)
                .sessionManagement(
                        // 配置会话管理策略为无状态（STATELESS），不创建 HttpSession
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(
                        // 配置认证失败处理|无权限访问处理
                        exception -> exception.accessDeniedHandler(returnAccessDeniedHandler)
                )
                .build();
    }

}
