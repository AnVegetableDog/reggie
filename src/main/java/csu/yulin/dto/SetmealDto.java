package csu.yulin.dto;

import csu.yulin.entity.Setmeal;
import csu.yulin.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
