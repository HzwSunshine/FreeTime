package com.hzwsunshine.freetime.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
//        toast.setGravity(View.VISIBLE,0,0);//土司的位置
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


    public static void transparentTheme(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }


}
