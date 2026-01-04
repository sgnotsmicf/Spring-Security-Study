package cn.sgnxotsmicf.util;

import cn.sgnxotsmicf.domain.po.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Author: lixiang
 * @CreateDate: 2025/12/30 15:29
 * @Version: 1.0
 * @Description:
 */

public class LoginInfoUtil {

    /**
     * 获取当前登录用户的完整信息，包括用户名、id、角色、权限等
     * @return 当前登录用户的完整信息
     */
    public static User getCurrentLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}
