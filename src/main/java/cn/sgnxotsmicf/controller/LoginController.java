package cn.sgnxotsmicf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    // 映射自定义登录页，返回 Thymeleaf 模板名称（对应 resources/templates/login.html）
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // 指向 src/main/resources/templates 下的 login.html
    }

    // 认证成功后的首页
    @GetMapping("/index")
    @ResponseBody
    public String showIndexPage() {
        return "hello Spring Security !";
    }
}