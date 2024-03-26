package com.zoi.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoi.entity.dto.Account;
import com.zoi.entity.vo.request.EmailRegisterVO;
import com.zoi.mapper.AccountMapper;
import com.zoi.service.AccountService;
import com.zoi.utils.Const;
import com.zoi.utils.FlowLimitUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    FlowLimitUtils flowLimitUtils;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.findAccountByNameOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    public Account findAccountByNameOrEmail(String text) {
        return this.query()
                .eq("username", text).or()
                .eq("email", text)
                .one();
    }

    /**
     * 生成验证码的业务逻辑
     * @param type 发送邮件的类型
     * @param email 邮箱地址
     * @param ip 申请验证码的ip，用于限流
     * @return 成功不返回信息
     */
    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        // 加锁，防止同一请求多次调用
        synchronized (ip.intern()) {
            if (!this.verifyLimit(ip))
                return "请求频繁，请稍后再试";
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            Map<String, Object> data = Map.of("type", type, "email", email, "code", code);
            amqpTemplate.convertAndSend("mail", data);
            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        }
    }

    /**
     * 注册业务逻辑
     * @param vo 注册信息实体类
     * @return 成功无返回
     */
    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {
        String email = vo.getEmail();
        String username = vo.getUsername();
        String key = Const.VERIFY_EMAIL_DATA + email;
        String code = stringRedisTemplate.opsForValue().get(key);
        if (code == null) return "请先获取验证码";
        if (!code.equals(vo.getCode())) return "验证码输入错误，请重试";
        if (this.existAccountByEmail(email)) return "此电子邮件已被其他用户注册";
        if (this.existAccountByUsername(username)) return "此用户名已被占用";
        String password = passwordEncoder.encode(vo.getPassword());
        Account account = new Account(null, username, password, email, "user", new Date());
        if (this.save(account)) {
            stringRedisTemplate.delete(key);
            return null;
        } else {
            return "内部错误，请联系管理员";
        }
    }

    /**
     * 通过邮箱判断用户是否存在
     * @param email 查询邮箱
     * @return 是否存在该用户
     */
    private boolean existAccountByEmail(String email) {
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email", email));
    }

    /**
     * 通过用户名判断用户是否存在
     * @param username 用户名
     * @return 是否存在用户
     */
    private boolean existAccountByUsername(String username) {
        return this.baseMapper.exists(Wrappers.<Account>query().eq("username", username));
    }

    /**
     * 验证用户限制状态
     * @param ip 用户的ip地址
     * @return 是否在冷却期
     */
    private boolean verifyLimit(String ip) {
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowLimitUtils.limitOnceCheck(key, 60);
    }
}