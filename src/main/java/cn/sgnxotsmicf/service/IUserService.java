package cn.sgnxotsmicf.service;

import cn.sgnxotsmicf.domain.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sgnxotsmicf
 * @since 2025-12-28
 */
public interface IUserService extends IService<User>, UserDetailsService {

}
