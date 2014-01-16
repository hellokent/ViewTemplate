package com.example.viewtemplate.lua;

import android.app.Application;
import com.example.viewtemplate.classscanner.Scanned;

/**
 * Created by chenyang.coder@gmail.com on 14-1-16 下午5:06.
 */
@Scanned
public class NewToastFunc extends BaseFunction {

    public NewToastFunc(final Application context) {
        super(context);
    }

    @Lua
    public void log(String arg1, String arg2){
        com.example.viewtemplate.L.v("arg1:%s, arg2:%s", arg1, arg2);
    }

}
