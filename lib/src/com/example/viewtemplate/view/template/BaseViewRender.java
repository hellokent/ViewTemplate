package com.example.viewtemplate.view.template;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import com.example.viewtemplate.L;
import com.example.viewtemplate.Utils;
import com.example.viewtemplate.lua.LuaUtils;
import com.example.viewtemplate.view.AbstractViewRender;
import com.example.viewtemplate.view.ClientAblity;
import com.example.viewtemplate.view.clientablity.IImageLoaderCallback;
import com.example.viewtemplate.xml.nodes.BaseViewNode;
import com.example.viewtemplate.xml.nodes.MeasuringUnit;
import org.keplerproject.luajava.LuaException;

/**
 * Created by chenyang.coder@gmail.com on 13-10-24 下午10:43.
 */
public abstract class BaseViewRender<T extends BaseViewNode> extends AbstractViewRender<T> {

    final String mId;

    public BaseViewRender(final Context context, final T node, ViewGroup parentView) {
        super(context, node, parentView);
        mId = node.id;
    }

    @Override
    protected void initView(final Context context, final T node) {
        final Pair<MeasuringUnit, Integer> widthInfo = node.getWidthInfo();
        final Pair<MeasuringUnit, Integer> heightInfo = node.getHeightInfo();
        final int width, height;

        switch (widthInfo.first){
            case PX:
                width = widthInfo.second;
                break;
            case DP:
                width = Utils.App.getPXFromDP(widthInfo.second);
                break;
            case MATCH:
                width = ViewGroup.LayoutParams.MATCH_PARENT;
                break;
            case WRAP:
                width = ViewGroup.LayoutParams.WRAP_CONTENT;
                break;
            case PRECENTAGE:
                width = ViewGroup.LayoutParams.WRAP_CONTENT;
                //TODO
                break;
            default:
                return;
        }

        switch (heightInfo.first){
            case PX:
                height = heightInfo.second;
                break;
            case DP:
                height = Utils.App.getPXFromDP(heightInfo.second);
                break;
            case MATCH:
                height = ViewGroup.LayoutParams.MATCH_PARENT;
                break;
            case WRAP:
                height = ViewGroup.LayoutParams.WRAP_CONTENT;
                break;
            case PRECENTAGE: //layout的height不支持weight
                height = ViewGroup.LayoutParams.WRAP_CONTENT;
                break;
            default:
                return;
        }

        final ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, height);
        getView().setLayoutParams(lp);

        if (!TextUtils.isEmpty(node.bg)){
            try {
                getView().setBackgroundColor(Color.parseColor(node.bg));
            } catch (Throwable throwable){
                ClientAblity.sInstance.sendImageLoadCallback(node.bg, new IImageLoaderCallback() {
                    @Override
                    public void onLoadingStarted(final String url) {
                    }

                    @Override
                    public void onLoadingFailed() {
                    }

                    @Override
                    public void onLoadingComplete(final Bitmap bitmap) {
                        getView().setBackground(new BitmapDrawable(getView().getContext().getResources(), bitmap));
                    }
                });
            }
        }


        if (!TextUtils.isEmpty(node.onClick)){
            getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    try {
                        L.v("click!");
                        LuaUtils.callFunc(node.onClick);
                    } catch (LuaException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}