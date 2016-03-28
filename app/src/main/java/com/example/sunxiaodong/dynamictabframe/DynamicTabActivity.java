package com.example.sunxiaodong.dynamictabframe;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicTabActivity extends AppCompatActivity {

    private TextView mTitle;
    private TabLayout mTabLayout;
    private ImageView mTabEditIcon;
    private ViewPager mViewPager;

    private DynamicTabPagerAdapter mDynamicTabPagerAdapter;

    private Map<Integer, Fragment> mCurrAddedToFMFragmentMap = new HashMap<Integer, Fragment>();//当前添加到FM中的Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_tab_activity);
        initView();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabEditIcon = (ImageView) findViewById(R.id.tab_edit_icon);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mDynamicTabPagerAdapter = new DynamicTabPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mDynamicTabPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * 动态标签页面适配器
     */
    class DynamicTabPagerAdapter extends FragmentStatePagerAdapter {

        private List<TabBean> mTabBeanList = new ArrayList<>();
        private List<Fragment> mFragmentList = new ArrayList<>();

        public DynamicTabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public int getRecoverTabPos(List<TabBean> newTabBeanList) {
            int recoverTabPos = 0;
            if (!mTabBeanList.isEmpty()) {
                TabBean currTabBean = mTabBeanList.get(mViewPager.getCurrentItem());
                for (int i = 0; i < newTabBeanList.size(); i++) {
                    TabBean newsTabBean = newTabBeanList.get(i);
                    if (newsTabBean.getId() == currTabBean.getId()) {
                        recoverTabPos = i;
                        break;
                    }
                }
            }
            return recoverTabPos;
        }

        public List<TabBean> getTabBeanList() {
            return mTabBeanList;
        }

        public void update(List<TabBean> tagBean, List<Fragment> fragmentList) {
            if (tagBean == null || fragmentList == null) {
                return;
            }
            mTabBeanList.clear();
            mTabBeanList.addAll(tagBean);
            mFragmentList.clear();
            mFragmentList.addAll(fragmentList);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mFragmentList.get(position);
            mCurrAddedToFMFragmentMap.put(mTabBeanList.get(position).getId(), fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabBeanList.get(position).getName();
        }

    }

}
