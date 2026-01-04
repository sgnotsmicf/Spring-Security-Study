package cn.sgnxotsmicf.config;

import cn.sgnxotsmicf.filter.CaptchaAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author: lixiang
 * @CreateDate: 2025/12/28 20:39
 * @Version: 1.0
 * @Description: SpringSecurity的一些配置
 */

@Configuration
public class SecurityConfig {

    /**
     * 注册自定义验证码过滤器
     */
    @Bean
    public CaptchaAuthenticationFilter captchaAuthenticationFilter() {
        return new CaptchaAuthenticationFilter();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 配置安全过滤链，指定自定义登录页
    // 配置这bean之后，Spring Security的某些默认行为就失效了，没有配置，需要重新配置回来
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // 授权配置：放行登录页、静态资源（css/js/img）
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login","/captcha" ,"/css/**", "/js/**", "/img/**", "/error/**").permitAll() // 放行登录页和静态资源
                        .anyRequest().authenticated() // 其他任何后端请求都需要认证
                )
                // 表单登录配置：替换自定义登录页
                .formLogin(form -> form
                        .loginPage("/login") // 指定自定义登录页的访问路径（对应 Controller 的 /login）
                        .loginProcessingUrl("/login") // 表单提交的目标路径（Spring Security 自动处理，无需自己写 Controller）
                        .usernameParameter("username") // 表单中用户名的 name 属性（需与 Thymeleaf 页面一致）
                        .passwordParameter("password") // 表单中密码的 name 属性（需与 Thymeleaf 页面一致）
                        .defaultSuccessUrl("/index") // 认证成功后默认跳转的路径
                        .failureUrl("/login?error=true") // 认证失败后跳转的路径（携带错误标识）
                )
                // 注册验证码过滤器：添加到UsernamePasswordAuthenticationFilter之前
                .addFilterBefore(captchaAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
