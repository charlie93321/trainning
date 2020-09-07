package com.dxm.test.interceptor;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ChannelFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;

        if (servletRequest instanceof HttpServletRequest && ((HttpServletRequest) servletRequest).getMethod().equalsIgnoreCase("post")) {
            String header = ((HttpServletRequest) servletRequest).getHeader("Content-Type");
            if (header != null && !header.toLowerCase().contains("application/x-www-form-urlencoded")) {
                requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
            }
        }
        if (requestWrapper == null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
