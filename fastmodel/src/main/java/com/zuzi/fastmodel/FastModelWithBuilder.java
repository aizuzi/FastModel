package com.zuzi.fastmodel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  @author liyi
 *  create at 2018/5/28
 **/
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface FastModelWithBuilder {
}
