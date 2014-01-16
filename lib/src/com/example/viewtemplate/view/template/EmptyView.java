package com.example.viewtemplate.view.template;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.example.viewtemplate.xml.nodes.BaseViewNode;
import com.example.viewtemplate.xml.nodes.Text;

/**
 * Created by chenyang.coder@gmail.com on 13-11-5 上午2:07.
 */
public final class EmptyView extends BaseViewRender<BaseViewNode> {

    View mView;

    protected EmptyView(final Context context, final Text node, final ViewGroup parentView) {
        super(context, node, parentView);
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
        return mView;
    }

    @Override
    protected void initView(final Context context, BaseViewNode node) {
    }

    @Override
    protected void onCreateView(final Context context) {
        mView = new View(context);
    }
}
