package com.example.ViewTemplateDemo;

import android.graphics.Bitmap;
import android.view.View;
import com.example.viewtemplate.view.ClientAblity;
import com.example.viewtemplate.view.clientablity.IImageLoaderCallback;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * Created by chenyang.coder@gmail.com on 13-10-31 上午2:09.
 */
public final class ClientAblityImpl extends ClientAblity {
    static final ClientAblityImpl sImpl = new ClientAblityImpl();
    static {
        setInstance(sImpl);
    }

    ImageLoader mImageLoader;

    private ClientAblityImpl(){
        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public void sendImageLoadCallback(final String url, final IImageLoaderCallback callback) {
        mImageLoader.loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(final String imageUri, final View view) {
                callback.onLoadingStarted(imageUri);
            }

            @Override
            public void onLoadingFailed(final String imageUri, final View view, final FailReason failReason) {
                callback.onLoadingFailed();
            }

            @Override
            public void onLoadingComplete(final String imageUri, final View view, final Bitmap loadedImage) {
                callback.onLoadingComplete(loadedImage);
            }

            @Override
            public void onLoadingCancelled(final String imageUri, final View view) {

            }
        });
    }
}
