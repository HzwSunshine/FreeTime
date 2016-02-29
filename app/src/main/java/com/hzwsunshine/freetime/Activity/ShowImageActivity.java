package com.hzwsunshine.freetime.Activity;

import android.animation.ObjectAnimator;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hzwsunshine.freetime.Application.Application;
import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.CommonUtils;
import com.hzwsunshine.freetime.Utils.ViewUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

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
        ViewUtils.transparentTheme(this);
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
        if (mUrl.endsWith("gif")) {
            Bitmap image = ImageLoader.getInstance().getMemoryCache().get(mUrl);
            if (image == null) {
                File fileOfDisk = ImageLoader.getInstance().getDiskCache().get(mUrl);
                if (fileOfDisk == null) {
                    Glide.with(this).load(mUrl).asGif().diskCacheStrategy(DiskCacheStrategy.NONE).into(showImage);
                } else {
                    Glide.with(this).load(fileOfDisk).asGif().diskCacheStrategy(DiskCacheStrategy.NONE).into(showImage);
                }
            } else {
                Glide.with(this).load(image).asGif().diskCacheStrategy(DiskCacheStrategy.NONE).into(showImage);
                mBitmap = image;
            }
        } else {
            displayImage(mUrl, showImage);
        }
    }

    private void displayImage(final String imgUrl, ImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        if (Application.connectedType == ConnectivityManager.TYPE_MOBILE
                || ImageLoader.getInstance().getDiskCache() == null
                || ImageLoader.getInstance().getMemoryCache() == null) {
            return;
        }
        ImageLoader.getInstance().displayImage(imgUrl, imageView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                mBitmap = loadedImage;
            }
        });
    }

    private void initView() {
        imgBack.setOnClickListener(v -> finish());
        //复制图片URL
        imgCopyUrl.setOnClickListener(v -> {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
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
                ObjectAnimator.ofFloat(bottomBar, "translationY", ViewUtils.dip2px(this, 100), 0)
                        .setDuration(300).start();
                isMove = false;
            } else {
                ObjectAnimator.ofFloat(bottomBar, "translationY", 0, ViewUtils.dip2px(this, 100))
                        .setDuration(300).start();
                isMove = true;
            }
        });
    }

}


