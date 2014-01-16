package com.example.viewtemplate.xml;

import java.lang.annotation.*;

/**
 * 描述一个节点的Tag
 * Created by chenyang.coder@gmail.com on 14-1-10 下午2:23.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XmlTags {
    String[] value();
}
