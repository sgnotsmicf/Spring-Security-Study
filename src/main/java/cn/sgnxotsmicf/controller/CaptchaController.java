package cn.sgnxotsmicf.controller;

import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.ICaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.generator.CodeGenerator;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
public class CaptchaController {

    /**
     * 生成图形验证码
     */
    @Operation(summary = "生成验证码")
    @GetMapping("/captcha")
    public void generateCaptcha(HttpServletResponse response, HttpSession session) throws IOException {
        // 1.1 创建线纹验证码（宽度120px、高度40px、验证码长度4位、干扰线5条）
        //ICaptcha captcha = CaptchaUtil.createLineCaptcha(120, 40, 4, 10);

        //1.2 圆形 gif 扭曲...
        ICaptcha captcha = CaptchaUtil.createCircleCaptcha(120, 40, 4, 10);
        //1.3 自定义生成验证码
        CodeGenerator codeGenerator = new CodeGenerator() {
            @Override
            public String generate() {
                return "";
            }

            @Override
            public boolean verify(String code, String userInputCode) {
                return false;
            }
        };
        // 2. 存储验证码文本到Session（键为captchaCode，后续校验使用）
        session.setAttribute("captchaCode", captcha.getCode());
        
        // 3. 禁止浏览器缓存验证码图片（避免刷新页面不更新验证码）
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 4. 设置响应格式为图片/png，输出验证码图片到前端
        response.setContentType("image/png");
        captcha.write(response.getOutputStream());
    }
}