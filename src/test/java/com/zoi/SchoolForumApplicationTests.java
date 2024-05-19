package com.zoi;

import com.zoi.utils.JwtUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootTest
class SchoolForumApplicationTests {

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    JwtUtils jwtUtils;

    @Test
    void contextLoads() {
        System.out.println(bCryptPasswordEncoder.matches("123456", "$2a$10$FVnhxXODi7K0GjBpjKEdPuLUpRswYmeW8XR0zbYT3vhVmKn20HIIK"));
    }

    @Test
    public void resolveJwt() {
        System.out.println(jwtUtils.resolveJwt("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiU3ViZXJ1TGluZSIsImlkIjo0MTAxMjgyNiwiZXhwIjoxNzE2NjQ1MTAwLCJpYXQiOjE3MTYwNDAzMDAsImp0aSI6IjYxODQzY2MzLWE4NjUtNGU2ZS1hYzc4LWRmODY4MTA1MjY4MSIsImF1dGhvcml0aWVzIjpbIlJPTEVfdXNlciJdfQ.aGGogGOE88RWQbC752Fc--1Nqzkom5ecOsiFoICBsTU").getClaims());
    }

}
