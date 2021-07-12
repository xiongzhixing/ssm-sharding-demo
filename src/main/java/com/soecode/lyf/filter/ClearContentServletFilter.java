package com.soecode.lyf.filter;

import com.soecode.lyf.global.RequestContentManager;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author: xzx
 * @Description:
 * @date: 2021/7/12
 */
@Slf4j
public class ClearContentServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("----ClearContentServletFilter过滤器初始化----");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("ClearContentServletFilter执行！！！");
        // 让目标资源执行，放行
        try{
            filterChain.doFilter(servletRequest, servletResponse);
        }finally {
            //通过过滤器再次移除上下文
            RequestContentManager.remove();
        }
    }

    @Override
    public void destroy() {
        log.debug("----ClearContentServletFilter----");
    }

}
