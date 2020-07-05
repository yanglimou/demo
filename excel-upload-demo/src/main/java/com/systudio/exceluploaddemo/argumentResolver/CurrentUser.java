package com.systudio.exceluploaddemo.argumentResolver;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface CurrentUser {
}
