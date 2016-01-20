package com.hzwsunshine.freetime.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hzwsunshine.freetime.Fragment.BeautyFunnyFragment;
import com.hzwsunshine.freetime.Fragment.CSDNFragment;
import com.hzwsunshine.freetime.Fragment.FuLiImageFragment;
import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.SharedUtils;
import com.hzwsunshine.freetime.Zero.Test2Activity;
import com.hzwsunshine.freetime.Zero.TestActivity;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private Class[] fragmentNameArray = {BeautyFunnyFragment.class, BeautyFunnyFragment.class,
            FuLiImageFragment.class, CSDNFragment.class};
    private Fragment[] fragmentArray = new Fragment[fragmentNameArray.length];
    private FragmentManager mManager;
    private FragmentTransaction mTransaction;
    private int fragmentTag = 0;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private String[] mTitleArray;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //透明的主题
        transparentTheme();
        initFragment();
        initView();
    }

    private void initFragment() {
        mManager = getSupportFragmentManager();
        mTransaction = mManager.beginTransaction();
        for (int i = 0; i < fragmentNameArray.length; i++) {
            //启动时创建所有的Fragment视图，并加载
            fragmentArray[i] = Fragment.instantiate(this, fragmentNameArray[i].getName());
            Bundle bundle = new Bundle();
            if (i == 0) {
                bundle.putString("tag", "FunnyImage");
            }
            if (i == 1) {
                bundle.putString("tag", "BeautyImage");
            }
            fragmentArray[i].setArguments(bundle);
            mTransaction.add(R.id.show_fragment, fragmentArray[i]);
            mTransaction.hide(fragmentArray[i]);
        }
        //显示上次退出时显示的Fragment，这里之所以有if-else的判断是为了程序的健壮性
        Object tag = SharedUtils.get(this, "CurrentFragment", Integer.class);
        fragmentTag = tag == null ? 0 : (int) tag;
        mTransaction.show(fragmentArray[fragmentTag]);
        mTransaction.commit();
    }

    private void initView() {
        //初始化Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mTitleArray = new String[]{getString(R.string.funnyImage), getString(R.string.beautyImage),
                getString(R.string.FuLiImage), getString(R.string.CSDN_News)};
        mToolbar.setTitle(mTitleArray[fragmentTag]);
        setSupportActionBar(mToolbar);
        //初始化DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();//初始化状态
        //初始化NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        mTransaction = mManager.beginTransaction();
        int tag;
        switch (menuItem.getItemId()) {
            case R.id.nav_funny_image:
                tag = 0;
                hideAndShowFragment(tag);
                break;
            case R.id.nav_beauty_image:
                tag = 1;
                hideAndShowFragment(tag);
                break;
            case R.id.nav_fuli_image:
                tag = 2;
                hideAndShowFragment(tag);
                break;
            case R.id.nav_csdn:
                tag = 3;
                hideAndShowFragment(tag);
                break;
            case R.id.nav_setting:
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(getApplication(), SettingActivity.class);
                    startActivity(intent);
                }, 180);
                break;
            case R.id.nav_test:
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(getApplication(), TestActivity.class);
                    startActivity(intent);
                }, 180);
                break;
            case R.id.nav_test1:
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(getApplication(), Test2Activity.class);
                    startActivity(intent);
                }, 180);
                break;
            case R.id.nav_exit:
                exitAppDialog();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void hideAndShowFragment(int tag) {
        mToolbar.setTitle(mTitleArray[tag]);
        mTransaction = mManager.beginTransaction();
        //隐藏正在显示的Fragment
        mTransaction.hide(fragmentArray[fragmentTag]);
        //显示切换后的Fragment
        mTransaction.show(fragmentArray[tag]);
        fragmentTag = tag;
        mTransaction.commit();
        SharedUtils.put(this, "CurrentFragment", tag);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if ((System.currentTimeMillis() - exitTime) > 2000) {
            exitTime = System.currentTimeMillis();
        } else {
            moveTaskToBack(false);
        }
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
        }
    }

    private void exitAppDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.exitTitle));
        dialog.setPositiveButton(getString(R.string.confirm), (dialog1, which) -> {
            exitApp();
        });
        dialog.setNegativeButton(getString(R.string.cancel), (dialog1, which) -> {
        });
        dialog.show();
    }

    private void exitApp() {
        finish();
        System.gc();
        System.exit(0);
    }

}


//switch (newState) {
//        case 0:
//        ImageLoader.getInstance().resume();
//        break;
//        case 1:
//        ImageLoader.getInstance().pause();
//        break;
//        case 2:
//        ImageLoader.getInstance().pause();
//        break;
//        }


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();


//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        Animator animator = ViewAnimationUtils.createCircularReveal(progressView,
//        progressView.getWidth() / 2, progressView.getHeight() / 2, progressView.getWidth(), 0);
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());
//        animator.setDuration(1000);
//        animator.start();
//        }

//if ((System.currentTimeMillis() - exitTime) > 2000) {
//        ViewUtils.showToast(getString(R.string.exit_press_again));
//        exitTime = System.currentTimeMillis();
//        } else {
////            exitApp();
//        moveTaskToBack(false);
//        }