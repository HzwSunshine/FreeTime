package com.hzwsunshine.freetime.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.hzwsunshine.freetime.Application.Application;
import com.hzwsunshine.freetime.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by 何志伟 on 2015/12/17.
 */
public class GlideUtils {
    public static void showImage(Context t, String imgUrl, ImageView imageView) {
//        Glide.with(t)
//                .load(imgUrl)
////                .asBitmap()//作为静态图加载，默认同时支持静态图和gif图
////                .asGif()//作为gif图加载
//                .diskCacheStrategy(imgUrl.contains("gif") ? DiskCacheStrategy.SOURCE : DiskCacheStrategy.ALL)//既缓存全尺寸又缓存其他尺寸
//                .placeholder(R.drawable.fuli_image)//加载图片时的占位图片
//                .error(R.drawable.fuli_image)//加载错误时的显示图片
////                .fitCenter()//图片拉伸方式
//                .dontAnimate()//去掉动画，默认为淡入淡出动画
////                .override(100,100)//指定加载的图片宽高
////                .thumbnail(2.8f)//缩略比例
//                .into(imageView);

        if (Application.connectedType == ConnectivityManager.TYPE_MOBILE
                || Glide.getPhotoCacheDir(t) == null) {
            return;
        }

        if (imgUrl.endsWith("gif")) {
            Glide.with(t)
                    .load(imgUrl)
                    .asGif()//作为gif图加载
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        } else {
            Glide.with(t)
                    .load(imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//既缓存全尺寸又缓存其他尺寸
                    .placeholder(R.drawable.fuli_image)//加载图片时的占位图片
                    .error(R.drawable.fuli_image)//加载错误时的显示图片
                    .dontAnimate()//去掉动画，默认为淡入淡出动画
                    .into(imageView);
        }


    }

    public static void clearCache(Context context) {
        Glide.get(context).clearMemory();//这个必须在主线程中运行
        Glide.get(context).clearDiskCache();
    }

}

//参考博客：http://blog.csdn.net/theone10211024/article/details/45557859