package com.hzwsunshine.freetime.Activity;

import android.animation.ObjectAnimator;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.CommonUtils;
import com.hzwsunshine.freetime.Utils.ViewUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import butterknife.InjectView;
import uk.co.senab.photoview.PhotoView;

public class ShowImageActivity extends BaseActivity {

    @InjectView(R.id.img_showImage)
    PhotoView showImage;
    @InjectView(R.id.img_back)
    ImageView imgBack;
    @InjectView(R.id.img_copyUrl)
    ImageView imgCopyUrl;
    @InjectView(R.id.img_download)
    ImageView imgDownload;
    @InjectView(R.id.fl_bottomBar)
    FrameLayout bottomBar;
    @InjectView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;

    private String mUrl;
    private Bitmap mBitmap;
    private boolean isMove = false;

    @Override
    protected void onCreated(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setView(R.layout.activity_show_image);
        getSupportActionBar().hide();
        setTransTheme();
        initView();
        setShowImage();
    }

    private void setShowImage() {
        Bundle bundle = getIntent().getExtras();
        mUrl = bundle.getString("imgUrl");
//        String w = bundle.getString("imgW");
//        String h = bundle.getString("imgH");
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) showImage.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        displayImage(mUrl);
    }

    private void displayImage(final String imgUrl) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.defaultimage)
                .showImageOnFail(R.mipmap.defaultimage)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(imgUrl, showImage, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                mBitmap = loadedImage;
            }
        });
    }

    private void setTransTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void initView() {
        imgBack.setOnClickListener(v -> finish());
        //复制图片URL
        imgCopyUrl.setOnClickListener(v -> {
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(mUrl);
            ViewUtils.showToast(getString(R.string.copyImgUrl));
        });
        //保存图片
        imgDownload.setOnClickListener(v -> {
            if (mBitmap != null) {
                CommonUtils.saveImageToGallery(this, mBitmap);
                ViewUtils.showToast(getString(R.string.downloadImg));
            }
        });
        mRelativeLayout.setOnClickListener(v -> {
            if (isMove) {
                ObjectAnimator.ofFloat(bottomBar, "translationY", ViewUtils.dip2px(this,100), 0)
                        .setDuration(300).start();
                isMove = false;
            } else {
                ObjectAnimator.ofFloat(bottomBar, "translationY", 0, ViewUtils.dip2px(this,100))
                        .setDuration(300).start();
                isMove = true;
            }
        });
    }

}

