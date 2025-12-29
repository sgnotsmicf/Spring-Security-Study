package cn.sgnxotsmicf.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sgnxotsmicf
 * @since 2025-12-28
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Operation(summary = "用户登录")
    @RequestMapping("/call")
    public String index() {

        return "Hello Spring Security Study, I am Lixiang !";
    }
}
