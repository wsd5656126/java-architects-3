package com.wusd.security;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private HttpServletRequest request;

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    public String getParameter(String name) {
        String value = request.getParameter(name);
        if (StringUtils.isNotEmpty(value))
            value = StringEscapeUtils.escapeHtml(value);
        return value;
    }
}
