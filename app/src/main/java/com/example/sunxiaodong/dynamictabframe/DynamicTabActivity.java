package com.example.sunxiaodong.dynamictabframe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sunxiaodong.dynamictabframe.DragSortAdapter.DragSortAdapter;
import com.example.sunxiaodong.dynamictabframe.DragSortAdapter.DragSortItemTouchHelperCallback;
import com.example.sunxiaodong.dynamictabframe.DragSortAdapter.OnStartDragListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicTabActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] mMeTabDef = new String[]{"标签1", "标签2", "标签3", "标签4", "标签5", "标签6", "标签7", "标签8", "标签9", "标签10"};
    private String[] mMoreTabDef = new String[]{"标签11", "标签12", "标签13", "标签14", "标签15", "标签16", "标签17", "标签18"};

    private TextView mTitle;
    private TabLayout mTabLayout;
    private ImageView mTabEditIcon;
    private ViewPager mViewPager;
    private View mMaskView;
    private ScrollView mTabDealLayout;
    private RecyclerView mMeRecyclerView;
    private RecyclerView mMoreRecyclerView;

    private DragSortAdapter mMeDragSortAdapter;//我的标签适配器
    private DragSortAdapter mMoreDragSortAdapter;//更改标签适配器

    private DynamicTabPagerAdapter mDynamicTabPagerAdapter;

    private boolean mIsTabOpera = false;//标签编辑 打开/关闭状态

    private static final int SPAN_COUNT = 3;//grid列数

    private static final int ANIM_DURATION = 500;//动画执行时长
    private ObjectAnimator mMaskShowAnim;//遮罩层显示动画
    private ObjectAnimator mTabDealShowAnim;//标签处理布局显示动画
    private ObjectAnimator mMaskHideAnim;//遮罩层隐藏动画
    private ObjectAnimator mTabDealHideAnim;//标签处理布局隐藏动画

    private Map<Integer, Fragment> mCurrAddedToFMFragmentMap = new HashMap<Integer, Fragment>();//当前添加到FM中的Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_tab_activity);
        initView();
        initData();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabEditIcon = (ImageView) findViewById(R.id.tab_edit_icon);
        mTabEditIcon.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mDynamicTabPagerAdapter = new DynamicTabPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mDynamicTabPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mMaskView = (View) findViewById(R.id.mask_layer);
        mMaskView.setOnClickListener(this);
        mTabDealLayout = (ScrollView) findViewById(R.id.tab_deal_layout);
        mMeRecyclerView = (RecyclerView) findViewById(R.id.me_recycler_view);
        mMoreRecyclerView = (RecyclerView) findViewById(R.id.more_recycler_view);
        initMeRecyclerView();
        initMoreRecyclerView();
    }

    private void initMeRecyclerView() {
        mMeDragSortAdapter = new DragSortAdapter(this);
        mMeDragSortAdapter.setOnItemClickListener(new DragSortAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position, TabBean data) {
                mMeDragSortAdapter.onItemDismiss(position);
                mMoreDragSortAdapter.addItemAtEnd(data);
            }
        });
        mMeRecyclerView.setHasFixedSize(true);
        mMeRecyclerView.setAdapter(mMeDragSortAdapter);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        mMeRecyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper.Callback callback = new DragSortItemTouchHelperCallback(mMeDragSortAdapter);
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mMeRecyclerView);
        mMeDragSortAdapter.setOnStartDragListener(new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                itemTouchHelper.startDrag(viewHolder);
            }
        });
    }

    private void initMoreRecyclerView() {
        mMoreDragSortAdapter = new DragSortAdapter(this);
        mMoreDragSortAdapter.setOnItemClickListener(new DragSortAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position, TabBean data) {
                mMoreDragSortAdapter.onItemDismiss(position);
                mMeDragSortAdapter.addItemAtEnd(data);
            }
        });
        mMoreRecyclerView.setHasFixedSize(true);
        mMoreRecyclerView.setAdapter(mMoreDragSortAdapter);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        mMoreRecyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper.Callback callback = new DragSortItemTouchHelperCallback(mMoreDragSortAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mMoreRecyclerView);
    }

    private void initData() {
        List<TabBean> meTabBeanList = new ArrayList<>();
        int meTabDefLength = mMeTabDef.length;
        for (int i = 1; i <= meTabDefLength; i++) {
            TabBean tabBean = new TabBean();
            tabBean.setId(i);
            tabBean.setName(mMeTabDef[i - 1]);
            if (i < 3) {
                //将前两项设置为默认项，不允许执行任何操作
                tabBean.setDefault(true);
            }
            meTabBeanList.add(tabBean);
        }
        mMeDragSortAdapter.update(meTabBeanList);
        List<TabBean> moreTabBeanList = new ArrayList<>();
        for (int i = 1; i <= mMoreTabDef.length; i++) {
            TabBean tabBean = new TabBean();
            tabBean.setId(i + meTabDefLength);
            tabBean.setName(mMoreTabDef[i - 1]);
            moreTabBeanList.add(tabBean);
        }
        mMoreDragSortAdapter.update(moreTabBeanList);
        refresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_edit_icon:
            case R.id.mask_layer:
                toggle();
                break;
        }
    }

    private void toggle() {
        if (mIsTabOpera) {
            closeUserChannelOpera();
            mIsTabOpera = false;
        } else {
            mIsTabOpera = true;
            openUserChannelOpera();
        }
    }

    private ObjectAnimator createMaskShowAnim() {
        if (mMaskShowAnim == null) {
            mMaskShowAnim = ObjectAnimator.ofFloat(mMaskView, "alpha", 0.0f, 0.5f);
        }
        return mMaskShowAnim;
    }

    private ObjectAnimator createTabDealShowAnim() {
        if (mTabDealShowAnim == null) {
            mTabDealShowAnim = ObjectAnimator.ofFloat(mTabDealLayout, "alpha", 0.0f, 1.0f);
        }
        return mTabDealShowAnim;
    }

    private ObjectAnimator createTabDealLayoutTranInAnim() {
        float tabDealLayoutHeight = mTabDealLayout.getHeight();
        ObjectAnimator tabDealLayoutTranInAnim = ObjectAnimator.ofFloat(mTabDealLayout, "translationY", -tabDealLayoutHeight, 0);
        return tabDealLayoutTranInAnim;
    }

    private void openUserChannelOpera() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIM_DURATION);
        animatorSet.play(createMaskShowAnim()).with(createTabDealShowAnim()).with(createTabDealLayoutTranInAnim());
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mTitle.setVisibility(View.VISIBLE);
                mTabLayout.setVisibility(View.GONE);
                mMaskView.setVisibility(View.VISIBLE);
                mTabDealLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

        });
        animatorSet.start();
    }

    private ObjectAnimator createMaskHideAnim() {
        if (mMaskHideAnim == null) {
            mMaskHideAnim = ObjectAnimator.ofFloat(mMaskView, "alpha", 0.5f, 0.0f);
        }
        return mMaskHideAnim;
    }

    private ObjectAnimator createTabDealHideAnim() {
        if (mTabDealHideAnim == null) {
            mTabDealHideAnim = ObjectAnimator.ofFloat(mTabDealLayout, "alpha", 1.0f, 0.0f);
        }
        return mTabDealHideAnim;
    }

    private ObjectAnimator createTabDealLayoutTranOutAnim() {
        float tabDealLayoutHeight = mTabDealLayout.getHeight();
        ObjectAnimator tabDealLayoutTranInAnim = ObjectAnimator.ofFloat(mTabDealLayout, "translationY", 0, -tabDealLayoutHeight);
        return tabDealLayoutTranInAnim;
    }

    private void closeUserChannelOpera() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIM_DURATION);
        animatorSet.play(createMaskHideAnim()).with(createTabDealHideAnim()).with(createTabDealLayoutTranOutAnim());
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mTabLayout.setVisibility(View.VISIBLE);
                mTitle.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mMaskView.setVisibility(View.GONE);
                mTabDealLayout.setVisibility(View.INVISIBLE);
            }

        });
        animatorSet.start();
        refresh();
    }

    private boolean isTabNoChange(List<TabBean> newTabBeanList) {
        List<TabBean> tabBeanList = mDynamicTabPagerAdapter.getTabBeanList();
        if (tabBeanList.size() != newTabBeanList.size()) {
            return false;
        }
        for (int i = 0; i < tabBeanList.size(); i++) {
            if (tabBeanList.get(i).getId() != newTabBeanList.get(i).getId()) {
                return false;
            }
        }
        return true;
    }

    private void refresh() {
        List<TabBean> tagBeanList = mMeDragSortAdapter.getData();
        boolean isTabNoChange = isTabNoChange(tagBeanList);
        if (isTabNoChange) {
            return;
        }
        List<Fragment> fragmentList = new ArrayList<>();
        Map<Integer, Fragment> currRetainFragmentMap = new HashMap<Integer, Fragment>();
        for (TabBean tabBean : tagBeanList) {
            int id = tabBean.getId();
            Fragment fragment = mCurrAddedToFMFragmentMap.remove(id);
            if (fragment == null) {
                ContentFragment contentFragment = ContentFragment.newInstance(id);
                fragmentList.add(contentFragment);
            } else {
                currRetainFragmentMap.put(id, fragment);
                fragmentList.add(fragment);
            }
        }
        final int tabPos = mDynamicTabPagerAdapter.getRecoverTabPos(tagBeanList);
        mCurrAddedToFMFragmentMap = currRetainFragmentMap;
        mDynamicTabPagerAdapter.update(tagBeanList, fragmentList);
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(tabPos);
            }
        });
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
