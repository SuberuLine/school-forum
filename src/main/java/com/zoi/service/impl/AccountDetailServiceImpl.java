package com.zoi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoi.entity.dto.Account;
import com.zoi.entity.dto.AccountDetails;
import com.zoi.entity.vo.request.DetailsSaveVO;
import com.zoi.mapper.AccountDetailsMapper;
import com.zoi.service.AccountDetailService;
import com.zoi.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailServiceImpl extends ServiceImpl<AccountDetailsMapper, AccountDetails> implements AccountDetailService {

    @Resource
    AccountService service;

    @Override
    public AccountDetails findAccountDetailsById(int id) {
        return this.getById(id);
    }

    @Override
    public synchronized boolean saveAccountDetails(int id, DetailsSaveVO vo) {
        Account account =  service.findAccountByNameOrEmail(vo.getUsername());
        if (account == null || account.getId() == id) {
            service.update()
                    .eq("id", id)
                    .set("username", vo.getUsername())
                    .update();
            this.saveOrUpdate(new AccountDetails(
                id, vo.getGender(), vo.getPhone(), vo.getQq(),
                    vo.getWx(), vo.getDesc()
            ));
            return true;
        }
        return false;
    }
}
