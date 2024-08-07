package com.zoi.controller;

import com.zoi.entity.RestBean;
import com.zoi.entity.dto.Account;
import com.zoi.entity.dto.AccountDetails;
import com.zoi.entity.vo.request.ChangePasswordVO;
import com.zoi.entity.vo.request.DetailsSaveVO;
import com.zoi.entity.vo.request.ModifyEmailVO;
import com.zoi.entity.vo.request.PrivacyVO;
import com.zoi.entity.vo.response.AccountDetailsVO;
import com.zoi.entity.vo.response.AccountPrivacyVO;
import com.zoi.entity.vo.response.AccountVO;
import com.zoi.service.AccountDetailService;
import com.zoi.service.AccountPrivacyService;
import com.zoi.service.AccountService;
import com.zoi.utils.Const;
import com.zoi.utils.ControllerUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    @Resource
    AccountService accountService;

    @Resource
    AccountDetailService detailService;

    @Resource
    AccountPrivacyService privacyService;

    @Resource
    ControllerUtils controllerUtils;

    @GetMapping("/info")
    public RestBean<AccountVO> info(@RequestAttribute(Const.ATTR_USER_ID) int id) {
        Account account = accountService.findAccountById(id);
        return RestBean.success(account.asViewObject(AccountVO.class));
    }

    @GetMapping("/details")
    public RestBean<AccountDetailsVO> details(@RequestAttribute(Const.ATTR_USER_ID) int id) {
        AccountDetails details = Optional
                .ofNullable(detailService.findAccountDetailsById(id))
                .orElseGet(AccountDetails::new);
        return RestBean.success(details.asViewObject(AccountDetailsVO.class));
    }

    @PostMapping("/save-details")
    public RestBean<Void> saveDetails(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                      @RequestBody @Valid DetailsSaveVO vo) {
        boolean success = detailService.saveAccountDetails(id, vo);
        return success ? RestBean.success() : RestBean.failure(400, "此用户名被占用");
    }

    @PostMapping("/change-password")
    public RestBean<Void> changePassword(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                         @RequestBody @Valid ChangePasswordVO vo) {
        return controllerUtils.messageHandle(() -> accountService.changePassword(id, vo));
    }

    @PostMapping("/modify-email")
    public RestBean<Void> modifyEmail(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                      @RequestBody @Valid ModifyEmailVO vo) {
        return controllerUtils.messageHandle(() -> accountService.modifyEmail(id, vo));
    }

    @PostMapping("/save-privacy")
    public RestBean<Void> savePrivacy(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                      @RequestBody @Valid PrivacyVO vo){
        privacyService.savePrivacy(id, vo);
        return RestBean.success();
    }

    @GetMapping("/privacy")
    public RestBean<AccountPrivacyVO> privacy(@RequestAttribute(Const.ATTR_USER_ID) int id) {
        return RestBean.success(privacyService.accountPrivacy(id).asViewObject(AccountPrivacyVO.class));
    }
}
