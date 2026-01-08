package cn.sgnxotsmicf.handler;

import cn.hutool.json.JSONUtil;
import cn.sgnxotsmicf.domain.po.User;
import cn.sgnxotsmicf.result.R;
import cn.sgnxotsmicf.result.StateCode;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: lixiang
 * @CreateDate: 2026/1/4 16:53
 * @Version: 1.0
 * @Description: 自定义登录失败处理逻辑
 */
@Component
public class ReturnFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 登录失败返回JSON，不重定向
        R<StateCode> failure = R.fail("登录失败!!!");
        String failureString = JSON.toJSONString(failure);
        //String failureString = JSONUtil.toJsonStr(failure);
        System.out.println(failureString);
        //返回数据
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(failureString);
    }
}
