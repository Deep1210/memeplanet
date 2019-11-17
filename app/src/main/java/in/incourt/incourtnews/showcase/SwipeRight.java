package in.incourt.incourtnews.showcase;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import in.incourt.incourtnews.helpers.IncourtTourGuideHelper;
import in.incourt.incourtnews.newslist.adapters.NewsListHorizontalViewPagerAdapter;

/**
 * Created by gurpreetsingh on 28/03/17.
 */

public class SwipeRight extends RelativeLayout {

    LayoutInflater mInflater;

    NewsListHorizontalViewPagerAdapter newsListHorizontalViewPagerAdapter;

    public SwipeRight(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();

    }

    public SwipeRight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public SwipeRight(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public void init() {
        final View view = mInflater.inflate(in.incourt.incourtnews.R.layout.showcase_swipe_right, this, true);
        view.setVisibility(GONE);
        ImageView swipehand = (ImageView) view.findViewById(in.incourt.incourtnews.R.id.swipehand);
        Animation animation = new TranslateAnimation(0, 400, 0, 0);
        animation.setDuration(4000);
        animation.setRepeatMode(Animation.INFINITE);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setFillAfter(true);
        swipehand.startAnimation(animation);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IncourtTourGuideHelper.updateStatus(IncourtTourGuideHelper.TYPE_SWIPE_RIGHT);
                getNewsListHorizontalViewPagerAdapter().getNewsListVerticalViewPager().setViewPagerSwipe(true);
                view.setVisibility(GONE);
            }
        });

    }

    public NewsListHorizontalViewPagerAdapter getNewsListHorizontalViewPagerAdapter() {
        return newsListHorizontalViewPagerAdapter;
    }

    public void setNewsListHorizontalViewPagerAdapter(NewsListHorizontalViewPagerAdapter newsListHorizontalViewPagerAdapter) {
        this.newsListHorizontalViewPagerAdapter = newsListHorizontalViewPagerAdapter;
    }
}
