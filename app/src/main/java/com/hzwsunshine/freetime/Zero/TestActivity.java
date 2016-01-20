package com.hzwsunshine.freetime.Zero;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzwsunshine.freetime.Activity.BaseActivity;
import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.AnimUtils;
import com.hzwsunshine.freetime.Utils.ImageLoaderUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TestActivity extends BaseActivity {

    @InjectView(R.id.img1)
    ImageView img1;
    @InjectView(R.id.btn1)
    Button btn1;
    @InjectView(R.id.tv_text1)
    TextView tvText1;
    @InjectView(R.id.btn2)
    Button btn2;
    @InjectView(R.id.btn3)
    Button btn3;
    @InjectView(R.id.btn4)
    Button btn4;
    @InjectView(R.id.btn5)
    Button btn5;

    private boolean tag = true;
    private boolean tag1 = true;

    @Override
    protected void onCreated(Bundle savedInstanceState) {
        setView(R.layout.activity_test);
        setTitle(getString(R.string.menu_test));
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.inject(this);
    }

    private void initView() {
        img1.setOnClickListener(v -> {
            String imgUrl = "http://img.fotomen.cn/2012/05/jyy001-673x1009.jpg";
            ImageLoaderUtils.displayImage(img1, imgUrl, R.mipmap.defaultimage, R.mipmap.defaultimage);
        });

        btn1.setOnClickListener(v -> {
            if (tag1) {
                tvText1.setBackgroundColor(getResources().getColor(R.color.themeColor_green));
                tag1 = false;
            } else {
                tvText1.setBackgroundColor(getResources().getColor(R.color.themeColor_blue));
                tag1 = true;
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Animator animator = ViewAnimationUtils.createCircularReveal(
                        tvText1, 0, 0, 0,
                        (float) Math.hypot(tvText1.getWidth(), tvText1.getHeight()));
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(1000);
                animator.start();
            }
        });

        final int[] m = {0};
        btn2.setOnClickListener(v -> {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_header);
            Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch virbrant = null;
                    if (m[0] == 0) {
                        //通过Palette获取对应的色调
                        virbrant = palette.getDarkMutedSwatch();
                        btn2.setText("柔和的黑");
                    }
                    if (m[0] == 1) {
                        virbrant = palette.getMutedSwatch();
                        btn2.setText("柔和");
                    }
                    if (m[0] == 2) {
                        virbrant = palette.getLightVibrantSwatch();
                        btn2.setText("活力的亮");
                    }
                    if (m[0] == 3) {
                        virbrant = palette.getDarkVibrantSwatch();
                        btn2.setText("活力的黑");
                    }
                    if (m[0] == 4) {
                        virbrant = palette.getVibrantSwatch();
                        btn2.setText("活力");
                        m[0] = -1;
                    }
                    //将颜色设置给相应的组件
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(virbrant.getRgb()));
                    Window window = getWindow();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(virbrant.getRgb());
                    }
                    m[0]++;
                }
            });
        });

        btn3.setOnClickListener(v -> {
            if (tag == true) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(tvText1, "rotationY", 0, 180F);
                animator.setDuration(500);
//                animator.setStartDelay(50);
                animator.start();
                AnimUtils.toSmall(tvText1);
                tag = false;
            } else {
                AnimUtils.toLarge(tvText1);
                ObjectAnimator.ofFloat(tvText1, "rotationY", 180F, 0).setDuration(500).start();
                tag = true;
            }
        });

        btn4.setOnClickListener(v -> {
            ValueAnimator colorAnim = ObjectAnimator.ofInt(tvText1, "backgroundColor",
                    0xFF3F51B5, 0xFF009688, 0xFFFC722A);
            colorAnim.setDuration(5000);
            colorAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            colorAnim.setEvaluator(new ArgbEvaluator());
            colorAnim.setRepeatCount(3);
            colorAnim.setRepeatMode(ValueAnimator.REVERSE);
            colorAnim.start();
        });

        btn5.setOnClickListener(v -> {
//            AnimatorSet set = new AnimatorSet();
//            set.playTogether(
//                    ObjectAnimator.ofFloat(tvText1, "rotationX", 0, 360),
//                    ObjectAnimator.ofFloat(tvText1, "rotationY", 0, 360),
//                    ObjectAnimator.ofFloat(tvText1, "rotation", 0, 8),
//                    ObjectAnimator.ofFloat(tvText1, "translationX", 0, 20),
//                    ObjectAnimator.ofFloat(tvText1, "translationY", 0, 30),
//                    ObjectAnimator.ofFloat(tvText1, "scaleX", 1.0f, 1.1f,1.0f),
//                    ObjectAnimator.ofFloat(tvText1, "scaleY", 1.0f, 1.1f,1.0f),
//                    ObjectAnimator.ofFloat(tvText1, "alpha", 1, 0.75f, 1)
//            );
//            set.setDuration(300);
//            set.start();

            ObjectAnimator animator = ObjectAnimator
                    .ofFloat(tvText1, "xxx", 1.0f, 1.05f, 1.0f)
                    .setDuration(500);
            animator.start();
            animator.setRepeatCount(5);
            animator.addUpdateListener(animation -> {
                float val = (float) animation.getAnimatedValue();
                tvText1.setScaleX(val);
                tvText1.setScaleY(val);
            });
        });


    }

}
