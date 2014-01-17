package com.example.viewtemplate.lua;

import android.app.Application;
import com.example.viewtemplate.classscanner.Scanned;

@Scanned
public class LogFunc extends BaseLuaFunction {

    public LogFunc(final Application context) {
        super(context);
    }

    @Lua
    public void log(String arg1, String arg2){
        com.example.viewtemplate.L.v("arg1:%s, arg2:%s", arg1, arg2);
    }

}
