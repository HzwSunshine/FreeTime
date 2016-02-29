package com.hzwsunshine.freetime.Application;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hzwsunshine.freetime.Utils.DBUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * 作为全局变量和全局对象的使用
 * Created by He Zhiwei on 2015/9/26.
 */
public class Application extends android.app.Application {
    //    private static  final Application instance=new Application();
    private static Context appContent;
    //用于标示当前网络的连接方式，无网络连接时该值为-1
    public static int connectedType = -1;
    public static Map<String, Map<String, Integer>> imageWH = new HashMap<>();

    /**
     * android应用程序真正的入口
     * 此方法在所有activity，service组件之前调用
     */
    @Override
    public void onCreate() {
        super.onCreate();
        appContent = this;
        //初始化图片加载工具，初始化为默认的配置
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        try {
            //在application得到DaoMaster和DaoSession
            DBUtils.setupDatabase(this);
            DBUtils.getImageCacheDao();
            DBUtils.getCSDNCacheDao();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("xxx", "数据库初始化异常：" + e.toString());
        }
    }

    public static Context getContent() {
        return appContent;
    }

    /**
     * 在程序结束时调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }



}
