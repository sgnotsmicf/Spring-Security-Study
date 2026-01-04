package cn.sgnxotsmicf.mapper;

import cn.sgnxotsmicf.domain.po.Permission;
import cn.sgnxotsmicf.domain.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sgnxotsmicf
 * @since 2025-12-28
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
