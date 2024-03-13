package csu.yulin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import csu.yulin.entity.ShoppingCart;
import csu.yulin.mapper.ShoppingCartMapper;
import csu.yulin.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {

}




