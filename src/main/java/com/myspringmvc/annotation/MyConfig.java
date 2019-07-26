package com.myspringmvc.annotation;

import java.lang.annotation.*;

@Target( ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyConfig {
    String value() default "";

}
