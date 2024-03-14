package csu.yulin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import csu.yulin.entity.OrderDetail;
import csu.yulin.mapper.OrderDetailMapper;
import csu.yulin.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
        implements OrderDetailService {

}