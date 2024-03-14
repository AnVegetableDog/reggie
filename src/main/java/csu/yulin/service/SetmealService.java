package csu.yulin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import csu.yulin.dto.SetmealDto;
import csu.yulin.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐
     *
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐
     *
     * @param ids
     */
    void removeWithDish(List<Long> ids);
}