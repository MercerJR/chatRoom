package com.train.chat.configuration.filter;

import com.train.chat.data.HttpInfo;
import com.train.chat.pojo.User;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author MercerJR
 * @Data 2021/4/23 9:46
 */
@Slf4j
@WebFilter(filterName = "sessionFilter",urlPatterns = {"/*"})
public class SessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("sessionFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();

        if (uri.contains(HttpInfo.INDEX_URL)){
            User user = (User) session.getAttribute(HttpInfo.USER_SESSION);
            if(user != null){
                filterChain.doFilter(request, response);
            }else{
                response.sendRedirect(request.getContextPath() + "/");
            }
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        log.info("sessionFilter destroy");
    }
}
