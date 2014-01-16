package com.example.viewtemplate.lua;

import android.app.Application;
import android.content.Context;
import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chenyang.coder@gmail.com on 14-1-16 下午4:35.
 */
public class BaseFunction extends JavaFunction {

    Context mContext;
    Method mMethod;
    Class<?>[] mParameterTypes;
    int mTypeCount;

    static final Object[] EMPTY_ARRAY = {};

    private BaseFunction(final LuaState L) {
        super(L);
    }

    public BaseFunction(final Application application){
        this(LuaUtils.L);
        mContext = application;
        for (Method method : getClass().getDeclaredMethods()){
            if (method.getAnnotation(Lua.class) != null){
                mMethod = method;
                mParameterTypes = method.getParameterTypes();
                mTypeCount = mParameterTypes.length;
                mMethod.setAccessible(true);
                break;
            }
        }
    }

    public final String getFunctionName(){
        return mMethod == null ? null : mMethod.getName();
    }


    @Override
    public final int execute() throws LuaException {
        if (mMethod == null){
            return 0;
        }
        Object[] parameter;
        if (mParameterTypes == null || mParameterTypes.length == 0) {
            parameter = EMPTY_ARRAY;
        } else {
            parameter = new Object[mParameterTypes.length];
            for (int i = 0; i < mParameterTypes.length; ++i) {
                final Class paraClz = mParameterTypes[i];
                LuaObject luaObject = getParam(0 - mTypeCount + i);
                if (paraClz == String.class){
                    parameter[i] = luaObject.getString();
                } else if (paraClz == int.class || paraClz == Integer.class){
                    parameter[i] = (int) luaObject.getNumber();
                } else if (paraClz == double.class || paraClz == Double.class){
                    parameter[i] = luaObject.getNumber();
                } else if (paraClz == boolean.class || paraClz == Boolean.class){
                    parameter[i] = luaObject.getBoolean();
                }
            }
        }

        try {
            Object obj = mMethod.invoke(this, parameter);
            if (mMethod.getReturnType() != Void.class){
                L.pushObjectValue(obj);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return mMethod != null && mMethod.getReturnType() != Void.class ? 1 : 0;
    }
}
