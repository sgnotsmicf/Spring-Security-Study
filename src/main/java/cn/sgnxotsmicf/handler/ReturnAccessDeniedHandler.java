package cn.sgnxotsmicf.handler;

import cn.sgnxotsmicf.result.R;
import cn.sgnxotsmicf.result.StateCode;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: lixiang
 * @CreateDate: 2026/1/7 17:22
 * @Version: 1.0
 * @Description: 自定义访问拒绝处理类|当用户没有访问权限时，返回自定义的拒绝信息
 */

@Component
public class ReturnAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //返回JSON格式的拒绝信息
        //设置响应头为JSON格式
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //设置响应状态码为403 Forbidden
        //response.setStatus(HttpServletResponse.SC_FORBIDDEN); //尽量不要直接在response中设置状态码，在R中设置
        //返回拒绝信息
        response.getWriter().write(JSON.toJSONString(R.builder().stateCode(StateCode.ACCESS_DENIED).message("权限不足").build()));
    }
}
