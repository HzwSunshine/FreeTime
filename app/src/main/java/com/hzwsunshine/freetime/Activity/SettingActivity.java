package com.hzwsunshine.freetime.Activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hzwsunshine.freetime.Application.Application;
import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.CommonUtils;
import com.hzwsunshine.freetime.Utils.GlideUtils;
import com.hzwsunshine.freetime.Utils.SharedUtils;
import com.hzwsunshine.freetime.Utils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.DecimalFormat;

import butterknife.InjectView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.switch_wifiConn)
    Switch switchWifiConn;
    @InjectView(R.id.tv_wifiConn)
    TextView tvWifiConn;
    @InjectView(R.id.fl_wifiConn)
    FrameLayout flWifiConn;
    @InjectView(R.id.switch_key)
    Switch switchKey;
    @InjectView(R.id.fl_key)
    FrameLayout flKey;
    @InjectView(R.id.switch_key_normal)
    Switch switchKeyNormal;
    @InjectView(R.id.fl_key_nomal)
    FrameLayout flKeyNomal;
    @InjectView(R.id.switch_key_special)
    Switch switchKeySpecial;
    @InjectView(R.id.fl_key_special)
    FrameLayout flKeySpecial;
    @InjectView(R.id.tv_keySpecial_text)
    TextView tvKeySpecialText;
    @InjectView(R.id.tv_keyNormal)
    TextView tvKeyNormal;
    @InjectView(R.id.tv_keySpecial)
    TextView tvKeySpecial;
    @InjectView(R.id.fl_clear_cache)
    FrameLayout clearCache;
    @InjectView(R.id.tv_cache_size)
    TextView cacheSizeText;

    @Override
    protected void onCreated(Bundle savedInstanceState) {
        setView(R.layout.activity_setting);
        setTitle(getString(R.string.menu_setting));
//        RefWatcher refWatcher = Application.getRefWatcher(this);//内存泄露检测
//        refWatcher.watch(this);
        initView();
    }

    private void initView() {
        setBackground(flWifiConn);
        setBackground(flKey);
        setBackground(flKeyNomal);
        setBackground(flKeySpecial);
        setBackground(clearCache);

        String netConn = (String) SharedUtils.get(this, "net_conn", String.class);
        if (netConn.equals("wifi_only")) {
            tvWifiConn.setVisibility(View.GONE);
            switchWifiConn.setChecked(false);
        } else if (netConn.equals("all_type")) {
            tvWifiConn.setVisibility(View.VISIBLE);
            switchWifiConn.setChecked(true);
        }

        String key = (String) SharedUtils.get(this, "key", String.class);
        if (key != null && key.equals("key_on")) {
            switchKey.setChecked(true);
            setView1();
        } else if (key != null && key.equals("key_off")) {
            switchKey.setChecked(false);
            setView1();
            setView2();
        } else {
            setView2();
        }
        //显示缓存大小
        File cacheFile = ImageLoader.getInstance().getDiskCache().getDirectory();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        double cacheSize = CommonUtils.getDirSize(cacheFile) + CommonUtils.getDirSize(Glide.getPhotoCacheDir(this).getParentFile());
        cacheSizeText.setText(decimalFormat.format(cacheSize) + "M");


    }

    private void setBackground(View view) {
        //系统版本大于5.0，设置水波纹背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setBackground(getDrawable(R.drawable.ripple_bg));
        }
    }

    private void setView1() {
        String key1 = (String) SharedUtils.get(this, "key_normal", String.class);
        if (key1 != null && key1.equals("key_on")) {
            switchKeyNormal.setChecked(true);
            switchKeySpecial.setChecked(false);
        } else if (key1 != null && key1.equals("key_off")) {
            switchKeyNormal.setChecked(false);
            switchKeySpecial.setChecked(true);
            tvKeySpecialText.setVisibility(View.VISIBLE);
        }
    }

    private void setView2() {
        flKeyNomal.setEnabled(false);
        flKeySpecial.setEnabled(false);
        tvKeyNormal.setTextColor(getResources().getColor(R.color.gravy));
        tvKeySpecial.setTextColor(getResources().getColor(R.color.gravy));
    }

    @OnClick({R.id.fl_wifiConn, R.id.fl_key, R.id.fl_key_nomal, R.id.fl_key_special,
            R.id.fl_clear_cache, R.id.btn_about_app})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_wifiConn:
                if (switchWifiConn.isChecked()) {
                    switchWifiConn.setChecked(false);
                    tvWifiConn.setVisibility(View.GONE);
                    Application.connectedType = ConnectivityManager.TYPE_MOBILE;
                    SharedUtils.put(this, "net_conn", "wifi_only");
                } else {
                    switchWifiConn.setChecked(true);
                    tvWifiConn.setVisibility(View.VISIBLE);
                    Application.connectedType = ConnectivityManager.TYPE_WIFI;
                    SharedUtils.put(this, "net_conn", "all_type");
                }
                break;
            case R.id.fl_key:
                if (switchKey.isChecked()) {
                    switchKey.setChecked(false);
                    flKeyNomal.setEnabled(false);
                    flKeySpecial.setEnabled(false);
                    tvKeyNormal.setTextColor(getResources().getColor(R.color.gravy));
                    tvKeySpecial.setTextColor(getResources().getColor(R.color.gravy));
                    SharedUtils.put(this, "key", "key_off");
                } else {
                    switchKey.setChecked(true);
                    flKeyNomal.setEnabled(true);
                    flKeySpecial.setEnabled(true);
                    tvKeyNormal.setTextColor(Color.parseColor("#000000"));
                    tvKeySpecial.setTextColor(Color.parseColor("#000000"));
                    SharedUtils.put(this, "key", "key_on");
                }
                break;
            case R.id.fl_key_nomal:
                if (switchKeyNormal.isChecked()) {
                    inputKeyDialog("closeKey");
                } else {
                    inputKeyDialog("openKey");
                }
                break;
            case R.id.fl_key_special:
                if (switchKeySpecial.isChecked()) {
                    inputKeyDialog("openKey");
                } else {
                    String key1 = (String) SharedUtils.get(this, "key_normal", String.class);
                    if (key1 != null && (!key1.equals("key_on") || !key1.equals("key_off"))) {
                        switchKeySpecial.setChecked(true);
                        tvKeySpecialText.setVisibility(View.VISIBLE);
                        SharedUtils.put(this, "key_normal", "key_off");
                    } else {
                        inputKeyDialog("closeKey");
                    }
                }
                break;
            case R.id.fl_clear_cache:
                ViewUtils.showToast(getString(R.string.clearCache));
                cacheSizeText.setText("0M");
                Glide.get(this.getApplication()).clearMemory();//这个必须在主线程中运行
                new Thread(() -> {//异步清除缓存
                    ImageLoader.getInstance().clearMemoryCache();
                    ImageLoader.getInstance().clearDiskCache();
                    Glide.get(this.getApplication()).clearDiskCache();
                }).start();
                break;
            case R.id.btn_about_app:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.share_github));
                builder.setPositiveButton(getString(R.string.copy_url_github), (dialog, which) -> {
                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText("https://github.com/HzwSunshine/FreeTime");
                    ViewUtils.showToast(getString(R.string.copyed_url_github));
                }).show();
                break;
        }
    }

    private void inputKeyDialog(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.inputKey));
        builder.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.item_dialog_view, null);
        EditText key = (EditText) view.findViewById(R.id.et_key);
        EditText keyRepeat = (EditText) view.findViewById(R.id.et_keyRepeat);
        if (type.equals("closeKey")) {
            keyRepeat.setVisibility(View.GONE);
        }
        builder.setView(view);
        builder.setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
            if (type.equals("openKey")) {
                if (key.getText().toString().equals(keyRepeat.getText().toString()) &&
                        key.getText().toString().length() == 4) {
                    SharedUtils.put(this, "password", key.getText().toString());
                    ViewUtils.showToast(getString(R.string.openKey));
                    switchKeyNormal.setChecked(true);
                    switchKeySpecial.setChecked(false);
                    tvKeySpecialText.setVisibility(View.GONE);
                    SharedUtils.put(this, "key_normal", "key_on");
                } else {
                    //这里失败时，不应该让dialog消失，可用java反射禁止dialog消失，但目前我还不会，汗！
                    ViewUtils.showToast(getString(R.string.keyError));
                }
            } else if (type.equals("closeKey")) {
                String passWord = (String) SharedUtils.get(this, "password", String.class);
                if (key.getText().toString().equals(passWord)) {
                    SharedUtils.put(this, "password", "");
                    ViewUtils.showToast(getString(R.string.closeKey));
                    switchKeyNormal.setChecked(false);
                    switchKeySpecial.setChecked(true);
                    tvKeySpecialText.setVisibility(View.VISIBLE);
                    SharedUtils.put(this, "key_normal", "key_off");
                } else {
                    ViewUtils.showToast(getString(R.string.keyError));
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.show();
    }

}
