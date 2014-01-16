package com.example.viewtemplate.view;

import com.example.viewtemplate.xml.nodes.BaseViewNode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识View处理对象要处理的Class
 * Created by chenyang.coder@gmail.com on 13-10-31 上午2:26.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewNodeClass {
    Class<? extends BaseViewNode> value();
}
