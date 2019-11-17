package in.incourt.incourtnews.newslist.pager;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import in.incourt.incourtnews.FabricKpi.IncourtFabricContentView;
import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.core.models.PostsModel;
import in.incourt.incourtnews.core.request.PostRequestInterface;
import in.incourt.incourtnews.core.request.PostsRequest;
import in.incourt.incourtnews.core.sql.CategoriesSQL;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.core.sql.TopicSQL;
import in.incourt.incourtnews.helpers.AdvertisementHelper;
import in.incourt.incourtnews.helpers.IncourtToastHelprer;
import in.incourt.incourtnews.helpers.IncourtTourGuideHelper;
import in.incourt.incourtnews.helpers.NetworkHelper;
import in.incourt.incourtnews.helpers.PostListTypeHelper;
import in.incourt.incourtnews.newslist.adapters.NewsListHorizontalViewPagerAdapter;
import in.incourt.incourtnews.newslist.adapters.NewsListVerticalViewPagerAdapter;
import in.incourt.incourtnews.pagers.ImageViewPager;
import in.incourt.incourtnews.threads.AutoUpdateInterface;
import in.incourt.incourtnews.threads.AutoUpdateSyncRecords;
import in.incourt.incourtnews.transformation.VerticalViewPageTransformation;
import retrofit2.Call;

/**
 * Created by bhavan on 2/24/17.
 */

public class NewsListVerticalViewPager extends ViewPager implements PostRequestInterface.PostModels, AutoUpdateInterface, AutoUpdateSyncRecords {

    int LAST_PAGE_INDEX_VERTICAL_LIST_VIEW_PAGER = 0 ;
    float x = 0;
    float mStartDragX = 0;
    private static final float SWIPE_X_MIN_THRESHOLD = 50;

    Context context;

    boolean viewPagerSwipe = true;

    NewsListHorizontalViewPagerAdapter newsListHorizontalViewPagerAdapter;

    float oldX = 0, sens = 5;

    boolean advertisement_load = true;

    PostsRequest postsRequest;

    boolean is_from_manual_search = false;

    public NewsListVerticalViewPager(Context context) {
        super(context);
        init(context);

    }

    public NewsListVerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    void init(Context context) {
        this.context = context;
        setPageTransformer(true, new VerticalViewPageTransformation());
        setOverScrollMode(OVER_SCROLL_NEVER);
        setupPageChangeListener();

    }


    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */


    private MotionEvent swapXY(MotionEvent eventAction) {
        float width = getWidth();
        float height = getHeight();
        float newX = (eventAction.getY() / height) * width;
        float newY = (eventAction.getX() / width) * height;
        //Log.e("New Location: ",newX+" "+newY);
        eventAction.setLocation(newX, newY);
        detectClickEvent(eventAction);
        return eventAction;
    }

    boolean detectClickEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = motionEvent.getX();

                return true;
            case MotionEvent.ACTION_UP:


                if (Math.abs(oldX - motionEvent.getX()) < sens) {
                    viewPagerClick(getCurrentItem(), true);
                }else {

                    oldX = 0;

                }


                return true;
        }


        return false;
    }


    void viewPagerClick(int position, boolean flag){
        if(getPostSql() == null) return;
        if(getPostSql().hasAdvertisementModel() && flag && getNewsListVerticalViewPagerAdapter().getAdvertisementHelper() != null){
            getNewsListVerticalViewPagerAdapter().getAdvertisementHelper().onClickAdvertisement(getPostSql().getAdvertisementModel());
        }else {
            getNewsListHorizontalViewPagerAdapter().barShowHide(getCurrentItemView(position), position, getLAST_PAGE_INDEX_VERTICAL_LIST_VIEW_PAGER());
        }
    }

   /* @Override public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean intercepted = super.onInterceptTouchEvent(swapXY(motionEvent));

        swapXY(motionEvent);
        return intercepted;

    }*/

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = super.onInterceptTouchEvent(swapXY(event));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            switch (event.getAction() & MotionEventCompat.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    break;
            }
        }
        swapXY(event); // return touch coordinates to original reference frame for any child views
        return intercepted;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (getAdapter() != null) {
                if (getCurrentItem() >= 0 || getCurrentItem() < getAdapter().getCount()) {
                    swapXY(event);
                    final int action = event.getAction();
                    switch (action & MotionEventCompat.ACTION_MASK) {
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            mStartDragX = event.getX();
                            if (x < mStartDragX
                                    && (mStartDragX - x > SWIPE_X_MIN_THRESHOLD)
                                    && getCurrentItem() > 0) {
                                Log.i("VerticalViewPager", "down " + x + " : " + mStartDragX + " : " + (mStartDragX - x));
                                setCurrentItem(getCurrentItem() - 1, true);
                                return true;
                            } else if (x > mStartDragX
                                    && (x - mStartDragX > SWIPE_X_MIN_THRESHOLD)
                                    && getCurrentItem() < getAdapter().getCount() - 1) {
                                Log.i("VerticalViewPager", "up " + x + " : " + mStartDragX + " : " + (x - mStartDragX));
                                mStartDragX = 0;
                                setCurrentItem(getCurrentItem() + 1, true);
                                return true;
                            }
                            break;
                    }
                } else {
                    mStartDragX = 0;
                }
                swapXY(event);

                return super.onTouchEvent(swapXY(event));
            }
            return false;
        }else{
            return super.onTouchEvent(swapXY(event));

        }
    }

    public void setNewsAdapter(NewsListHorizontalViewPagerAdapter newsListHorizontalViewPagerAdapter) {
        this.newsListHorizontalViewPagerAdapter = newsListHorizontalViewPagerAdapter;
        setAdapter(new NewsListVerticalViewPagerAdapter(this, context, null));
    }

    public void setCategoryNewsAdapter(CategoriesSQL categoriesSQL) {
        getNewsListVerticalViewPagerAdapter().setNEWS_LIST_TYPE(PostListTypeHelper.CATEGORY_LIST);
        setupRestCall(getPostsRequest().setCategory_id(categoriesSQL.getCategory_id()));
    }

    public void setTopicNewsAdapter(TopicSQL topicSQL) {
        getNewsListVerticalViewPagerAdapter().setNEWS_LIST_TYPE(PostListTypeHelper.TOPIC_LIST);
        setupRestCall(getPostsRequest().setTopic_id(topicSQL.getTopic_id()));
    }

    public void setSearchNewsAdapter(List<PostsSql> postsSqlList, String search_text, int page_no) {
        getNewsListVerticalViewPagerAdapter().setPostsSqlList(postsSqlList);
        getNewsListVerticalViewPagerAdapter().setNEWS_LIST_TYPE(PostListTypeHelper.STATE_SEARCH_LIST);
        onRequestChange(getPostsRequest().setPage(page_no).setTitle(search_text));
        setAdapter(getNewsListVerticalViewPagerAdapter());
    }

    public void setupRestCall(PostsRequest postsRequest) {
        if (!NetworkHelper.state()) {
            noNetworkState(getNewsListVerticalViewPagerAdapter());
            return;
        }
        setNewsAdapterToLoadingState(PostListTypeHelper.STATE_LOADING);
        postsRequest.fetchFirstPage();
        onRequestChange(postsRequest);
    }

    private void setNewsAdapterToLoadingState(String type) {
        getNewsListVerticalViewPagerAdapter().setPostsSqlList(new ArrayList<PostsSql>());
        getNewsListVerticalViewPagerAdapter().setNEWS_LIST_STATE(type);
        getNewsListVerticalViewPagerAdapter().notifyDataSetChanged();
    }

    void noNetworkState(NewsListVerticalViewPagerAdapter newsListVerticalViewPagerAdapter) {
        newsListVerticalViewPagerAdapter.setPostsSqlList(new ArrayList<PostsSql>());
        newsListVerticalViewPagerAdapter.setNEWS_LIST_STATE(PostListTypeHelper.STATE_NO_NETWORK);
        setAdapter(newsListVerticalViewPagerAdapter);
    }


    @Override
    public void setAdapter(PagerAdapter adapter) {

        super.setAdapter(adapter);

        if(!getListStatusForTopBar() || getNewsListHorizontalViewPagerAdapter().getTopbarStatus()) {

            if(getNewsListHorizontalViewPagerAdapter().getHorizontalViewPagerThread() == null) {
                getNewsListHorizontalViewPagerAdapter().createThread();
            }

            if(getNewsListHorizontalViewPagerAdapter().getHorizontalViewPagerThread() != null && !getNewsListHorizontalViewPagerAdapter().getHorizontalViewPagerThread().isAlive()){
                getNewsListHorizontalViewPagerAdapter().getHorizontalViewPagerThread().start();
            }
            if(getNewsListHorizontalViewPagerAdapter().getTopbarStatus()) {
                getNewsListHorizontalViewPagerAdapter().topbarLayoutVisible();
            }

            if(!advertisement_load) advertisement_load = true;
        }

        checkAutoUpdateThread();

    }

    private void checkAutoUpdateThread() {
        if(checkIfAutoUpdateImplement()) {
            IncourtApplication.getAutoUpdateThread().registerAutoUpdate(this);
            IncourtApplication.getAutoUpdateThread().registerAutoUpdateSyncRecords(this);
        }
    }

    public void onRequestChange(PostsRequest postsRequest){
        getNewsListVerticalViewPagerAdapter().adapterChange(postsRequest);
    }

    public boolean checkIfAutoUpdateImplement(){
        boolean ret = false;
        switch (getNewsListVerticalViewPagerAdapter().getNEWS_LIST_TYPE()){
            case PostListTypeHelper.ALL_NEWS:
            case PostListTypeHelper.MY_FEED:
            case PostListTypeHelper.TOP_STORIES:
            case PostListTypeHelper.TRENDING_NEWS:
                ret = true;
        }
        return ret;
    }

    private boolean getListStatusForTopBar() {
        return (
                getNewsListVerticalViewPagerAdapter().getNEWS_LIST_STATE() == PostListTypeHelper.STATE_LOADING
                ||
                getNewsListVerticalViewPagerAdapter().getNEWS_LIST_STATE() == PostListTypeHelper.STATE_NO_NETWORK
            );
    }

    public NewsListHorizontalViewPagerAdapter getNewsListHorizontalViewPagerAdapter() {
        return newsListHorizontalViewPagerAdapter;
    }


    void setupPageChangeListener() {

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position > 0) {
                    newsListHorizontalViewPagerAdapter.buttonToTopShow();
                } else {
                    newsListHorizontalViewPagerAdapter.buttonToTopHide();
                }
            }

            @Override
            public void onPageSelected(int position) {
                viewPagerClick(position, false);
                onPageChangeEvent();
                if(position >=2 || position <=4) {
                    if(getNewsListVerticalViewPagerAdapter().getPostsSqlList() != null && getNewsListVerticalViewPagerAdapter().getPostsSqlList().size() > position)
                        IncourtTourGuideHelper.implementTask(NewsListVerticalViewPager.this);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

    }

    public void onPageChangeEvent() {

        if(getPostsSqls().size() > getCurrentItem()){
            setupPostImageAdapter();
            PostsSql.unReadNewsPost(getNewsListVerticalViewPagerAdapter().getPostsSqlList().get(getCurrentItem()));
        }

        if ((getCurrentItem() % 4) == 0) {
            showUnreadCount();
        }

        addNewsPostOnEnd();

        checkWebViewAvailable();

       // IncourtFabricContentView.postView(getPostSql());

    }

    public void showUnreadCount(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int count = PostsSql.getUnReadCount();
                if (count > 0) {
                    IncourtApplication.getIncourtLauncherActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            IncourtToastHelprer.showUnreadToast(count);
                        }
                    });
                }
            }
        }).start();
    }

    private void checkWebViewAvailable() {
        if(!(getPostsSqls().size() > getCurrentItem())){
            getNewsListHorizontalViewPagerAdapter().setWeb_view_available(false);
        }
        else if (getPostsSqls().get(getCurrentItem()).hasAdvertisementModel()) {
            getNewsListHorizontalViewPagerAdapter().setWeb_view_available(false);
        } else if(getNewsListHorizontalViewPagerAdapter().isWeb_view_available() == false){
            getNewsListHorizontalViewPagerAdapter().setWeb_view_available(true);
        }
    }


    private void addNewsPostOnEnd() {
        if ((getPostsSqls().size() - 5) <= getCurrentItem() && getCurrentItem() > getLAST_PAGE_INDEX_VERTICAL_LIST_VIEW_PAGER()) {
            if (NetworkHelper.state()) {
                //updateFromServer();
            }
        }
        setLAST_PAGE_INDEX_VERTICAL_LIST_VIEW_PAGER(getCurrentItem());
    }

    public NewsListVerticalViewPagerAdapter getNewsListVerticalViewPagerAdapter() {
        return (NewsListVerticalViewPagerAdapter) getAdapter();
    }

    public void setupPostImageAdapter() {
        if(getCurrentItemView(getCurrentItem()) == null) return;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;


        int imageHeight = (int) (height*50)/100;
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        ImageViewPager imageViewPager = (ImageViewPager) getCurrentItemView(getCurrentItem()).findViewById(R.id.content_main_image_container);



        if (imageViewPager == null) return;
        imageViewPager.setLayoutParams(rp);
        imageViewPager.setImageAdapter(getPostSql());
        imageViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                transformPagec(page, position);
            }
        });
    }

    private PostsSql getPostSql() {
        if(getPostsSqls().size() <= 0 || !(getPostsSqls().size() > getCurrentItem())) return null;
        PostsSql postsSql = getPostsSqls().get(getCurrentItem());
        postsSql.setPostImageSqls(getPostsSqls().get(getCurrentItem()).getPostImages(postsSql.getPost_id()));
        return postsSql;
    }

    public View getCurrentItemView(int position) {
        return findViewWithTag(position);
    }

    public List<PostsSql> getPostsSqls() {
        return getNewsListVerticalViewPagerAdapter().getPostsSqlList();
    }


    public void transformPagec(View view, float position) {

        if (position <= -1.0F || position >= 1.0F) {        // [-Infinity,-1) OR (1,+Infinity]
            view.setAlpha(0.0F);
            view.setVisibility(View.GONE);
        } else if (position == 0.0F) {     // [0]
            view.setAlpha(1.0F);
            view.setVisibility(View.VISIBLE);
        } else {

            // Position is between [-1,1]
            view.setAlpha(1.0F - Math.abs(position));
            view.setTranslationX(-position * (view.getWidth() / 3));
            view.setVisibility(View.VISIBLE);
        }
    }


    public void updateCurrentRecord(int position, PostsSql postsSql) {
        int index = getCurrentItem();
        if (position > 0) index = position;
        getPostsSqls().set(index, postsSql);
    }

    public PostsRequest getPostsRequest() {
        return postsRequest = new PostsRequest(this);
    }

    public int getLAST_PAGE_INDEX_VERTICAL_LIST_VIEW_PAGER() {
        return LAST_PAGE_INDEX_VERTICAL_LIST_VIEW_PAGER;
    }

    public void setLAST_PAGE_INDEX_VERTICAL_LIST_VIEW_PAGER(int LAST_PAGE_INDEX_VERTICAL_LIST_VIEW_PAGER) {
        this.LAST_PAGE_INDEX_VERTICAL_LIST_VIEW_PAGER = LAST_PAGE_INDEX_VERTICAL_LIST_VIEW_PAGER;
    }

    public boolean isViewPagerSwipe() {
        return viewPagerSwipe;
    }

    public void setViewPagerSwipe(boolean viewPagerSwipe) {
        this.viewPagerSwipe = viewPagerSwipe;
    }

    @Override
    public void onFirstRunPosts(PostsModel postModel, PostsRequest postsRequest) {
       getNewsListVerticalViewPagerAdapter().getFirstRunApp(postModel);
    }

    @Override
    public void onPostRequestSuccess(PostsModel postsModel, PostsRequest postsRequest) {
        getNewsListVerticalViewPagerAdapter().updatedPostsList(postsModel);
    }

    @Override
    public void onPostRequestNextPage(PostsModel postsModel, PostsRequest postsRequest) {
        getNewsListVerticalViewPagerAdapter().appendNextPage(postsModel);
    }

    @Override
    public void onPostRequestError(Call<PostsModel> postsModel, Throwable t) {
        noNetworkState(new NewsListVerticalViewPagerAdapter(NewsListVerticalViewPager.this, context, getNewsListVerticalViewPagerAdapter().getNEWS_LIST_TYPE()));
    }


    public void getFirstRunPosts() {
        getPostsRequest().getFirstRunPosts();
    }

    public void noRecordFoundOnLocal(String news_list_type) {
        getPostsRequest().getPostsWithCategoryIds(getCategoriesIdsType(news_list_type));
    }

    private String[] getCategoriesIdsType(String type) {
        int category_type = 0;
        if(type.equals(PostListTypeHelper.TRENDING_NEWS)) category_type = CategoriesSQL.CATEGORY_TYPE_TRENDING;
        if(type.equals(PostListTypeHelper.TOP_STORIES)) category_type = CategoriesSQL.CATEGORY_TYPE_TOP_STORIES;
        return PostsSql.getTypeCategoryIds(category_type).split(",");
    }


    @Override
    public void onUpdateSuccess(PostsModel postsModel) {
        if(postsModel != null && checkIfAutoUpdateImplement()){
            PostsSql.extractRequest(postsModel, true);
            getNewsListHorizontalViewPagerAdapter().hasNewPosts(PostsSql.parseData(postsModel, true));
            AdvertisementHelper.update(postsModel);
            PostsSql.deleteOldRecords(postsModel);
        }
        stopRefreshAnimation(postsModel);
    }

    @Override
    public void onSyncSuccess(PostsModel postsModel) {
        PostsSql.updateExistsPost(postsModel, true, getPostsSqls());
    }

    public void invalidateCurrentView() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                onPageChangeEvent();
            }
        }, 100);
    }

    public void onRefreshButtonClick() {
        if(!is_from_manual_search){
            IncourtApplication.getAutoUpdateThread().forceExecuteingUpdate();
            is_from_manual_search = true;
        }
    }

    public void stopRefreshAnimation(PostsModel postsModel ){
        if(getCurrentItemView(getCurrentItem()) == null)return;
        ImageView imageView = (ImageView) getCurrentItemView(getCurrentItem()).findViewById(R.id.post_page_refresh_button);
        if(imageView != null) imageView.clearAnimation();
        if(is_from_manual_search && postsModel.getPostModelList() != null && postsModel.getPostModelList().size() > 0) {
            IncourtToastHelprer.showRefreshComplete();
        }else if(is_from_manual_search) {
            IncourtToastHelprer.showAlreadyUptodate();
            is_from_manual_search = false;
        }
    }


    public void onResume() {
        checkAutoUpdateThread();
    }

}