package csu.yulin.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
//    @Autowired
//    private HttpServletRequest request;

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");

        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "createUser", Long.class, BaseContext.getVal()); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "updateUser", Long.class, BaseContext.getVal()); // 起始版本 3.3.3(推荐)
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");

        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
        this.strictUpdateFill(metaObject, "updateUser", Long.class, BaseContext.getVal()); // 起始版本 3.3.3(推荐)
    }
}