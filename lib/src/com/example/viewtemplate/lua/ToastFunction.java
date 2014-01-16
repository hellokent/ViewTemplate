package com.example.viewtemplate.lua;

import android.widget.Toast;
import com.example.viewtemplate.Utils;
import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;

/**
 * Created by chenyang.coder@gmail.com on 13-11-17 上午3:12.
 */
@LuaFunctionName("toast")
public class ToastFunction extends JavaFunction{

    public ToastFunction(final LuaState L) {
        super(L);
    }

    @Override
    public int execute() throws LuaException {
        Toast.makeText(Utils.getApp(), getParam(-1).getString(), Toast.LENGTH_SHORT).show();
        return 0;
    }
}
