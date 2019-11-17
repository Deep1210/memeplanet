package in.incourt.incourtnews.pagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.incourt.incourtnews.tabs.NewsTab;
import in.incourt.incourtnews.tabs.TopicsTab;

public class SearchPager extends FragmentStatePagerAdapter {

    private String[] tabTitles = new String[]{"News", "Tags"};

    public SearchPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NewsTab newsTab = new NewsTab();
                return newsTab;
            case 1:
                TopicsTab topicsTab = new TopicsTab();
                return topicsTab;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}