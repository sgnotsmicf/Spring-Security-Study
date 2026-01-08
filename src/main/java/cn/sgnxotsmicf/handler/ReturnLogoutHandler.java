package cn.sgnxotsmicf.handler;

import cn.sgnxotsmicf.domain.po.User;
import cn.sgnxotsmicf.result.R;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: lixiang
 * @CreateDate: 2026/1/4 21:02
 * @Version: 1.0
 * @Description: 自定义注销成功处理逻辑
 */
@Component
@Slf4j
public class ReturnLogoutHandler implements LogoutSuccessHandler {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        // 自定义注销成功处理逻辑
        if (authentication != null && authentication.isAuthenticated()) {
            writeJsonResponse(response,R.success("success",null));
            User user = (User) authentication.getPrincipal();
            System.out.println("用户[" + user.getUsername() + "]登出成功");
            //删除对应用户的token
            stringRedisTemplate.delete("token:user:" + user.getId());
        }else {
            log.warn("收到未认证用户的登出请求");
            writeJsonResponse(response, R.success("fail", null));
        }

    }

        /**
         * 写入JSON响应
         * @param response HttpServletResponse对象
         * @param result 要写入的R对象
         * @throws IOException 如果写入响应时发生IO异常
         */
    private void writeJsonResponse(HttpServletResponse response, R<?> result) throws IOException {
        String jsonResponse = JSON.toJSONString(result);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush(); // 刷新缓冲区
        response.getWriter().close(); // 关闭流
    }
}
