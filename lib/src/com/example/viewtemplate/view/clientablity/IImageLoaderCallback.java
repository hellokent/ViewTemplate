package com.example.viewtemplate.view.clientablity;

import android.graphics.Bitmap;

/**
 * Created by chenyang.coder@gmail.com on 13-10-31 上午1:44.
 */
public interface IImageLoaderCallback {
    /**
     * 开始加载图片的回调
     * @param url 图片url地址
     */
    void onLoadingStarted(String url);
    void onLoadingFailed();
    void onLoadingComplete(Bitmap bitmap);
}
