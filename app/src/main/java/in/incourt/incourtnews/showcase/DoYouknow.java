package in.incourt.incourtnews.showcase;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.helpers.IncourtTourGuideHelper;
import in.incourt.incourtnews.newslist.adapters.NewsListHorizontalViewPagerAdapter;

/**
 * Created by gurpreetsingh on 28/03/17.
 */

public class DoYouknow extends RelativeLayout {

    LayoutInflater mInflater;

    public View itemView;

    NewsListHorizontalViewPagerAdapter newsListHorizontalViewPagerAdapter;

    public DoYouknow(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();

    }

    public DoYouknow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
    }

    public DoYouknow(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public void init() {
        setItemView(mInflater.inflate(in.incourt.incourtnews.R.layout.showcase_doyouknow, this, true));
        getItemView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IncourtTourGuideHelper.updateStatus(IncourtTourGuideHelper.TYPE_BOOKMARKS);
                getNewsListHorizontalViewPagerAdapter().getNewsListVerticalViewPager().setViewPagerSwipe(true);
                v.setVisibility(GONE);
            }
        });
    }

    public void updateTitle(PostsSql postsSql){
        TextView showcase_post_news_title = (TextView) getItemView().findViewById(in.incourt.incourtnews.R.id.showcase_post_news_title);
        showcase_post_news_title.setText(postsSql.getTitle());
    }

    public View getItemView() {
        return itemView;
    }

    public void setItemView(View itemView) {
        this.itemView = itemView;
    }

    public NewsListHorizontalViewPagerAdapter getNewsListHorizontalViewPagerAdapter() {
        return newsListHorizontalViewPagerAdapter;
    }

    public void setNewsListHorizontalViewPagerAdapter(NewsListHorizontalViewPagerAdapter newsListHorizontalViewPagerAdapter) {
        this.newsListHorizontalViewPagerAdapter = newsListHorizontalViewPagerAdapter;
    }
}
