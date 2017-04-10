package com.crazymike.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.crazymike.util.EmptyFragment;
import com.crazymike.widget.SmartFragmentStatePagerAdapter;
import com.crazymike.models.UpSideMenu;
import com.crazymike.product.list.ProductListFragment;

import java.util.List;

class UpSideMenuPagerAdapter extends SmartFragmentStatePagerAdapter {

    private List<UpSideMenu> upSideMenus;

    UpSideMenuPagerAdapter(FragmentManager fm, List<UpSideMenu> upSideMenus) {
        super(fm);
        this.upSideMenus = upSideMenus;
    }

    void notifyDataSetChanged(List<UpSideMenu> upSideMenus) {
        this.upSideMenus = upSideMenus;
        notifyDataSetChanged();
    }

    UpSideMenu getMenu(int position) {
        return upSideMenus.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return upSideMenus.get(position).getName();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getRegisteredFragment(int position) {
        return super.getRegisteredFragment(position);
    }

    @Override
    public Fragment getItem(int position) {

        String url = upSideMenus.get(position).getUrl();
        String ch = "";
        String tag = "";

        //團購首頁
        if (url.equals("https://m.devel.crazymike.tw")||url.equals("https://m.crazymike.tw")) {
            return ProductListFragment.newInstance("5");
        }


        for (String aSubString : url.split("/")) {
            if (aSubString.contains("ch-")) {
                ch = aSubString.replace("ch-", "");
            }

            if (aSubString.contains("tag-")) {
                tag = aSubString.replace("tag-", "");
            }
        }

        if (ch.equals("") && tag.equals("")) {
            return EmptyFragment.newInstance();
        } else {
            return ProductListFragment.newInstance(tag);
        }
    }

    @Override
    public int getCount() {
        return upSideMenus.size();
    }

}
