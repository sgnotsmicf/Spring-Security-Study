package cn.sgnxotsmicf.service.impl;

import cn.sgnxotsmicf.domain.po.Order;
import cn.sgnxotsmicf.mapper.OrderMapper;
import cn.sgnxotsmicf.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
