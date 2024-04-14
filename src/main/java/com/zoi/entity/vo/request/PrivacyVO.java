package com.zoi.entity.vo.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PrivacyVO {

    @Pattern(regexp = ("phone|email|qq|wx|gender"))
    String type;
    boolean status;


}
