package csu.yulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import csu.yulin.config.CustomException;
import csu.yulin.entity.Category;
import csu.yulin.entity.Dish;
import csu.yulin.entity.Setmeal;
import csu.yulin.mapper.CategoryMapper;
import csu.yulin.service.CategoryService;
import csu.yulin.service.DishService;
import csu.yulin.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * @param id
     */
    @Override
    public void remove(Long id) {
        //菜品
        LambdaQueryWrapper<Dish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(queryWrapper1);
        if (count1 > 0) {
            throw new CustomException("当前分类关联了菜品,不能删除");
        }

        //套餐
        LambdaQueryWrapper<Setmeal> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(queryWrapper2);
        if (count2 > 0) {
            throw new CustomException("当前分类关联了套餐,不能删除");
        }

        //正常删除分类
        removeById(id);
    }


}

