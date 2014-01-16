package com.example.viewtemplate.view.template;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.viewtemplate.L;
import com.example.viewtemplate.view.ViewNodeClass;
import com.example.viewtemplate.xml.nodes.Text;

/**
 * Created by chenyang.coder@gmail.com on 13-10-24 下午10:38.
 */
@ViewNodeClass(Text.class)
public class TextViewRender extends BaseViewRender<Text> {

    TextView mTextView;

    public TextViewRender(final Context context, final Text node, final ViewGroup parentView) {
        super(context, node, parentView);
    }

    @Override
	protected void setData(final String data) {
		mTextView.setText(data);
	}

    @Override
    protected String getData() {
        return mTextView.getText().toString();
    }

    @Override
    public TextView getView() {
        return mTextView;
    }

    @Override
    protected void initView(final Context context, Text node) {
        super.initView(context, node);
        L.v("set textView with data:%s", node.getValue());
        if (!TextUtils.isEmpty(node.textColor)){
            mTextView.setTextColor(Color.parseColor(node.textColor));
        }
        mTextView.setText(node.getValue());
    }

    @Override
    protected void onCreateView(final Context context) {
        mTextView = new TextView(context);
    }
}
