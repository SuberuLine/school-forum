package com.zoi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoi.entity.dto.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {

    /**
     * 通过用户名或邮箱来登录
     * @param text 邮箱或用户名
     * @return 用户对象
     */
    Account findAccountByNameOrEmail(String text);

    /**
     * 邮箱验证码
     * @param type 发送邮件的类型
     * @param email 邮箱地址
     * @param ip 申请验证码的ip，用于限流
     * @return
     */
    String registerEmailVerifyCode(String type, String email, String ip);

}
