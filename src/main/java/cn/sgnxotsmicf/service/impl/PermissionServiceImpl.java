package cn.sgnxotsmicf.service.impl;

import cn.sgnxotsmicf.domain.po.Permission;
import cn.sgnxotsmicf.mapper.PermissionMapper;
import cn.sgnxotsmicf.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author sgnxotsmicf
 * @since 2026-01-04
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
