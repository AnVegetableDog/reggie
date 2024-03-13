package csu.yulin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import csu.yulin.entity.User;

public interface UserService extends IService<User> {

    User loginOrRegister(String phone);
}
