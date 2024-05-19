package com.zoi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoi.entity.dto.Account;
import com.zoi.entity.vo.request.ChangePasswordVO;
import com.zoi.entity.vo.request.ConfirmResetVO;
import com.zoi.entity.vo.request.EmailRegisterVO;
import com.zoi.entity.vo.request.EmailResetVO;
import com.zoi.entity.vo.request.ModifyEmailVO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

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

    /**
     * 注册账号
     * @param vo 注册信息实体类
     * @return
     */
    String registerEmailAccount(EmailRegisterVO vo);

    /**
     * 用户请求重置验证码
     * @param vo 用户重置密码信息实体
     * @return
     */
    String resetConfirm(ConfirmResetVO vo);

    /**
     * 用户确认重置密码
     * @param vo 用户确认重置密码实体
     * @return
     */
    String resetEmailAccountPassword(EmailResetVO vo);

    Account findAccountById(int id);

    String modifyEmail(int id, ModifyEmailVO vo);

    String changePassword(int id, ChangePasswordVO vo);

    Account oAuthLoginGithub(Map<String, Object> userInfo);

    Account findAccountByGithubId(int id);
}
