package com.hzwsunshine.freetime.Activity;

import android.content.Intent;
import android.graphics.Color;
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

import com.hzwsunshine.freetime.Fragment.BeautyFunnyFragment;
import com.hzwsunshine.freetime.Fragment.CSDNFragment;
import com.hzwsunshine.freetime.Fragment.FuLiImageFragment;
import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.SharedUtils;
import com.hzwsunshine.freetime.Utils.ViewUtils;

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
        ViewUtils.transparentTheme(this);//透明的主题
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
            } else if (i == 1) {
                bundle.putString("tag", "BeautyImage");
            }
            fragmentArray[i].setArguments(bundle);
            mTransaction.add(R.id.show_fragment, fragmentArray[i]);
            mTransaction.hide(fragmentArray[i]);
        }
        //显示上次退出时显示的Fragment，三目运算符是为了程序的健壮性
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
//            case R.id.nav_beauty_image:
//                tag = 1;
//                hideAndShowFragment(tag);
//                break;
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
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                }, 200);
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
//            moveTaskToBack(false);//退到后台但不销毁
            exitAppDialog();
        }
    }

    private void exitAppDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.exitTitle));
        dialog.setPositiveButton(getString(R.string.confirm), (dialog1, which) -> {
            System.gc();
            this.finish();
        });
        dialog.setNegativeButton(getString(R.string.cancel), (dialog1, which) -> {
        });
        dialog.show();
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
