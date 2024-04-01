package com.zoi.filter;

import com.zoi.entity.RestBean;
import com.zoi.utils.Const;
import com.zoi.utils.FlowLimitUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Order(Const.ORDER_LIMIT)
public class FlowLimitFilter extends HttpFilter {

    @Value("${spring.web.flow.limit}")
    int limit;
    //计数时间周期
    @Value("${spring.web.flow.period}")
    int period;
    //超出请求限制封禁时间
    @Value("${spring.web.flow.block}")
    int block;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    FlowLimitUtils utils;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String address = request.getRemoteAddr();
        if (!tryCount(address)) {
            this.writeBlockMessage(response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void writeBlockMessage(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(RestBean.forbidden("操作频繁，请稍后再试").asJsonString());
    }

    private boolean tryCount(String ip) {
        synchronized (ip.intern()) {
            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(Const.FLOW_LIMIT_BLOCK + ip))) return false;
            String counterKey = Const.FLOW_LIMIT_COUNTER + ip;
            String blockKey = Const.FLOW_LIMIT_BLOCK + ip;
            return utils.limitPeriodCheck(counterKey, blockKey, block, limit, period);
        }
    }
}
