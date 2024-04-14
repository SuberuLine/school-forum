package com.zoi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoi.entity.dto.AccountPrivacy;
import com.zoi.entity.vo.request.PrivacyVO;

public interface AccountPrivacyService extends IService<AccountPrivacy> {
    void savePrivacy(int id, PrivacyVO vo);
    AccountPrivacy accountPrivacy(int id);
}
