package com.hzwsunshine.freetime.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hzwsunshine.freetime.R;

/**
 * Created by 何志伟 on 2015/12/17.
 */
public class GlideUtils {
    public static  void showImage(Context t, String imgUrl, ImageView imageView){
        Glide.with(t)
                .load(imgUrl)
//                .asBitmap()//作为静态图加载，默认同时支持静态图和gif图
//                .asGif()//作为gif图加载
                .diskCacheStrategy(DiskCacheStrategy.ALL)//既缓存全尺寸又缓存其他尺寸
                .placeholder(R.mipmap.defaultimage)//加载图片时的占位图片
                .error(R.mipmap.defaultimage)//加载错误时的显示图片
//                .fitCenter()//图片拉伸方式
                .dontAnimate()//去掉动画，默认为淡入淡出动画
//                .override(100,100)//指定加载的图片宽高
//                .thumbnail(2.8f)//缩略比例
                .into(imageView);
    }

}

//参考博客：http://cache.baiducontent.com/c?m=9d78d513d9961ff40eba837e7c458b305909db352890904b708ed50ed1735a325a7bb3e57a770704a4943d315db8492bb6a7706f715977eac79f8b48ddb8912a2f8e3a67305add104c8404f99c00769a7ed147efa81ee0bffa3598&p=c3759a45d5c809f00be2963a5008cd&newp=8b2a970fca9216ff57ed97755252cf231610db2151dcdb5f6385c4&user=baidu&fm=sc&query=glide+android&qid=dd13b85900101f36&p1=3
