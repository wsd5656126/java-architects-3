package com.wusd.security;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ImgFilter implements Filter {
    @Value("$(domain.name)")
    private String domainName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String referer = req.getHeader("Referer");
        if (StringUtils.isEmpty(referer)) {
            request.getRequestDispatcher("/imgs/error.png").forward(request, response);
            return;
        }
        String domain = getDomain(referer);
        if (!domain.equals(domainName)) {
            request.getRequestDispatcher("/imgs/error.png").forward(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
    private String getDomain(String url) {
            String result = "";
            int j = 0, startIndex = 0, endIndex = 0;
            for (int i = 0; i < url.length(); i++) {
                if (url.charAt(i) == '/') {
                    j++;
                    if (j == 2)
                        startIndex = i;
                    else if (j == 3)
                        endIndex = i;
                }

            }
            result = url.substring(startIndex + 1, endIndex);
            return result;
        }

    @Override
    public void destroy() {

    }
}
