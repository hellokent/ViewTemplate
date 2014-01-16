package com.example.viewtemplate.view.template;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.viewtemplate.L;
import com.example.viewtemplate.Utils;
import com.example.viewtemplate.view.ViewNodeClass;
import com.example.viewtemplate.xml.nodes.BaseViewNode;
import com.example.viewtemplate.xml.nodes.Layout;

/**
 * 线性布局的Render，用来将Layout标签翻译成LinearLayout
 * Created by chenyang.coder@gmail.com on 13-11-20 上午1:13.
 */
@ViewNodeClass(Layout.class)
public class LayoutRender extends BaseViewRender<Layout> {

    LinearLayout mLayout;

    public LayoutRender(final Context context, final Layout node, final ViewGroup parentView) {
        super(context, node, parentView);
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mLayout.getLayoutParams());
        L.v("layout render, layout parms:(%s, %s)",
                Utils.Text.layoutSizeToString(lp.width),
                Utils.Text.layoutSizeToString(lp.height));
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        mLayout.setLayoutParams(lp);
        final String ori = node.orientation;
        if (TextUtils.isEmpty(ori)){
            mLayout.setOrientation(LinearLayout.VERTICAL);
        } else if (ori.toLowerCase().startsWith("h")){
            mLayout.setOrientation(LinearLayout.HORIZONTAL);
        }else {
            mLayout.setOrientation(LinearLayout.VERTICAL);
        }
    }

    @Override
    protected void setData(final String data) {
    }

    @Override
    protected String getData() {
        return null;
    }

    @Override
    public View getView() {
        return mLayout;
    }

    @Override
    protected void onCreateView(final Context context) {
        mLayout = new LinearLayout(context){
            @Override
            protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                L.v("layout, after measure: %s, %s",
                        MeasureSpec.toString(getMeasuredWidth()),
                        MeasureSpec.toString(getMeasuredHeight()));
            }
        };
    }

    @Override
    protected void addView(final View v, final BaseViewNode node) {
        super.addView(v, node);
        mLayout.addView(v);
    }
}
