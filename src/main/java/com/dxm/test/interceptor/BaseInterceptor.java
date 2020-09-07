package com.dxm.test.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;

public class BaseInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = Logger.getLogger(BaseInterceptor.class);

    /**
     * This implementation always returns {@code true}.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {


        String method = request.getMethod();
        LOGGER.info(this.getClass().getCanonicalName());
        LOGGER.info("-----------------------开始计时:" + new SimpleDateFormat("HH:mm:ss.sss").format(new Date()) + "-------------------------------------");
        LOGGER.info("Controller: " + ((HandlerMethod) handler).getBean().getClass().getCanonicalName());
        LOGGER.info("Method    : " + ((HandlerMethod) handler).getMethod().getName());

        if (StringUtils.equalsIgnoreCase(method, "GET")) {
            LOGGER.info("Params    :    " + request.getQueryString());
        } else {
            String header = (request).getHeader("Content-Type");
            if (header != null && !header.toLowerCase().contains("application/x-www-form-urlencoded")) {
                RequestWrapper requestWrapper = (RequestWrapper) request;
                String body = requestWrapper.getBody();
                LOGGER.info("Params    :    " + body);
            } else {
                Enumeration<String> ite = request.getParameterNames();
                StringBuilder builder = new StringBuilder();
                while (ite.hasMoreElements()) {
                    String key = ite.nextElement();
                    String[] value = request.getParameterValues(key);
                    builder.append("\"" + key + ":\"").append("\"" + Arrays.toString(value) + ":\"");
                }
                LOGGER.info("Params    :    " + builder.toString());
            }
        }
        LOGGER.info("URI       : " + request.getRequestURI());


        return true;
    }

    /**
     * This implementation is empty.
     * <p>
     * 2018-12-10 16:51:03,078 INFO  com.meiyunji.training.intercepter.BaseInterceptor
     * (BaseInterceptor.java/:84) - -----------------------开始计时:04:51:03.078-------------------------------------
     * Controller: com.meiyunji.training.controller.IndexController
     * Method    : index
     * Params    :    request.getQueryString()
     * URI       : /   request.getRequestURI()
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {


    }

    /**
     * This implementation is empty.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response,
                                               Object handler) throws Exception {
    }


}
