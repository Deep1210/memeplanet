package in.incourt.incourtnews.helpers;

import android.content.SharedPreferences;
import android.view.View;

import java.util.List;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.newslist.pager.NewsListVerticalViewPager;
import in.incourt.incourtnews.showcase.DoYouknow;
import in.incourt.incourtnews.showcase.SwipeLeft;
import in.incourt.incourtnews.showcase.SwipeRight;

/**
 * Created by bhavan on 3/31/17.
 */

public class IncourtTourGuideHelper {

    public static  String TYPE_BOOKMARKS = "BOOKMARKS";
    public static  String TYPE_SWIPE_LEFT = "SWIPE_LEFT";
    public static  String TYPE_SWIPE_RIGHT = "SWIPE_RIGHT";

    public static boolean status(String type) {
        return IncourtApplication.getDefaultSharedPreferences().getBoolean(type, true);
    }

    public static boolean updateStatus(String type){
        SharedPreferences.Editor editor = IncourtApplication.getDefaultSharedPreferencesEditor();
        editor.putBoolean(type, false);
        return editor.commit();
    }

    public static void implementTask(NewsListVerticalViewPager newsListVerticalViewPager) {

        if(newsListVerticalViewPager.getPostsSqls().size() < newsListVerticalViewPager.getCurrentItem() ||
        (
            newsListVerticalViewPager.getPostsSqls().size() >  newsListVerticalViewPager.getCurrentItem() &&
            newsListVerticalViewPager.getPostsSqls().get(newsListVerticalViewPager.getCurrentItem()).hasAdvertisementModel())
        )return;

        if(IncourtTourGuideHelper.status(IncourtTourGuideHelper.TYPE_BOOKMARKS)) {
            implement(newsListVerticalViewPager, IncourtTourGuideHelper.TYPE_BOOKMARKS);
        }else if(status(TYPE_SWIPE_LEFT)){
            implement(newsListVerticalViewPager, IncourtTourGuideHelper.TYPE_SWIPE_LEFT);
        }else  if(status(TYPE_SWIPE_RIGHT)){
            implement(newsListVerticalViewPager, IncourtTourGuideHelper.TYPE_SWIPE_RIGHT);
        }

    }

    private static void implement(final NewsListVerticalViewPager newsListVerticalViewPager, final String typeSwipe) {
        newsListVerticalViewPager.setViewPagerSwipe(false);
        if(typeSwipe == TYPE_BOOKMARKS){
            implementBookMarks(newsListVerticalViewPager);
        }else if(typeSwipe == TYPE_SWIPE_RIGHT){
            implementSwipeRight(newsListVerticalViewPager);
        }else if(typeSwipe == TYPE_SWIPE_LEFT){
            implementSwipeLeft(newsListVerticalViewPager);
        }
    }

    private static void implementSwipeLeft(NewsListVerticalViewPager newsListVerticalViewPager) {
        SwipeLeft swipeLeft = newsListVerticalViewPager.getNewsListHorizontalViewPagerAdapter().getSwipeLeft();
        if(swipeLeft.getVisibility() == View.INVISIBLE || swipeLeft.getVisibility() == View.GONE){
            swipeLeft.setVisibility(View.VISIBLE);
        }
    }

    private static void implementSwipeRight(NewsListVerticalViewPager newsListVerticalViewPager) {
        SwipeRight swipeRight = newsListVerticalViewPager.getNewsListHorizontalViewPagerAdapter().getSwipeRight();
        if(swipeRight.getVisibility() == View.INVISIBLE || swipeRight.getVisibility() == View.GONE){
            swipeRight.setVisibility(View.VISIBLE);
        }
    }

    private static void implementBookMarks(NewsListVerticalViewPager newsListVerticalViewPager) {
        DoYouknow doYouknow = newsListVerticalViewPager.getNewsListHorizontalViewPagerAdapter().getDoYouknow();
        List<PostsSql> postsSqls = newsListVerticalViewPager.getNewsListVerticalViewPagerAdapter().getPostsSqlList();
        if(postsSqls == null) return;
        if(postsSqls.size() <= newsListVerticalViewPager.getCurrentItem()) return;
        if (doYouknow.getVisibility() == View.INVISIBLE || doYouknow.getVisibility() == View.GONE) {
            doYouknow.updateTitle(postsSqls.get(newsListVerticalViewPager.getCurrentItem()));
            doYouknow.setVisibility(View.VISIBLE);
        }
    }
}
