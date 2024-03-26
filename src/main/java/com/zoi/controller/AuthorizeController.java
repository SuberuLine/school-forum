package com.zoi.controller;

import com.zoi.entity.RestBean;
import com.zoi.entity.vo.request.EmailRegisterVO;
import com.zoi.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    @Resource
    AccountService service;

    @GetMapping("/request-code")
    public RestBean<Void> requestVerifyCode(@RequestParam @Email String email,
                                            @RequestParam @Pattern(regexp = "register|reset") String type,
                                            HttpServletRequest request){
        return this.messageHandle(() -> service.registerEmailVerifyCode(type, email, request.getRemoteAddr()));
    }

    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody @Valid EmailRegisterVO vo) {
        return this.messageHandle(() -> service.registerEmailAccount(vo));
    }

    private RestBean<Void> messageHandle(Supplier<String> action) {
        String message = action.get();
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

}
