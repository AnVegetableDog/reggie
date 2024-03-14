package csu.yulin.controller;

import csu.yulin.common.R;
import csu.yulin.entity.User;
import csu.yulin.service.UserService;
import csu.yulin.utils.RedisCache;
import csu.yulin.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisCache redisCache;

    @PostMapping("/sendMsg")
    public R<String> sendMeg(@RequestBody User user) {

        String phone = user.getPhone();
        String code = ValidateCodeUtils.generateValidateCode(6).toString();
        log.info("验证码是: {}", code);

        redisCache.setCacheObject(phone, "123456", 5, TimeUnit.MINUTES);

//        session.setAttribute(phone, code);
//        session.setAttribute(phone, "123456");
//        try {
//            new AliyunSmsUtil().sendCode(phone, code);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return R.success("验证码已发送");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session) {
        String phone = map.get("phone");
        String code = map.get("code");

//        Object trueCode = session.getAttribute(phone);

        String trueCode = redisCache.getCacheObject(phone);

        if (trueCode != null && trueCode.equals(code)) {
            User user = userService.loginOrRegister(phone);
            session.setAttribute("user", user.getId());

            redisCache.deleteObject(phone);

            return R.success(user);
        }
        return R.error("验证码已过期或者错误!");
    }
}
