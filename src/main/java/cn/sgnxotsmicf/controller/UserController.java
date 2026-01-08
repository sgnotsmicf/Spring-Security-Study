package cn.sgnxotsmicf.controller;

import cn.sgnxotsmicf.domain.po.Permission;
import cn.sgnxotsmicf.domain.po.User;
import cn.sgnxotsmicf.mapper.PermissionMapper;
import cn.sgnxotsmicf.mapper.UserMapper;
import cn.sgnxotsmicf.result.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端用户控制器
 * </p>
 *
 * @author sgnxotsmicf
 * @since 2025-12-28
 */
@RestController
@RequestMapping("/users")
public class UserController {
    /**
     * 1.hasRole(String role) 方法用于检查当前用户是否有指定的角色。
     * 它会自动在角色名称前添加"ROLE_"前缀，例如：hasRole("admin") 会检查用户是否有"ROLE_ADMIN"角色。
     * 所以如果要使用hasRole方法，角色名称需要包含"ROLE_"前缀，在实现UserDetailsService的loadUserByUsername方法时，
     * 要确保返回的UserDetails对象的getAuthorities方法返回的权限列表中包含"ROLE_"前缀的角色。
     * 2.而hasAuthority(String authority) 方法则直接检查用户是否有指定的权限，不添加任何前缀。
     * 所以如果要使用hasAuthority方法，权限名称不需要包含"ROLE_"前缀，在实现UserDetailsService的loadUserByUsername方法时，
     * 要确保返回的UserDetails对象的getAuthorities方法返回的权限列表中包含权限名称。
     */
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;



    @RequestMapping("/list")
    public R<List<User>> userList() {
        List<User> users = userMapper.selectList(null);
        // 为每个用户加载权限信息
        users.forEach(user -> {
            List<Permission> permissions = permissionMapper.selectPermissionByUserId(user.getId());
            user.setPermissionList(permissions);
        });

        return R.success("用户列表", users);
    }

    @PreAuthorize(value = "hasAuthority('user')")
    @Operation(summary = "用户打招呼")
    @RequestMapping("/call")
    public String call() {

        return "Hello Spring Security Study, I am Lixiang !";
    }

    @PreAuthorize(value = "hasAnyAuthority('user', 'guest')")
    @Operation(summary = "用户打招呼并说点什么")
    @RequestMapping("/callandSay")
    public String callandSay() {

        return "Hello Spring Security Study, I am Lixiang !";
    }

    @PreAuthorize(value = "hasAuthority('guest')")
    @Operation(summary = "用户打招呼但不说点什么")
    @RequestMapping("/callandNotSay")
    public String callandNotSay() {

        return "Hello Spring Security Study, I am Lixiang !";
    }

    @PreAuthorize(value = "hasAuthority('admin:read')")
    @Operation(summary = "用户读取文件")
    @RequestMapping("/readFile1")
    public String readFile1() {
        return "Hello Spring Security Study, I am Lixiang !";
    }

    @PreAuthorize(value = "hasAuthority('user:read')")
    @Operation(summary = "用户读取文件")
    @RequestMapping("/readFile2")
    public String readFile2() {
        return "Hello Spring Security Study, I am Lixiang !";
    }

    @PreAuthorize(value = "hasAuthority('guest:read')")
    @Operation(summary = "用户读取文件")
    @RequestMapping("/readFile3")
    public String readFile3() {
        return "Hello Spring Security Study, I am Lixiang !";
    }

    @PreAuthorize(value = "hasAuthority('manager:read')")
    @Operation(summary = "用户读取文件")
    @RequestMapping("/readFile4")
    public String readFile4() {
        return "Hello Spring Security Study, I am Lixiang !";
    }
}
