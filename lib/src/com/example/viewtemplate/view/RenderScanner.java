package com.example.viewtemplate.view;

import android.text.TextUtils;
import com.example.viewtemplate.Utils;
import com.example.viewtemplate.classscanner.Scanned;
import com.example.viewtemplate.classscanner.ScannerListener;
import com.example.viewtemplate.view.template.BaseViewRender;
import com.example.viewtemplate.xml.Xml;

import java.util.HashMap;

/**
 * Created by chenyang.coder@gmail.com on 13-11-21 下午9:12.
 */
@Scanned
public class RenderScanner extends ScannerListener {

    public static final HashMap<String, Class<?>> NODE_NAME_MAP = new HashMap<String, Class<?>>();

    @Override
    public void onScan(final Class<?> clazz) {
        if (!Utils.Reflect.isSubclassOf(clazz, BaseViewRender.class)){
            return;
        }

        ViewNodeClass nodeClzAnnotation = clazz.getAnnotation(ViewNodeClass.class);

        if (nodeClzAnnotation == null){
            return;
        }

        final Class<?> nodeClz = nodeClzAnnotation.value();
        ViewFactory.NODE_COMP_MAP.put(nodeClzAnnotation.value(), (Class<? extends BaseViewRender>)clazz);
        assert nodeClz.getAnnotation(Xml.class) != null;
        final String localName = Utils.Reflect.getLocalNameFromClz(clazz);
        if (!TextUtils.isEmpty(localName)){
            NODE_NAME_MAP.put(localName, nodeClz);

        }
    }

}
