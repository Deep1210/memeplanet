package in.incourt.incourtnews.layouts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.CustomizeFeedActivity;
import in.incourt.incourtnews.helpers.IncourtStringHelper;
import in.incourt.incourtnews.helpers.IncourtToastHelprer;
import in.incourt.incourtnews.helpers.MyInterestHelper;
import in.incourt.incourtnews.helpers.PostListTypeHelper;
import in.incourt.incourtnews.newslist.pager.NewsListVerticalViewPager;

/**
 * Created by bhavan on 2/28/17.
 */

public class TopbarLayout implements View.OnClickListener {

    LinearLayout linearLayout;
    NewsListVerticalViewPager newsListVerticalViewPager;
    SharedPreferences sharedPreferences;

    public TopbarLayout(LinearLayout linearLayout, NewsListVerticalViewPager newsListVerticalViewPager) {
        this.linearLayout = linearLayout;
        this.newsListVerticalViewPager = newsListVerticalViewPager;
        init();
    }

    void init() {

       linearLayout.findViewById(R.id.top_bar_back_button).setOnClickListener(this);
//        linearLayout.findViewById(R.id.top_bar_icon_search).setOnClickListener(this);
//        linearLayout.findViewById(R.id.top_bar_new_count).setOnClickListener(this);
        linearLayout.findViewById(R.id.top_bar_news_to_top).setOnClickListener(this);

//
//        linearLayout.findViewById(R.id.top_bar_my_feed_button).setOnClickListener(this);
//        if(PostListTypeHelper.getMyFeedStatus()) setActivatedButtonToFalse(linearLayout.findViewById(R.id.top_bar_my_feed_button), false);
//
//        linearLayout.findViewById(R.id.top_bar_all_news_button).setOnClickListener(this);
//        if(PostListTypeHelper.getAllNewsStatus()) setActivatedButtonToFalse(linearLayout.findViewById(R.id.top_bar_all_news_button), false);
//
//        linearLayout.findViewById(R.id.top_bar_top_stories_button).setOnClickListener(this);
//        if(PostListTypeHelper.getTopStoriesStatus()) setActivatedButtonToFalse(linearLayout.findViewById(R.id.top_bar_top_stories_button), false);
//
//        linearLayout.findViewById(R.id.top_bar_trending_button).setOnClickListener(this);
//        if(PostListTypeHelper.getTrendingNewsStatus()) setActivatedButtonToFalse(linearLayout.findViewById(R.id.top_bar_trending_button), false);
//
//        linearLayout.findViewById(R.id.top_bar_icon_search).setOnClickListener(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(IncourtApplication.getIncourtContext());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_bar_back_button:
                IncourtApplication.getIncourtActivityViewPager().setCurrentItem(0, true);
                view.setActivated(true);
                break;

//            case R.id.top_bar_my_feed_button:
//                if(MyInterestHelper.getRaw()!=null)
//                    changeFilterList(PostListTypeHelper.MY_FEED, view);
//                else {
//                        IncourtToastHelprer.showToast("Please customize your Feed first");
//                        IncourtApplication.getIncourtLauncherActivity().startActivity(new Intent(IncourtApplication.getIncourtLauncherActivity(), CustomizeFeedActivity.class));
//                }
//                break;
//
//            case R.id.top_bar_all_news_button:
//                changeFilterList(PostListTypeHelper.ALL_NEWS, view);
//                break;
//
//            case R.id.top_bar_top_stories_button:
//                changeFilterList(PostListTypeHelper.TOP_STORIES, view);
//                break;
//
//            case R.id.top_bar_trending_button:
//                changeFilterList(PostListTypeHelper.TRENDING_NEWS, view);
//                break;
//
//            case R.id.top_bar_icon_search:
//                openSearchPage();
//                break;
//
            case R.id.top_bar_news_to_top:
                newsListVerticalViewPager.setCurrentItem(0, true);
                break;
//
//            case R.id.top_bar_new_count:
//                newsListVerticalViewPager.getNewsListHorizontalViewPagerAdapter().updateAutoRefresh();
//                break;
        }
    }

    public void changeFilterList(String type, View view) {
        newsListVerticalViewPager.getNewsListHorizontalViewPagerAdapter().setNewsListAdapter(type);
        setActivatedButtonToFalse(view, true);
    }

    public void setActivatedButtonToFalse(@Nullable View view, @Nullable boolean flag) {
//        linearLayout.findViewById(R.id.top_bar_my_feed_button).setActivated(false);
//        linearLayout.findViewById(R.id.top_bar_all_news_button).setActivated(false);
//        linearLayout.findViewById(R.id.top_bar_top_stories_button).setActivated(false);
//        linearLayout.findViewById(R.id.top_bar_trending_button).setActivated(false);

        int default_color = fetchColor(getLinearLayout().getContext(), R.attr.day_gray_text);

//        ((Button) linearLayout.findViewById(R.id.top_bar_my_feed_button)).setTextColor(default_color);
//        ((Button) linearLayout.findViewById(R.id.top_bar_all_news_button)).setTextColor(default_color);
//        ((Button) linearLayout.findViewById(R.id.top_bar_top_stories_button)).setTextColor(default_color);
//        ((Button) linearLayout.findViewById(R.id.top_bar_trending_button)).setTextColor(default_color);
        if (flag) {
            CategoryPageLayout.setSeletedTagId("");
            IncourtApplication.getIncourtActivityViewPager().getCategoryPageLayout().setActivatedButtonForFalse(null, false);
        }

        if(view != null){
            view.setActivated(true);
            int active_color = fetchColor(getLinearLayout().getContext(), R.attr.top_bar_active_color);
            ((TextView) view).setTextColor(active_color);
        }
    }
    public void setTitleText(String title) {

    }
    void openSearchPage() {
        IncourtApplication.getIncourtActivityViewPager().getCategoryPageLayout().openSearchActivity();
    }
    public void searchPageHideComponent() {
        linearLayout.findViewById(R.id.top_bar_back_button).setVisibility(View.INVISIBLE);
//        linearLayout.findViewById(R.id.top_bar_icon_search).setVisibility(View.INVISIBLE);
//        linearLayout.findViewById(R.id.top_bar_new_count).setVisibility(View.INVISIBLE);
        linearLayout.findViewById(R.id.top_bar_news_to_top).setVisibility(View.INVISIBLE);
//        linearLayout.findViewById(R.id.top_bar_button_container).setVisibility(View.GONE);
//        linearLayout.findViewById(R.id.top_bar_icon_search).setVisibility(View.INVISIBLE);
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public static int fetchColor(Context c, int id ) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = c.obtainStyledAttributes( typedValue.data, new int[]{ id } );
        int color = a.getColor( 0, 0 );
        a.recycle();
        return color;
    }
}
