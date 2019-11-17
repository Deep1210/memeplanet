package in.incourt.incourtnews.newslist.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.List;

import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.core.sql.TopicSQL;
import in.incourt.incourtnews.newslist.adapters.NewsListHorizontalViewPagerAdapter;
import in.incourt.incourtnews.newslist.adapters.NewsListVerticalViewPagerAdapter;

/**
 * Created by bhavan on 2/24/17.
 */

public class NewsListHorizontalViewPager extends ViewPager {

    Context context;

    DrawerLayout drawerLayout;

    public NewsListHorizontalViewPager(Context context) {
        super(context);
        this.context = context;
        setAdapter(new NewsListHorizontalViewPagerAdapter(context));
    }



    public NewsListHorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setAdapter(new NewsListHorizontalViewPagerAdapter(context));
        addPageChnage();
    }


    void addPageChnage(){

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1){
                    loadWbView();
                    if(drawerLayout != null) drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                }else {
                    setWebPageToBlank();
                    if(drawerLayout != null) drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    public void setNewsSearchAdapter(List<PostsSql> postsSqlList, String search_string, int position, final DrawerLayout drawerLayout, int page_no){
        this.drawerLayout = drawerLayout;
        getNewsListHorizontalViewPagerAdapter().setNewsSearchAdapter(postsSqlList, search_string, position, page_no);
        addPageChnage();
    }

    public void setNewsSearchTopicAdapter(TopicSQL topicSQL, final DrawerLayout drawerLayout){
        this.drawerLayout = drawerLayout;
        ((NewsListHorizontalViewPagerAdapter) getAdapter()).setNewsTopicFromSearchAdapter(topicSQL);
        addPageChnage();
    }

    public NewsListVerticalViewPagerAdapter getNewsListVerticalViewPagerAdapter(){
        NewsListHorizontalViewPagerAdapter newsListHorizontalViewPagerAdapter = ((NewsListHorizontalViewPagerAdapter) getAdapter());
        if(newsListHorizontalViewPagerAdapter != null)
            return (NewsListVerticalViewPagerAdapter) newsListHorizontalViewPagerAdapter.getNewsListVerticalViewPager().getAdapter();
        else return null;
    }

    public String getPostLink(){
        return getNewsListVerticalViewPagerAdapter().getPostsSqlList().get(getNewsListVerticalViewPager().getCurrentItem()).getUrl();
    }

    public NewsListVerticalViewPager getNewsListVerticalViewPager(){
        return getNewsListVerticalViewPagerAdapter().getNewsListVerticalViewPager();
    }

    NewsListHorizontalViewPagerAdapter getNewsListHorizontalViewPagerAdapter(){
        return (NewsListHorizontalViewPagerAdapter) getAdapter();
    }

    void loadWbView(){
        getNewsListHorizontalViewPagerAdapter().getWebViewHelper().loadUrl(getPostLink(), false);
    }

    void setWebPageToBlank(){
        getNewsListHorizontalViewPagerAdapter().getWebViewHelper().loadBlankPage();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getNewsListHorizontalViewPagerAdapter().isWeb_view_available()){
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    public void onResume() {
        if(getNewsListVerticalViewPager() != null) getNewsListVerticalViewPager().onResume();
    }
}
