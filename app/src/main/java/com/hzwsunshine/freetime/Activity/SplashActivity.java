package com.hzwsunshine.freetime.Activity;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.SharedUtils;
import com.hzwsunshine.freetime.Utils.ViewUtils;

import java.util.Random;

import butterknife.InjectView;

public class SplashActivity extends BaseActivity {

    @InjectView(R.id.rl_splash)
    RelativeLayout splashView;
    @InjectView(R.id.tv_begin_title)
    TextView beginTitle;

    @Override
    protected void onCreated(Bundle savedInstanceState) {
        //隐藏顶部状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setView(R.layout.activity_splash);
        getSupportActionBar().hide();
        transparentTheme();
        setBackground();
        setAnimation();
        new Handler().postDelayed(() -> initView(), 1700);
    }

    private void initView() {
        String key = (String) SharedUtils.get(this, "key", String.class);
        if (key != null && key.equals("key_on")) {//开启密码保护时进入输入界面
            Intent intent = new Intent(this, PassWordActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private void setAnimation() {
        new Handler().postDelayed(() -> {
            ValueAnimator colorAnim = ObjectAnimator.ofInt(beginTitle, "textColor",
                    R.color.themeColor_blue_trans, 0xFF000000);
            colorAnim.setDuration(500);
            colorAnim.setStartDelay(50);
            colorAnim.setEvaluator(new ArgbEvaluator());
            colorAnim.start();

            AnimatorSet animatorSet=new AnimatorSet();
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(beginTitle, "translationY", 0, -ViewUtils.dip2px(this,400)),
                    ObjectAnimator.ofFloat(beginTitle, "translationX", 0, -ViewUtils.dip2px(this,50)),
                    ObjectAnimator.ofFloat(beginTitle, "scaleX", 1.0f, 0.7f),
                    ObjectAnimator.ofFloat(beginTitle, "scaleY", 1.0f, 0.7f)
            );
            animatorSet.setDuration(500);
            animatorSet.start();

        }, 1200);
    }

    private void setBackground() {
        Random mRandom = new Random();
        switch (mRandom.nextInt(4)) {
            case 0:
                setBkg(R.mipmap.bkg0);
                break;
            case 1:
                setBkg(R.mipmap.bkg1);
                break;
            case 2:
                setBkg(R.mipmap.bkg2);
                break;
            case 3:
                setBkg(R.mipmap.bkg3);
                break;
        }
    }

    private void setBkg(int imgId) {
        splashView.setBackground(getResources().getDrawable(imgId));
    }

    private void transparentTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
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