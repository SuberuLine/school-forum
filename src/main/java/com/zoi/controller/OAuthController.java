package com.zoi.controller;

import com.zoi.entity.dto.Account;
import com.zoi.service.AccountService;
import com.zoi.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @Value("${spring.web.front}")
    String frontend;

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    String clientSecret;

    @Value("${spring.security.oauth2.client.provider.github.user-info-uri}")
    String userInfoUri;

    @Value("${spring.security.oauth2.client.provider.github.token-uri}")
    String tokenUri;

    @Resource
    RestTemplate restTemplate;

    @Resource
    AccountService accountService;

    @Resource
    JwtUtils jwtUtils;

    @GetMapping("/redirect")
    public void GetGithubCode(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        // 使用 code 获取访问令牌
        String accessToken = getAccessToken(code);
        // 使用访问令牌获取用户信息
        Map<String, Object> userInfo = getUserInfo(accessToken);
        System.out.println(userInfo);

        Account account = accountService.oAuthLoginGithub(userInfo);
        UserDetails user = accountService.loadUserByUsername(account.getUsername());
        // 创建认证对象
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        // 将认证对象存储在 SecurityContext 中
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 创建 JWT 令牌
        String token = jwtUtils.createJwt(user, account.getId(), account.getUsername());
        // 将 JWT 令牌发送给前端
        response.sendRedirect(frontend + "/oauth/redirect?token=" + token + "&expire=" + jwtUtils.expireTime());
    }

    private String getAccessToken(String code) {
        String uri = tokenUri +
                "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&code=" + code;

        ResponseEntity<String> response = restTemplate.postForEntity(uri, null, String.class);
        return Objects.requireNonNull(response.getBody()).split("&")[0].split("=")[1];
    }

    private Map<String, Object> getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }
}
