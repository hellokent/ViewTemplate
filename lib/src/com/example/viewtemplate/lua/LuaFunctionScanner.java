package com.example.viewtemplate.lua;

import android.text.TextUtils;
import com.example.viewtemplate.Utils;
import com.example.viewtemplate.classscanner.Scanned;
import com.example.viewtemplate.classscanner.ScannerListener;
import org.keplerproject.luajava.JavaFunction;

/**
 * Lua函数扫描类，在scanner里面注册好了的^_^
 * Created by chenyang.coder@gmail.com on 13-11-19 下午8:38.
 */
@Scanned
public class LuaFunctionScanner extends ScannerListener {

    @Override
    public void onScan(final Class<?> clazz) {
        if (!Utils.Reflect.isSubclassOf(clazz, JavaFunction.class)){
            return;
        }

        if (Utils.Reflect.isSubclassOf(clazz, BaseFunction.class)){
            try {
                final BaseFunction baseFunction = (BaseFunction) Utils.Reflect.newInstance(clazz, Utils.getApp());
                final String name = baseFunction.getFunctionName();
                if (TextUtils.isEmpty(name)){
                    return;
                }
                baseFunction.register(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            final LuaFunctionName nameAnnotation = clazz.getAnnotation(LuaFunctionName.class);

            if (nameAnnotation == null){
                return;
            }

            try {
                JavaFunction function = Utils.Reflect.newInstance((Class<JavaFunction>)clazz, LuaUtils.L);
                function.register(nameAnnotation.value());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
