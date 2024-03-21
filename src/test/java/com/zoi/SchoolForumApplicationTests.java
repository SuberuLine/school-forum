package com.zoi;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SchoolForumApplicationTests {

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void contextLoads() {
        System.out.println(bCryptPasswordEncoder.matches("123456", "$2a$10$FVnhxXODi7K0GjBpjKEdPuLUpRswYmeW8XR0zbYT3vhVmKn20HIIK"));
    }

}
