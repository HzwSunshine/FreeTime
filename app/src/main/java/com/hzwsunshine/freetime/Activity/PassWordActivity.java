package com.hzwsunshine.freetime.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.CommonUtils;
import com.hzwsunshine.freetime.Utils.SharedUtils;
import com.hzwsunshine.freetime.Utils.ViewUtils;

import butterknife.InjectView;
import butterknife.OnClick;

public class PassWordActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.text_1)
    TextView text1;
    @InjectView(R.id.text_2)
    TextView text2;
    @InjectView(R.id.text_3)
    TextView text3;
    @InjectView(R.id.text_4)
    TextView text4;
    @InjectView(R.id.et_password)
    EditText password;
    @InjectView(R.id.ll_passwordGroup)
    LinearLayout passwordGroup;
    @InjectView(R.id.rl_view)
    RelativeLayout rlView;

    private static int tag = 0;
    private String mKey;
    private String mPassWord;
    private String mKeyNormal;

    @Override
    protected void onCreated(Bundle savedInstanceState) {
        setView(R.layout.activity_pass_word);
        passwordGroup.setOnClickListener(this);
        initView();
        initData();
    }

    private void initView() {
        getSupportActionBar().hide();
        rlView.setBackground(getResources().getDrawable(R.mipmap.bkg0));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#E1DDDF"));
        }
        mKey = (String) SharedUtils.get(this, "key", String.class);
        mPassWord = (String) SharedUtils.get(this, "password", String.class);
        mKeyNormal = (String) SharedUtils.get(this, "key_normal", String.class);
    }

    private void initData() {
        password.addTextChangedListener(new TextIncreaseChange());
    }

    @OnClick({R.id.tv_title})
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_passwordGroup) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(password, InputMethodManager.SHOW_FORCED);
        }
        if (v.getId() == R.id.tv_title && password.length() == 2
                && mKey.equals("key_on") && mKeyNormal.equals("key_off")) {
            //密码已开关打开，并且正常方式关闭（即特殊方式打开），才会执行此方法
            tag++;
            if (tag == 3) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                tag = 0;
            }
        }
    }

    class TextIncreaseChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            switch (str.length()) {
                case 1:
                    text1.setText(str);
                    text2.setText("");
                    break;
                case 2:
                    text2.setText(str.charAt(1) + "");
                    text3.setText("");
                    break;
                case 3:
                    text3.setText(str.charAt(2) + "");
                    text4.setText("");
                    break;
                case 4:
                    text4.setText(str.charAt(3) + "");
                    break;
                default:
                    text1.setText("");
            }
            if (str.length() == 4) {
                new Handler().postDelayed(() -> {
                    if (mKey.equals("key_on")&&mKeyNormal.equals("key_on")) {
                        //密码保护打开，并且正常方式打开，才会执行此方法
                        if (password.getText().toString().equals(mPassWord)) {
                            Intent intent = new Intent(PassWordActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            initTextView();
                        }
                    } else {
                        initTextView();
                    }
                }, 300);
            }
        }

        private void initTextView() {
            ViewUtils.showToast(getString(R.string.keyError));
            password.setText("");
            text2.setText("");
            text3.setText("");
            text4.setText("");
            Animation animation = AnimationUtils.loadAnimation(getApplication(), R.anim.shake);
            passwordGroup.startAnimation(animation);
            //手机震动300毫秒
            CommonUtils.Vibrate(getApplication(), 300);
        }
    }

}
