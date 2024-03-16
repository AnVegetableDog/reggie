package csu.yulin.filter;

import com.alibaba.fastjson.JSON;
import csu.yulin.common.R;
import csu.yulin.config.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 检查用户是否完成登录
 */
@Slf4j
@WebFilter(filterName = "loginCheck", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final String[] passURLs = {
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
            "/user/sendMsg",
            "/user/login",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources",
            "/v2/api-docs"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("本次请求的路径是:{}", request.getRequestURI());

        Object employee = request.getSession().getAttribute("employee");
        Object user = request.getSession().getAttribute("user");
        //未登录
        if (Objects.isNull(employee) && Objects.isNull(user) && !check(request.getRequestURI())) {
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;
        }
        BaseContext.setVal((employee != null) ? (Long) employee : (Long) user);
        filterChain.doFilter(request, response);
    }


    /**
     * 检查此次请求的路径是否在允许的URL里面
     *
     * @param requestURI
     * @return
     */
    public boolean check(String requestURI) {
        for (String passURL : passURLs) {
            if (PATH_MATCHER.match(passURL, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
