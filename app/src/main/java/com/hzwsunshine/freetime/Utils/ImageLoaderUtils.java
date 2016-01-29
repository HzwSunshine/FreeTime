package com.hzwsunshine.freetime.Utils;

import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hzwsunshine.freetime.Application.Application;
import com.hzwsunshine.freetime.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by He Zhiwei on 2015/8/24.
 */
public class ImageLoaderUtils {

    public static void displayImage(final ImageView imgVew, final String imgUrl, int loadingImg, int loadErrorImg) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingImg)
                .showImageOnFail(loadErrorImg)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(new FadeInBitmapDisplayer(300))//图片加载好后的渐入动画
                .build();
        if (Application.connectedType == ConnectivityManager.TYPE_MOBILE
                || ImageLoader.getInstance().getDiskCache() == null
                || ImageLoader.getInstance().getMemoryCache() == null) {
            return;
        }
        ImageLoader.getInstance().displayImage(imgUrl, imgVew, options, new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        Map<String, Integer> wh = new HashMap<>();
                        wh.put("width", loadedImage.getWidth());
                        wh.put("height", loadedImage.getHeight());
                        Application.imageWH.put(imgUrl, wh);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);
                    }
                },
                new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {

                    }
                });
    }

}
