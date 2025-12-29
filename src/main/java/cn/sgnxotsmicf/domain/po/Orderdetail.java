package cn.sgnxotsmicf.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author sgnxotsmicf
 * @since 2025-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_orderdetail")
public class Orderdetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单号
     */
    private Integer orderId;

    /**
     * 商品id
     */
    private Integer itemId;

    /**
     * 商品总价
     */
    private Double totalPrice;

    /**
     * 状态
     */
    private Integer status;


}
