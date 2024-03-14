package csu.yulin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import csu.yulin.entity.User;
import csu.yulin.mapper.UserMapper;
import csu.yulin.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Override
    public User loginOrRegister(String phone) {
        User user = getOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getPhone, phone));
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            save(user);
        }

        return user;
    }
}


