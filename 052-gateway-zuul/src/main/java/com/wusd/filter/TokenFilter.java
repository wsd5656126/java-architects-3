package com.wusd.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class TokenFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";//前置执行
    }

    @Override
    public int filterOrder() {
        return 0;//过滤器优先级 数字越大 越优先执行大
    }

    @Override
    public boolean shouldFilter() {
        return true;//开启当前filter
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(500);
            ctx.setResponseBody("token is null");
            return null;
        }
        //继续执行正常业务逻辑
        return null;
    }
}
