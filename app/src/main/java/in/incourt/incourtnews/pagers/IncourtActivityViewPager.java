package in.incourt.incourtnews.pagers;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.activities.IncourtLauncherActivity;
import in.incourt.incourtnews.helpers.ViewHelpers;
import in.incourt.incourtnews.layouts.CategoryPageLayout;
import in.incourt.incourtnews.newslist.pager.NewsListHorizontalViewPager;

/**
 * Created by bhavan on 2/24/17.
 */

public class IncourtActivityViewPager extends ViewPager {


    CategoryPageLayout categoryPageLayout;
    NewsListHorizontalViewPager newsListHorizontalViewPager;

    public IncourtActivityViewPager(Context context) {
        super(context);
        setCustomAdapter(context);
    }

    public IncourtActivityViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAdapter(context);
    }

    void setCustomAdapter(Context context) {
        IncourtApplication.setIncourtActivityViewPager(this);
        super.setAdapter(new IncourtActivityViewPagerAdapter(context));
        setCurrentItem(1);
        setOffscreenPageLimit(1);
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

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

    @Override
    public boolean canScrollHorizontally(int direction) {
        return (ViewHelpers.getFirstRun())? false: true;
    }

    public class IncourtActivityViewPagerAdapter extends PagerAdapter{

        Context context;
        View itemView;
        LayoutInflater layoutInflater;

        public IncourtActivityViewPagerAdapter(Context context) {
            this.context = context;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            setOffscreenPageLimit(1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (LinearLayout) object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if(position == 0){
                itemView = layoutInflater.inflate(in.incourt.incourtnews.R.layout.category_page_layout, container, false);
                categoryPageLayout = new CategoryPageLayout((IncourtLauncherActivity) context, itemView);
                setupAutoRefreshThread(itemView);
            }else{
                if(ViewHelpers.getFirstRun()){
                    itemView = layoutInflater.inflate(in.incourt.incourtnews.R.layout.landing_screen_view_pager, container, false);
                }else {
                    itemView = layoutInflater.inflate(in.incourt.incourtnews.R.layout.news_list_horizontel_view_pager, container, false);
                    newsListHorizontalViewPager = (NewsListHorizontalViewPager) itemView.findViewById(in.incourt.incourtnews.R.id.news_list_horizontel_view_pager);
                }
            }
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

    }

    private void setupAutoRefreshThread(View itemView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(50000);
                        if(getCurrentItem() != 0) {
                            ((IncourtLauncherActivity) getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    categoryPageLayout.reRedraw();
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public CategoryPageLayout getCategoryPageLayout() {
        return categoryPageLayout;
    }

    public void setCategoryPageLayout(CategoryPageLayout categoryPageLayout) {
        this.categoryPageLayout = categoryPageLayout;
    }

    public NewsListHorizontalViewPager getNewsListHorizontalViewPager() {
        return newsListHorizontalViewPager;
    }

    public void onResume(){
        if(getNewsListHorizontalViewPager() != null) getNewsListHorizontalViewPager().onResume();
    }

}
