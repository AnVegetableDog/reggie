package csu.yulin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import csu.yulin.common.R;
import csu.yulin.dto.DishDto;
import csu.yulin.entity.Category;
import csu.yulin.entity.Dish;
import csu.yulin.entity.DishFlavor;
import csu.yulin.service.CategoryService;
import csu.yulin.service.DishFlavorService;
import csu.yulin.service.DishService;
import csu.yulin.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        //当新增一个菜品的时候,缓存中的菜品和套餐信息都应该被删除
        String key = "dish_" + dishDto.getCategoryId();
        redisCache.deleteObject(key);

        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }


    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<DishDto>> pageSearch(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);

        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> dishes = pageInfo.getRecords();

        //方案1
        List<DishDto> dishDtoList = dishes.stream().map((item) -> {
            Category category = categoryService.getById(item.getCategoryId());
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());

        //方案2
        for (Dish dish : dishes) {
            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            dishDto.setCategoryName(category.getName());
            dishDtoList.add(dishDto);
        }

        dishDtoPage.setRecords(dishDtoList);

        return R.success(dishDtoPage);
    }


    /**
     * 根据菜品id返回菜品信息和其对应的口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getByDishId(@PathVariable Long id) {
        return R.success(dishService.getDishWithFlavorByDishId(id));
    }

    /**
     * 更新菜品信息及其口味信息
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        //当一个菜品信息被更新的时候,缓存中的菜品和套餐信息都应该被删除
        String key = "dish_" + dishDto.getCategoryId();
        redisCache.deleteObject(key);

        dishService.updateWithFlavor(dishDto);
        return R.success("更新菜品成功");
    }

    /**
     * 根据分类id返回菜品列表
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        String key = "dish_" + dish.getCategoryId();
        //先从缓存中获取数据
        List<DishDto> dishDtoList = redisCache.getCacheList(key);
        if (dishDtoList != null && !dishDtoList.isEmpty()) {
            return R.success(dishDtoList);
        }

        LambdaQueryWrapper<Dish> queryWrapper = Wrappers.lambdaQuery(Dish.class)
                .eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
                .eq(Dish::getStatus, 1)
                .like(dish.getName() != null, Dish::getName, dish.getName())
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);

        List<Dish> dishList = dishService.list(queryWrapper);

        Map<Long, String> categoryNameMap = new HashMap<>();
        for (Dish item : dishList) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            // 获取分类名称
            Long categoryId = item.getCategoryId();
            String categoryName = categoryNameMap.computeIfAbsent(categoryId, id -> {
                Category category = categoryService.getById(id);
                return category != null ? category.getName() : "";
            });
            dishDto.setCategoryName(categoryName);

            // 获取口味列表
            List<DishFlavor> flavors = dishFlavorService.list(Wrappers.lambdaQuery(DishFlavor.class)
                    .eq(DishFlavor::getDishId, item.getId()));
            dishDto.setFlavors(flavors);

            dishDtoList.add(dishDto);
        }
        redisCache.setCacheList(key, dishDtoList);
        redisCache.expire(key, 60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }
}
