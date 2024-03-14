package csu.yulin.config;

/**
 * 基于ThreadLocal的封装工具类, 用来存储和获取当前登录用户id
 */
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static Long getVal() {
        return threadLocal.get();
    }

    public static void setVal(Long id) {
        threadLocal.set(id);
    }
}
