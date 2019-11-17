package in.incourt.incourtnews.pagers;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.google.gson.Gson;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.activities.IncourtActivity;
import in.incourt.incourtnews.adapters.LandingScreenViewPagerAdapter;
import in.incourt.incourtnews.core.models.PostsModel;
import in.incourt.incourtnews.core.request.PostRequestInterface;
import in.incourt.incourtnews.core.request.PostsRequest;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.helpers.AdvertisementHelper;
import in.incourt.incourtnews.helpers.ViewHelpers;
import in.incourt.incourtnews.transformation.VerticalViewPageTransformation;
import retrofit2.Call;

/**
 * Created by bhavan on 3/4/17.
 */

public class LandingScreenViewPager extends ViewPager implements PostRequestInterface.PostModels{

    Context context;

    float x = 0;
    float mStartDragX = 0;
    private static final float SWIPE_X_MIN_THRESHOLD = 50;


    public LandingScreenViewPager(Context context) {
        super(context);
        init(context);
    }

    public LandingScreenViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);


    }

    void init(Context context){
        this.context = context;
        setPageTransformer(true, new VerticalViewPageTransformation());
        setOverScrollMode(OVER_SCROLL_NEVER);
        setAdapter(new LandingScreenViewPagerAdapter(this, context));

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LandingScreenViewPagerAdapter landingScreenViewPagerAdapter = ((LandingScreenViewPagerAdapter) getAdapter());
//                        if(position == 3){
//                            Log.d("check123456789","2");
//                            if(!NetworkHelper.state() || CategoriesSQL.getList().size() <= 0){
//                                landingScreenViewPagerAdapter.setSLIDE_COUNTS(4);
//                                landingScreenViewPagerAdapter.notifyDataSetChanged();
//                            }
//                        }
                        if(position == 3){
                            new PostsRequest(LandingScreenViewPager.this).getFirstRunPosts();
                        }
                    }
                }, 1000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public boolean canScrollHorizontally(int direction)
    {
        return true;
    }

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

    private MotionEvent swapXY(MotionEvent eventAction) {
        float width = getWidth();
        float height = getHeight();
        float newX = (eventAction.getY() / height) * width;
        float newY = (eventAction.getX() / width) * height;
        eventAction.setLocation(newX, newY);
        return eventAction;
    }


    void saveNewsPost(final PostsModel postsModel, final PostsRequest postsRequest){

        AdvertisementHelper.update(postsModel);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                PostsSql.extractRequest(postsModel, true);
                if(postsRequest.getPage() <= 8){
                    postsRequest.setPage(postsRequest.getPage()+1);
                    if(postsModel != null && postsModel.getPostModelList() != null && postsModel.getPostModelList().size() > 0)
                        postsRequest.fetchNextPage();
                    else{
                        firstRunSetupCompleted();
                    }
                }else{
                    firstRunSetupCompleted();
                }
            }
        });

        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

    }

    void firstRunSetupCompleted(){
        ViewHelpers.setFirstRun(false);
        IncourtApplication.setFirstRunFetchFromServer(false);
        ((IncourtActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IncourtApplication.getIncourtActivityViewPager().setAdapter(IncourtApplication.getIncourtActivityViewPager().getAdapter());
                IncourtApplication.getIncourtActivityViewPager().setCurrentItem(0);
            }
        });
    }


    @Override
    public void onFirstRunPosts(PostsModel postModel, PostsRequest postsRequest) {
        onPostRequestSuccess(postModel, postsRequest);
    }

    @Override
    public void onPostRequestSuccess(PostsModel postsModel, PostsRequest postsRequest) {
        saveNewsPost(postsModel, postsRequest);
        AdvertisementHelper.saveAdvertisement(new Gson().toJson(postsModel.getAdvertisements()));
    }

    @Override
    public void onPostRequestNextPage(PostsModel postsModel, PostsRequest postsRequest) {
        onPostRequestSuccess(postsModel, postsRequest);
    }

    @Override
    public void onPostRequestError(Call<PostsModel> postsModel, Throwable t) {
        new PostsRequest(this).getFirstRunPosts();
    }

}
