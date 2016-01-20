package com.hzwsunshine.freetime.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
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
     *
     * @param msg
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

    public static int[] getScreenWH() {
        WindowManager wm = (WindowManager) Application.getContent().
                getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        int[] screenInfo = new int[]{width, height};
        return screenInfo;
    }


}
