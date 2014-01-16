package com.example.viewtemplate.view;

import com.example.viewtemplate.view.clientablity.IImageLoaderCallback;

/**
 * Created by chenyang.coder@gmail.com on 13-10-31 上午2:08.
 */
public abstract class ClientAblity {
    public static ClientAblity sInstance;

    protected static void setInstance(ClientAblity instance){
        sInstance = instance;
    }

    public abstract void sendImageLoadCallback(final String url, final IImageLoaderCallback callback);
}
