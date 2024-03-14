package csu.yulin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import csu.yulin.common.R;
import csu.yulin.config.BaseContext;
import csu.yulin.entity.ShoppingCart;
import csu.yulin.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加菜品或者套餐到购物车里
     *
     * @param cart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart cart) {
        Long userId = BaseContext.getVal();
        cart.setUserId(userId);

        Long dishId = cart.getDishId();
        String dishFlavor = cart.getDishFlavor();
        Long setmealId = cart.getSetmealId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = Wrappers.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, userId)
                .eq(dishId != null, ShoppingCart::getDishId, dishId)
                .eq(dishFlavor != null, ShoppingCart::getDishFlavor, dishFlavor)
                .eq(setmealId != null, ShoppingCart::getSetmealId, setmealId);

        ShoppingCart existingCart = shoppingCartService.getOne(queryWrapper);
        if (existingCart == null) {
            //执行插入操作
            cart.setNumber(1);
            shoppingCartService.save(cart);
        } else {
            //执行更新操作
            existingCart.setNumber(existingCart.getNumber() + 1);
            shoppingCartService.updateById(existingCart);
            cart = existingCart; // 将返回的购物车项设为已存在的购物车项
        }

        return R.success(cart);
    }

    /**
     * 返回用户购物车信息
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        Long userId = BaseContext.getVal();

        List<ShoppingCart> shoppingCarts = shoppingCartService.list(Wrappers.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, userId)
                .orderByAsc(ShoppingCart::getCreateTime));
        return R.success(shoppingCarts);
    }

    @DeleteMapping("clean")
    public R<String> clean() {
        Long userId = BaseContext.getVal();
        shoppingCartService.remove(Wrappers.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, userId));
        return R.success("删除购物车信息成功");
    }
}
