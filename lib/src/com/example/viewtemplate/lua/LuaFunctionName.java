package com.example.viewtemplate.lua;

import java.lang.annotation.*;

/**
 * Created by chenyang.coder@gmail.com on 13-11-21 下午9:38.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LuaFunctionName {
    String value();
}
