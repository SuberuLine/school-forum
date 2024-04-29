package com.zoi.utils;

import com.zoi.entity.RestBean;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class ControllerUtils {
    public RestBean<Void> messageHandle(Supplier<String> action) {
        String message = action.get();
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }
}
