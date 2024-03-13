package csu.yulin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import csu.yulin.dto.DishDto;
import csu.yulin.entity.Dish;

public interface DishService extends IService<Dish> {

    /**
     * 新增菜品, 同时添加该菜品的口味信息
     *
     * @param dishDto
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 根据菜品id查询菜品和口味信息
     *
     * @param id
     * @return
     */
    DishDto getDishWithFlavorByDishId(Long id);

    /**
     * 更新菜品信息及其口味信息
     *
     * @param dishDto
     */
    void updateWithFlavor(DishDto dishDto);
}

