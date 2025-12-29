package cn.sgnxotsmicf.service.impl;

import cn.sgnxotsmicf.domain.po.User;
import cn.sgnxotsmicf.mapper.UserMapper;
import cn.sgnxotsmicf.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        System.out.println(user);
        String encryptedPassword = user.getPassword();
        if (!user.getPassword().startsWith("$")) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            encryptedPassword = encoder.encode(user.getPassword());
        }
        return new org.springframework.security.core.userdetails
                .User(user.getUserName(), encryptedPassword, AuthorityUtils.NO_AUTHORITIES);
    }
}
