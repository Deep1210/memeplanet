package in.incourt.incourtnews.newslist.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.IncourtActivity;
import in.incourt.incourtnews.anim.BottombarAnimationDown;
import in.incourt.incourtnews.anim.BottombarAnimationUp;
import in.incourt.incourtnews.anim.TopbarAnimationDown;
import in.incourt.incourtnews.anim.TopbarAnimationUp;
import in.incourt.incourtnews.core.models.AdvertisementModel;
import in.incourt.incourtnews.core.sql.CategoriesSQL;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.core.sql.TopicSQL;
import in.incourt.incourtnews.helpers.AdvertisementHelper;
import in.incourt.incourtnews.helpers.IncourtToastHelprer;
import in.incourt.incourtnews.helpers.PostListTypeHelper;
import in.incourt.incourtnews.helpers.ViewHelpers;
import in.incourt.incourtnews.helpers.WebViewHelper;
import in.incourt.incourtnews.layouts.TopbarLayout;
import in.incourt.incourtnews.newslist.pager.NewsListVerticalViewPager;
import in.incourt.incourtnews.showcase.DoYouknow;
import in.incourt.incourtnews.showcase.SwipeLeft;
import in.incourt.incourtnews.showcase.SwipeRight;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollStateListener;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

import static me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_END_SIDE;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_START_SIDE;

/**
 * Created by bhavan on 2/24/17.
 */

public class NewsListHorizontalViewPagerAdapter extends PagerAdapter {

    LayoutInflater news_list_inflater;
    View itemView;
    ViewGroup container;
    LinearLayout top_bar_layout;

    TopbarAnimationUp topbarAnimationUp;
    TopbarAnimationDown topbarAnimationDown;
    BottombarAnimationUp bottombarAnimationUp;
    BottombarAnimationDown bottombarAnimationDown;

    Thread horizontalViewPagerThread;

    long TOP_BAR_HIDE_TIME = 10000;
    long TOP_BAR_SHOW_TIME = 0;

    List<PostsSql> autoRefreshList = new ArrayList<>();
    NewsListVerticalViewPager newsListVerticalViewPager;

    TopbarLayout topbarLayout;

    DoYouknow doYouknow;

    SwipeLeft swipeLeft;

    SwipeRight swipeRight;

    WebViewHelper webViewHelper;

    boolean web_view_available = true;

    int new_record_insert = 0;

    public NewsListHorizontalViewPagerAdapter(Context context) {
        news_list_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (!ViewHelpers.getFirstRun()) {
            return 2;
        }
        else {
            return 1;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        itemView = getPageItem(position);
        container.addView(itemView);
        return itemView;
    }

    View getPageItem(int position) {

        if (position == 1) {
            itemView = news_list_inflater.inflate(in.incourt.incourtnews.R.layout.web_view_fragment, container, false);
            webViewHelper = WebViewHelper.getWebViewHelperInstance(itemView);
            return itemView;
        }

        itemView = news_list_inflater.inflate(in.incourt.incourtnews.R.layout.news_list_vetical_view_pager, container, false);
        newsListSetup(itemView);
        return itemView;
    }


    void newsListSetup(final View itemView) {
        newsListVerticalViewPager = (NewsListVerticalViewPager) itemView.findViewById(in.incourt.incourtnews.R.id.news_list_vertical_view_pager);

        top_bar_layout = (LinearLayout) itemView.findViewById(in.incourt.incourtnews.R.id.top_bar_layout);

        topbarLayout = new TopbarLayout(top_bar_layout, newsListVerticalViewPager);

        doYouknow = (DoYouknow) itemView.findViewById(in.incourt.incourtnews.R.id.doyouknow);
        doYouknow.setNewsListHorizontalViewPagerAdapter(this);

        swipeLeft = (SwipeLeft) itemView.findViewById(in.incourt.incourtnews.R.id.swipeleft);
        swipeLeft.setNewsListHorizontalViewPagerAdapter(this);

        swipeRight = (SwipeRight) itemView.findViewById(in.incourt.incourtnews.R.id.swiperight);
        swipeRight.setNewsListHorizontalViewPagerAdapter(this);

        swipeRefreshData(newsListVerticalViewPager);

        /**
         * Animation Init
         */
        topbarAnimationUp = new TopbarAnimationUp(itemView.getContext());
        topbarAnimationDown = new TopbarAnimationDown(itemView.getContext());
        bottombarAnimationUp = new BottombarAnimationUp(itemView.getContext());
        bottombarAnimationDown = new BottombarAnimationDown(itemView.getContext());
        setNewsListAdapter(null);

    }

    void swipeRefreshData(NewsListVerticalViewPager newsListVerticalViewPager) {

        VerticalOverScrollBounceEffectDecorator verticalOverScrollBounceEffectDecorator = new VerticalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(newsListVerticalViewPager));

        verticalOverScrollBounceEffectDecorator.setOverScrollStateListener(new IOverScrollStateListener() {
            @Override
            public void onOverScrollStateChange(IOverScrollDecor decor, int oldState, int newState) {
                switch (newState) {
                    case STATE_DRAG_START_SIDE:

                        break;
                    case STATE_DRAG_END_SIDE:

                        break;
                }
            }
        });
    }


    public void setNewsListAdapter(String type) {
        if (type != null) PostListTypeHelper.setFilterType(type);
        if (type == null) type = PostListTypeHelper.getFilterStatus();
        topbarLayout.setTitleText(PostListTypeHelper.getTypeTitle(type));
        if(getNewsListVerticalViewPagerAdapter() != null && getNewsListVerticalViewPagerAdapter().getNEWS_LIST_TYPE().equals(type)){
            IncourtApplication.getIncourtActivityViewPager().setCurrentItem(1, true);
            return;
        }
        newsListVerticalViewPager.setNewsAdapter(this);
        IncourtApplication.getIncourtActivityViewPager().setCurrentItem(1, true);
    }


    public void setCategoryFilterAdapter(CategoriesSQL categoriesSQL, boolean equals) {
        if(getNewsListVerticalViewPagerAdapter().getNEWS_LIST_TYPE().equals(PostListTypeHelper.CATEGORY_LIST) && equals){
            IncourtApplication.getIncourtActivityViewPager().setCurrentItem(1, true);
            return;
        }
        newsListVerticalViewPager.setCategoryNewsAdapter(categoriesSQL);
        topbarLayout.setTitleText(categoriesSQL.getName());
        IncourtApplication.getIncourtActivityViewPager().setCurrentItem(1, true);
    }

    public void setTopicFilterAdapter(TopicSQL topicSQL, boolean equals) {
        if(getNewsListVerticalViewPagerAdapter().getNEWS_LIST_TYPE().equals(PostListTypeHelper.TOPIC_LIST) && equals){
            IncourtApplication.getIncourtActivityViewPager().setCurrentItem(1, true);
            return;
        }
        newsListVerticalViewPager.setTopicNewsAdapter(topicSQL);
        topbarLayout.setTitleText(topicSQL.getName());
        IncourtApplication.getIncourtActivityViewPager().setCurrentItem(1, true);
    }


    public void setNewsSearchAdapter(List<PostsSql> postsSqlList, String search_text, int position, int page_no) {
        newsListVerticalViewPager.setSearchNewsAdapter(new ArrayList<>(postsSqlList), search_text, page_no);
        newsListVerticalViewPager.setCurrentItem(getNewsPosition(position), true );
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                newsListVerticalViewPager.onPageChangeEvent();
            }
        }, 200);
        topbarLayout.searchPageHideComponent();
        topbarLayout.setTitleText(search_text);
    }

    private int getNewsPosition(int position) {
        List<AdvertisementModel.Advertisement> advertisementList = AdvertisementHelper.getAdvertisementList();
        if(advertisementList != null && advertisementList.size() > 0){
            for(int i = 0; i < advertisementList.size() ; i++){
                String[] advertisement = advertisementList.get(i).getPositionList();
                if(advertisement.length > 0){
                    for(int y = 0; y < advertisement.length; y++){
                        if(Integer.parseInt(advertisement[y]) <= position)
                            position++;
                    }
                }
            }
        }
        return position;
    }

    public void setNewsTopicFromSearchAdapter(TopicSQL topicSQL) {
        newsListVerticalViewPager.setTopicNewsAdapter(topicSQL);
        topbarLayout.searchPageHideComponent();
        topbarLayout.setTitleText(topicSQL.getName());
    }

    public void updateAutoRefresh() {
        top_bar_layout.findViewById(in.incourt.incourtnews.R.id.top_bar_new_count).setVisibility(View.GONE);
        if(autoRefreshList != null) {
            getNewsListVerticalViewPagerAdapter().getPostsSqlList().addAll(0, autoRefreshList);
            getNewsListVerticalViewPagerAdapter().notifyDataSetChanged();
            getNewsListVerticalViewPager().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    getNewsListVerticalViewPager().setCurrentItem(0, true);
                }
            }, 100);
            autoRefreshList = new ArrayList<>();
        }
    }


    public void barShowHide(View itemView, int new_index, int last_index){

        if(itemView == null) return;

        LinearLayout bottom_layer = (LinearLayout) itemView.findViewById(R.id.buttonslayer);

        if(!getBottomBarStatus(bottom_layer) && getTopbarStatus() && new_index == last_index){
            topbarLayoutVisible();
        }
        else if(new_index <= last_index && getTopbarStatus() && getBottomBarStatus(bottom_layer)){
            topbarLayoutVisible();
            if(last_index == new_index ){
                bottomVisible(bottom_layer);
            }
        }
        else if(new_index >= last_index && (!getTopbarStatus()) && (!getBottomBarStatus(bottom_layer))){
            topbarLayoutGone();
            bottomInVisible(bottom_layer);
        }
        else if(!getTopbarStatus() && getBottomBarStatus(bottom_layer) && last_index == new_index) {
            bottomVisible(bottom_layer);
        }else if(new_index >= last_index && (!getTopbarStatus())){
            topbarLayoutGone();
        }else{
            bottomInHide(bottom_layer);
        }

    }

    private boolean getBottomBarStatus(LinearLayout bottom_layer) {
        if(bottom_layer == null) return false;
        return (bottom_layer.getVisibility() == View.GONE || bottom_layer.getVisibility() == View.INVISIBLE);
    }


    public void hasNewPosts(List<PostsSql> postsSqls) {
        if (postsSqls != null && postsSqls.size() > 0) {
            autoRefreshList = (autoRefreshList.size() < 1)? postsSqls: autoRefreshList;
            new_record_insert = autoRefreshList.size();
            TextView update_count_text_view = (TextView) top_bar_layout.findViewById(in.incourt.incourtnews.R.id.top_bar_new_count);
            update_count_text_view.setText(String.valueOf(autoRefreshList.size()) + " New");
            update_count_text_view.setVisibility(View.VISIBLE);
            IncourtToastHelprer.showRefreshComplete();
            getNewsListVerticalViewPagerAdapter().getPostLayoutPage().stopRefreshAnimation();
        }
    }


    public void buttonToTopHide() {
        top_bar_layout.findViewById(in.incourt.incourtnews.R.id.top_bar_news_to_top).setVisibility(View.INVISIBLE);
    }

    public void buttonToTopShow() {
        top_bar_layout.findViewById(in.incourt.incourtnews.R.id.top_bar_news_to_top).setVisibility(View.VISIBLE);
    }

    public void setWeb_view_available(boolean web_view_available) {
        this.web_view_available = web_view_available;
    }

    public boolean isWeb_view_available() {
        return web_view_available;
    }

    public TopbarLayout getTopbarLayout() {
        return topbarLayout;
    }

    public NewsListVerticalViewPager getNewsListVerticalViewPager() {
        return newsListVerticalViewPager;
    }

    public WebViewHelper getWebViewHelper() {
        return webViewHelper;
    }

    public NewsListVerticalViewPagerAdapter getNewsListVerticalViewPagerAdapter() {
        return getNewsListVerticalViewPager().getNewsListVerticalViewPagerAdapter();
    }

    public long getTOP_BAR_HIDE_TIME() {
        return TOP_BAR_HIDE_TIME;
    }

    public void setTOP_BAR_HIDE_TIME(long TOP_BAR_HIDE_TIME) {
        this.TOP_BAR_HIDE_TIME = TOP_BAR_HIDE_TIME;
    }


    public void createThread(){
        horizontalViewPagerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(2000);

                        if(!getTopbarStatus()) {
                            setTOP_BAR_SHOW_TIME(getTOP_BAR_SHOW_TIME() + 2000);
                        }

                        if (getTOP_BAR_HIDE_TIME() <= getTOP_BAR_SHOW_TIME() && !getTopbarStatus()) {
                            topbarLayoutGone();
                            setTOP_BAR_SHOW_TIME(0);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void topbarLayoutGone(){
        getIncourtActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                top_bar_layout.startAnimation(topbarAnimationUp.getAnimation(top_bar_layout));
                top_bar_layout.setVisibility(View.GONE);
                setTOP_BAR_SHOW_TIME(0);
            }
        });
    }

    public void topbarLayoutVisible(){
        getIncourtActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                top_bar_layout.startAnimation(topbarAnimationDown.getAnimation(top_bar_layout));
                top_bar_layout.setVisibility(View.VISIBLE);
                setTOP_BAR_SHOW_TIME(0);
            }
        });
    }

    public void bottomVisible(LinearLayout itemView){
        if(itemView == null)return;
        itemView.startAnimation(bottombarAnimationUp.getAnimation(itemView));
        itemView.setVisibility(View.VISIBLE);
        getNewsListVerticalViewPagerAdapter().getPostLayoutPage().hideReadMoreAt();
    }

    public void bottomInVisible(LinearLayout itemView){
        if(itemView == null)return;
        itemView.startAnimation(bottombarAnimationDown.getAnimation(itemView));
        itemView.setVisibility(View.INVISIBLE);
        getNewsListVerticalViewPagerAdapter().getPostLayoutPage().showReadMoreAt();
    }

    public void bottomInHide(LinearLayout itemView){
        if(itemView == null)return;
        itemView.setVisibility(View.INVISIBLE);
        getNewsListVerticalViewPagerAdapter().getPostLayoutPage().showReadMoreAt();
    }

    public IncourtActivity getIncourtActivity(){
        return (IncourtActivity) getNewsListVerticalViewPager().getContext();
    }

    public boolean getTopbarStatus() {
        if(top_bar_layout == null)return false;
        return (top_bar_layout.getVisibility() == View.GONE || top_bar_layout.getVisibility() == View.INVISIBLE);
    }

    public long getTOP_BAR_SHOW_TIME() {
        return TOP_BAR_SHOW_TIME;
    }

    public void setTOP_BAR_SHOW_TIME(long TOP_BAR_SHOW_TIME) {
        this.TOP_BAR_SHOW_TIME = TOP_BAR_SHOW_TIME;
    }

    // Showcase views ->>
    public DoYouknow getDoYouknow() {
        return doYouknow;
    }
    public void setDoYouknow(DoYouknow doYouknow) {
        this.doYouknow = doYouknow;
    }


    public SwipeLeft getSwipeLeft() {
        return swipeLeft;
    }
    public void setSwipeLeft(SwipeLeft swipeLeft) {
        this.swipeLeft = swipeLeft;
    }


    public SwipeRight getSwipeRight() {
        return swipeRight;
    }
    public void setSwipeRight(SwipeLeft swipeLeft) {
        this.swipeRight = swipeRight;
    }

    public Thread getHorizontalViewPagerThread() {
        return horizontalViewPagerThread;
    }

    public void setHorizontalViewPagerThread(Thread horizontalViewPagerThread) {
        this.horizontalViewPagerThread = horizontalViewPagerThread;
    }
}
