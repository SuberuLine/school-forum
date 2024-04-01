package com.zoi.controller;

import com.zoi.entity.RestBean;
import com.zoi.entity.dto.Account;
import com.zoi.entity.dto.AccountDetails;
import com.zoi.entity.vo.request.DetailsSaveVO;
import com.zoi.entity.vo.request.ModifyEmailVO;
import com.zoi.entity.vo.response.AccountDetailsVO;
import com.zoi.entity.vo.response.AccountVO;
import com.zoi.service.AccountDetailService;
import com.zoi.service.AccountService;
import com.zoi.utils.Const;
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

    @PostMapping("/modify-email")
    public RestBean<Void> modifyEmail(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                      @RequestBody @Valid ModifyEmailVO vo) {
        String result = accountService.modifyEmail(id, vo);
        return result == null ? RestBean.success() : RestBean.failure(400, result);
    }

}
