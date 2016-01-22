package com.hzwsunshine.freetime.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hzwsunshine.freetime.Application.Application;
import com.hzwsunshine.freetime.R;

/**
 * 控件，视图的工具类：
 *
 * @author hzw
 * @date 2015年7月22日
 */
public class ViewUtils {

    /**
     * px和dip互相转换的方法
     *
     * @param pxValue px值
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 自定义土司
     */
    public static void showToast(String msg) {
        Toast toast = new Toast(Application.getContent());
        toast.setDuration(Toast.LENGTH_SHORT);
        ViewGroup layout = new RelativeLayout(Application.getContent());
        TextView textView = (TextView) LayoutInflater.from(Application.getContent()).inflate(R.layout.toast, null);
        textView.setText(msg);
        layout.addView(textView);
        toast.setView(layout);
        toast.show();
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }


}
