package csu.yulin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import csu.yulin.entity.Orders;

public interface OrdersService extends IService<Orders> {
    /**
     * 用户下单
     *
     * @param orders
     */
    void submit(Orders orders);
}
