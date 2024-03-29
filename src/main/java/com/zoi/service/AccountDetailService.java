package com.zoi.service;

import com.zoi.entity.dto.AccountDetails;
import com.zoi.entity.vo.request.DetailsSaveVO;

public interface AccountDetailService {
    AccountDetails findAccountDetailsById(int id);
    boolean saveAccountDetails(int id, DetailsSaveVO vo);
}
