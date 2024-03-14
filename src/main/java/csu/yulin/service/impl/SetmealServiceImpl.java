package csu.yulin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import csu.yulin.config.CustomException;
import csu.yulin.dto.SetmealDto;
import csu.yulin.entity.Setmeal;
import csu.yulin.entity.SetmealDish;
import csu.yulin.mapper.SetmealMapper;
import csu.yulin.service.SetmealDishService;
import csu.yulin.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().peek((item) -> {
            item.setSetmealId(setmealDto.getId());
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐
     *
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        List<Setmeal> sellingSetmeals = list(Wrappers.lambdaQuery(Setmeal.class)
                .in(Setmeal::getId, ids)
                .eq(Setmeal::getStatus, 1));
        if (!sellingSetmeals.isEmpty()) {
            throw new CustomException("当前套餐正在售卖中,无法删除");
        }

        //执行删除操作
        removeByIds(ids);
        setmealDishService.remove(Wrappers.lambdaQuery(SetmealDish.class)
                .in(SetmealDish::getSetmealId, (Object) ids));

    }
}