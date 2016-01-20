package com.hzwsunshine.freetime.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.hzwsunshine.freetime.Application.Interface;
import com.hzwsunshine.freetime.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.InjectView;

public class CSDNFragment extends BaseFragment {

    @InjectView(R.id.tabLayout)
    SmartTabLayout tabLayout;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    public void onCreateView(ViewGroup container, Bundle savedInstanceState) {
        setView(R.layout.fragment_csdn, null);
        initView();
    }

    private void initView() {
        Bundle bundle0 = new Bundle();
        bundle0.putString("news", Interface.csdn_news);
        Bundle bundle1 = new Bundle();
        bundle1.putString("mobile", Interface.csdn_mobile);
        Bundle bundle2 = new Bundle();
        bundle2.putString("sd", Interface.csdn_sd);
        Bundle bundle3 = new Bundle();
        bundle3.putString("programmer", Interface.csdn_programmer);
        Bundle bundle4 = new Bundle();
        bundle4.putString("cloud", Interface.csdn_cloud);
        new Handler().postDelayed(() -> {
            FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                    getFragmentManager(), FragmentPagerItems.with(getActivity())
                    .add(getString(R.string.csdn_news), CSDNItemFragment.class, bundle0)
                    .add(getString(R.string.csdn_mobile), CSDNItemFragment.class, bundle1)
                    .add(getString(R.string.csdn_develope), CSDNItemFragment.class, bundle2)
                    .add(getString(R.string.csdn_magazine), CSDNItemFragment.class, bundle3)
                    .add(getString(R.string.csdn_yun), CSDNItemFragment.class, bundle4)
                    .create());
            viewPager.setAdapter(adapter);
            tabLayout.setViewPager(viewPager);
        }, 180);

    }

}
