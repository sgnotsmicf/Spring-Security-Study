package cn.sgnxotsmicf.service.impl;

import cn.sgnxotsmicf.domain.po.Item;
import cn.sgnxotsmicf.mapper.ItemMapper;
import cn.sgnxotsmicf.service.IItemService;
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
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {

}
