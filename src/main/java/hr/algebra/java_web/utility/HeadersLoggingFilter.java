package hr.algebra.java_web.utility;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
@Slf4j
public class HeadersLoggingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpRequest=(HttpServletRequest)servletRequest;
        Collections.list(httpRequest.getHeaderNames())
                .forEach(header->{
        log.info("Header: {}={}",header,httpRequest.getHeader(header));
                });
        filterChain.doFilter(httpRequest,servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
