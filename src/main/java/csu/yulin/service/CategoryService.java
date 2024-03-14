package csu.yulin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import csu.yulin.entity.Category;


public interface CategoryService extends IService<Category> {
    void remove(Long id);
}

