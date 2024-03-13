package csu.yulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import csu.yulin.dto.DishDto;
import csu.yulin.entity.Dish;
import csu.yulin.entity.DishFlavor;
import csu.yulin.mapper.DishMapper;
import csu.yulin.service.DishFlavorService;
import csu.yulin.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
        implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品, 同时添加该菜品的口味信息
     *
     * @param dishDto Dish数据传输对象
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品信息
        save(dishDto);

        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }

        //保存菜品的口味信息列表
        dishFlavorService.saveOrUpdateBatch(flavors);
    }

    /**
     * 根据菜品id查询菜品和口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getDishWithFlavorByDishId(Long id) {
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(getById(id), dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        dishDto.setFlavors(dishFlavorService.list(queryWrapper));

        return dishDto;
    }

    /**
     * 更新菜品信息及其口味信息
     *
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().peek((item) -> item.setDishId(dishDto.getId())).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}

