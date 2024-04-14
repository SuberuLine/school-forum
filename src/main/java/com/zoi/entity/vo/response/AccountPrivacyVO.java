package com.zoi.entity.vo.response;

import lombok.Data;

@Data
public class AccountPrivacyVO {
    boolean phone;
    boolean email;
    boolean qq;
    boolean wx;
    boolean gender;
}
