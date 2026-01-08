package cn.sgnxotsmicf.controller;

import cn.sgnxotsmicf.util.LoginInfoUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.security.Principal;

@Controller
public class LoginController {

    // 映射自定义登录页，返回 Thymeleaf 模板名称（对应 resources/templates/login.html）
//    @GetMapping("/login")
//    public String showLoginPage(Principal principal) {
//        // 如果用户已登录，直接跳转到首页
//        if (principal != null) {
//            return "redirect:/index";
//        }
//
//        //获取当前登录用户的完整信息，比如用户名、id、角色、权限等
//        //Principal principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//
//        return "login"; // 指向 src/main/resources/templates 下的 login.html
//    }

    // 认证成功后的首页
//    @RequestMapping("/index")
//    @ResponseBody
//    public Principal showIndexPage(Principal principal) {
//       return principal;
//    }
//    @RequestMapping("/index")
//    @ResponseBody
//    public Principal showIndexPage(Authentication authentication) {
//        return authentication;
//    }
//    @RequestMapping("/index")
//    @ResponseBody
//    public Principal showIndexPage(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
//        return usernamePasswordAuthenticationToken;
//    }
    @RequestMapping("/index")
    @ResponseBody
    public Principal showIndexPage() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @RequestMapping("/userinfo")
    @ResponseBody
    public UserDetails showUserInfo() {
        return LoginInfoUtil.getCurrentLoginUser();
    }
}