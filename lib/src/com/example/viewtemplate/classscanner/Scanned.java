package com.example.viewtemplate.classscanner;

import java.lang.annotation.*;

/**
 * Scanner的子类可以加上这个注解，用来避免IDE提示类没有调用的异常
 * Created by chenyang.coder@gmail.com on 13-11-20 上午12:12.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Scanned {}
