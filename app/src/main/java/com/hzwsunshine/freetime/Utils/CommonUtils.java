package com.hzwsunshine.freetime.Utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 何志伟 on 2015/11/19.
 */
public class CommonUtils {
    public static String timeFormat(String timeStr) {
        try {
            //2015-10-18 09:23:23
            String data = (timeStr.split(" ")[0]).replace("-", "");//20151018
            String str = (timeStr.split(" ")[1]);
            String time = str.substring(0, str.lastIndexOf(":"));//09:23
            //获取当前时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String nowData = formatter.format(curDate);
            int timeNum = Integer.parseInt(nowData) - Integer.parseInt(data);
            switch (timeNum) {
                case 0:
                    return "今天 " + time;
                case 1:
                    return "昨天 " + time;
                case 2:
                    return "前天 " + time;
                default:
                    return (timeStr.split(" ")[0]) + " " + time;
            }
        } catch (Exception e) {
            Log.i("xxx", "时间格式化异常：" + e.toString());
//            e.printStackTrace();
            return timeStr.substring(0, timeStr.lastIndexOf(":"));//2015-10-18 09:23
        }
    }


    /**
     * 时间戳转换成时间字符串
     */
    public static String timestamp2Date(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(new Date(Long.parseLong(time) * 1000));
    }

    /**
     * 时间字符串转换成时间戳
     */
    public static long date2Timestamp(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long timsTamp = sf.parse(time).getTime();
            return timsTamp;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 字符串转换成日期
     */
    public static Date StrToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String date2str(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str;
        try {
            str = sdf.format(date);
            return str;
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * final Activity activity  ：调用该方法的Activity实例
     * long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */
    public static void Vibrate(final Context activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void Vibrate(final Context activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "FreeTime");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(appDir)));
    }


}
