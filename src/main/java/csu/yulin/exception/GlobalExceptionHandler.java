package csu.yulin.exception;

import csu.yulin.common.R;
import csu.yulin.config.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * SQL完整性约束冲突
     *
     * @param e
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e) {
        logger.info(e.getMessage());
        return R.error(e.getMessage());
    }

    /**
     * /**
     * 业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public R<String> exceptionHandler(CustomException e) {
        logger.info(e.getMessage());
        return R.error(e.getMessage());
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public R<String> exceptionHandler(IllegalArgumentException e) {
        logger.info(e.getMessage());
        return R.error(e.getMessage());
    }
}
