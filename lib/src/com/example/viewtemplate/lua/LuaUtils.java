package com.example.viewtemplate.lua;


import android.text.TextUtils;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

/**
 * Lua的工具类
 * Created by chenyang.coder@gmail.com on 13-11-14 下午11:30.
 */
public final class LuaUtils {
    private LuaUtils(){}

    public static final LuaState L = LuaStateFactory.newLuaState();

    static {
        L.openLibs();
        try {
            new ToastFunction(L).register("toast");
        } catch (LuaException e) {
            e.printStackTrace();
        }
    }

    public static int callFunc(final String funcName) throws LuaException {
        if (TextUtils.isEmpty(funcName)){
            return -1;
        }
        L.setTop(0);
        int ok = L.LloadString(funcName);
        if (ok == 0) {
            L.getGlobal("debug");
            L.getField(-1, "traceback");
            L.remove(-2);
            L.insert(-2);
            ok = L.pcall(0, 0, -2);
            if (ok == 0) {
                return ok;
            }
        }
        throw new LuaException(errorReason(ok) + ": " + L.toString(-1));
    }

    private static String errorReason(int error) {
        switch (error) {
            case 4:
                return "Out of memory";
            case 3:
                return "Syntax error";
            case 2:
                return "Runtime error";
            case 1:
                return "Yield error";
        }
        return "Unknown error " + error;
    }

}
