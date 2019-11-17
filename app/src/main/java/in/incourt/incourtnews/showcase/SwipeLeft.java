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

public class SwipeLeft extends RelativeLayout {

    LayoutInflater mInflater;

    NewsListHorizontalViewPagerAdapter newsListHorizontalViewPagerAdapter;

    public SwipeLeft(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public SwipeLeft(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public SwipeLeft(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public void init() {
        ((SwipeLeft) mInflater.inflate(in.incourt.incourtnews.R.layout.showcase_swipe_left, this, true)).startAnimation01();
    }


    public void startAnimation01() {
        setVisibility(GONE);
        ImageView swipehand = (ImageView) findViewById(in.incourt.incourtnews.R.id.swipehand);
        Animation animation = new TranslateAnimation(400, 0, 0, 0);
        animation.setDuration(4000);
        animation.setRepeatMode(Animation.INFINITE);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setFillAfter(true);
        swipehand.startAnimation(animation);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IncourtTourGuideHelper.updateStatus(IncourtTourGuideHelper.TYPE_SWIPE_LEFT);
                getNewsListHorizontalViewPagerAdapter().getNewsListVerticalViewPager().setViewPagerSwipe(true);
                v.setVisibility(GONE);
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
