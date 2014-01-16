package com.example.viewtemplate.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.example.viewtemplate.xml.XmlNode;
import com.example.viewtemplate.xml.nodes.BaseViewNode;

/**
 *
 * Created by chenyang.coder@gmail.com on 13-10-24 下午3:40.
 */
public abstract class AbstractViewRender<T extends BaseViewNode>{

	final Context mContext;
    final T mNode;
    final ViewGroup mParentView;
    final View mView;

	protected AbstractViewRender(final Context context, T node, ViewGroup parentView) {
        onCreateView(context);
        assert getView() != null;
        initView(context, node);
        final View view = getView();
        mView = view;
        if (view == null){
            throw new IllegalStateException("after initView(), view should not be null!");
        }
		mContext = context;
        mNode = node;
        mParentView = parentView;
        if (view instanceof ViewGroup){
            final ViewGroup vg = (ViewGroup) view;
            vg.removeAllViewsInLayout();
            for (XmlNode childNode : node.getChilds()){
                if (childNode instanceof BaseViewNode){
                    Class<? extends AbstractViewRender> c = ViewFactory.getComponent((BaseViewNode) childNode);
                    if (c == null){
                        continue;
                    }
                    try {
                        AbstractViewRender vc = c
                                .getConstructor(Context.class, childNode.getClass(), ViewGroup.class)
                                .newInstance(context, childNode, vg);
                        addView(vc.getView(), (BaseViewNode)childNode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
	}

    protected void addView(View v, BaseViewNode node){
    }

	protected abstract void setData(String data);

    protected abstract String getData();

	public abstract View getView();

    protected abstract void initView(Context context, T node);

    protected abstract void onCreateView(Context context);

}
