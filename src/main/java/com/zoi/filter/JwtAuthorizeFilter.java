package com.zoi.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zoi.utils.Const;
import com.zoi.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 继承OncePerRequestFilter表示每次请求过滤一次，用于快速编写JWT校验规则
 */
@Component
public class JwtAuthorizeFilter extends OncePerRequestFilter {

    @Resource
    JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 首先从Header中取出JWT
        String authorization = request.getHeader("Authorization");
        DecodedJWT jwt = jwtUtils.resolveJwt(authorization);
        // 判断是否包含JWT且格式正确
        if (jwt != null) {
            // 开始解析成UserDetails对象，如果得到的是null说明解析失败，JWT有问题
            UserDetails user = jwtUtils.toUser(jwt);
            // 验证没有问题，那么就可以开始创建Authentication了，这里我们跟默认情况保持一致
            // 使用UsernamePasswordAuthenticationToken作为实体，填写相关用户信息进去
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 然后直接把配置好的Authentication塞给SecurityContext表示已经完成验证
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            request.setAttribute(Const.ATTR_USER_ID, jwtUtils.toId(jwt));
        }
        // 最后放行，继续下一个过滤器
        filterChain.doFilter(request, response);
    }
}
