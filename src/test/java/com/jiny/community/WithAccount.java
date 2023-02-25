package com.jiny.community;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = AccountWithSecurityFactory.class)
public @interface WithAccount {
    String value();
}
