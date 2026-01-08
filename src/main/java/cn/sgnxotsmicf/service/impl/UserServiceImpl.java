package cn.sgnxotsmicf.service.impl;

import cn.sgnxotsmicf.domain.po.Permission;
import cn.sgnxotsmicf.domain.po.Role;
import cn.sgnxotsmicf.domain.po.User;
import cn.sgnxotsmicf.domain.po.UserRole;
import cn.sgnxotsmicf.mapper.PermissionMapper;
import cn.sgnxotsmicf.mapper.RoleMapper;
import cn.sgnxotsmicf.mapper.UserMapper;
import cn.sgnxotsmicf.mapper.UserRoleMapper;
import cn.sgnxotsmicf.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sgnxotsmicf
 * @since 2025-12-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 实现UserDetailsService的loadUserByUsername方法，自己封装用户密码，
     * 并返回UserDetails对象，用于Spring Security认证。
     * <p>
     * 1. 从数据库查询用户信息。
     * 2. 如果用户不存在，抛出UsernameNotFoundException。
     * 3. 如果密码不是BCrypt加密的，使用BCryptPasswordEncoder加密。
     * 4. 返回UserDetails对象，包含用户名、密码和权限（无权限）。
     * </p>
     * @param username 用户名
     * @return 用户信息
     * @throws UsernameNotFoundException 如果用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("登录账户不存在");
        }
        //查询该用户的角色列表---角色管理控制
//        List<UserRole> roles = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getId()));
//        List<Role> roleList = roles.stream().map(userRole -> roleMapper.selectById(userRole.getRoleId())).toList();
//        for (Role role : roleList) {
//            System.out.println(user.getUsername() + " has role: " + role.getRole());
//        }
//        user.setRoleList(roleList);
        //权限管理控制
        List<Permission> permissionList = permissionMapper.selectPermissionByUserId(user.getId());
        for (Permission permission : permissionList) {
            System.out.println(user.getUsername() + " has permission: " + permission.getCode());
        }
        user.setPermissionList(permissionList);
        System.out.println(user);
//        String encryptedPassword = user.getPassword();
//        if (!user.getPassword().startsWith("$")) {
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            encryptedPassword = encoder.encode(user.getPassword());
//        }
//        user.setPassword(encryptedPassword);
          // 返回UserDetails对象，包含用户名、密码和权限（无权限）
//        return org.springframework.security.core.userdetails
//                .User.builder()
//                .username(user.getUsername())
//                .password(encryptedPassword)
//                .authorities(AuthorityUtils.NO_AUTHORITIES)  // 无权限
//                .build();
        return user;
    }
}
