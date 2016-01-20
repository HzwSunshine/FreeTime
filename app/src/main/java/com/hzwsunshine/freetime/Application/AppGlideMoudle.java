package com.hzwsunshine.freetime.Application;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by 何志伟 on 2015/12/17.
 */
public class AppGlideMoudle implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置最大缓存大小
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,500*1024));//500M
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);//加载全彩图片，但耗内存
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
