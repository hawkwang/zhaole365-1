package com.example.hyan.demo3;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;

/**
 * Created by hyan on 2015/4/19.
 */
public class TabsAdapter extends FragmentPagerAdapter implements
        TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    Context context;
    TabHost tabHost;
    ViewPager viewPager;
    RadioGroup radioGroup;
    ArrayList<TabInfo> tabs = new ArrayList<TabInfo>();
    public TabsAdapter(FragmentActivity f,
                       TabHost t,
                       ViewPager v,
                       RadioGroup r) {
        super(f.getSupportFragmentManager());
        context = f;
        tabHost = t;
        viewPager = v;
        radioGroup = r;
        tabHost.setOnTabChangedListener(this);
        viewPager.setAdapter(this);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = tabs.get(position);
        return Fragment.instantiate(context, info.fragment.getName(), info.bundle);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TabWidget widget = tabHost.getTabWidget();
        int i = widget.getDescendantFocusability();
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        tabHost.setCurrentTab(position);
        widget.setDescendantFocusability(i);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String tabId) {
        int position = tabHost.getCurrentTab();
        viewPager.setCurrentItem(position);
        ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    public void addTab(TabHost.TabSpec spec, Class fragment, Bundle bundle) {
        spec.setContent(new TabFactory(context));
        String tag = spec.getTag();
        TabInfo info = new TabInfo(tag, fragment, bundle);
        tabs.add(info);
        tabHost.addTab(spec);
        notifyDataSetChanged();
    }
}

class TabInfo{
    String tag;
    Class<?> fragment;
    Bundle bundle;

    TabInfo(String t, Class<?> f, Bundle b){
        tag = t;
        fragment = f;
        bundle = b;
    }
}

class  TabFactory implements TabHost.TabContentFactory{
    private Context context;
    TabFactory(Context c){
        context = c;
    }
    @Override
    public View createTabContent(String tag) {
        View v = new View(context);
        v.setMinimumHeight(0);
        v.setMinimumWidth(0);
        return v;
    }
}
