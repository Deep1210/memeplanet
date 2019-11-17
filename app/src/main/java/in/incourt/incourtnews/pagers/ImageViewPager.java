package in.incourt.incourtnews.pagers;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.eftimoff.viewpagertransformers.FlipHorizontalTransformer;

import java.util.List;

import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.IncourtActivity;
import in.incourt.incourtnews.adapters.ImageViewAdaptor;
import in.incourt.incourtnews.core.sql.PostImageSql;
import in.incourt.incourtnews.core.sql.PostsSql;

/**
 * Created by bhavan on 2/10/17.
 */

public class ImageViewPager extends ViewPager {
    ImageViewAdaptor imageViewAdaptor;

    PostsSql postsSql;

    Animation fadeIn;

    public ImageViewPager(Context context) {
        super(context);
    }

    public ImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return true;
    }

    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        super.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
    }

    public void setImageAdapter(PostsSql postsSql) {
        setPostsSql(postsSql);
        setOffscreenPageLimit(getPostImageSqls().size());
        imageViewAdaptor = new ImageViewAdaptor(this, getPostsSql());
        setAdapter(imageViewAdaptor);
        setPageTransformer(true, new FlipHorizontalTransformer());
        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_ingallery);

        setPageTransformer(true, new PageTransformer() {
            @Override
            public void transformPage(View view, float position) {

                if (position <= -1.0F || position >= 1.0F) {        // [-Infinity,-1) OR (1,+Infinity]
                    view.setAlpha(0.0F);
                    view.setVisibility(View.GONE);
                } else if (position == 0.0F) {     // [0]
                    view.setAlpha(1.0F);
                    view.setVisibility(View.VISIBLE);
                } else {

                    // Position is between [-1,1]
                    view.setAlpha(1.0F - Math.abs(position));
                    view.setTranslationX(-position * (view.getWidth() / 2));
                    view.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    public void setUpThread() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int index = 0; index < getPostImageSqls().size(); index++) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    final int finalIndex = index;

                    if (getIncourtActivity() != null) {
                        getIncourtActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setCurrentItem(finalIndex, false);
                            }
                        });
                    }

                    if (index > 0 && index == getPostImageSqls().size() - 1) {
                        showGalleryViewThread();
                    }
                }
            }
        });
        thread.setName("post_image_view");
        thread.start();

    }

    public void showGalleryViewThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    getIncourtActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            animateFadeIn();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void animateFadeIn() {

        fadeIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });

        getChildAt(getCurrentItem()).findViewById(R.id.galleryview).setVisibility(VISIBLE);
        getChildAt(getCurrentItem()).findViewById(R.id.galleryview).startAnimation(fadeIn);
    }


    IncourtActivity getIncourtActivity() {
        if (getChildAt(getCurrentItem()) != null) {
            return (IncourtActivity) getChildAt(getCurrentItem()).getContext();
        }
        return null;
    }

    public PostsSql getPostsSql() {
        return postsSql;
    }

    public void setPostsSql(PostsSql postsSql) {
        this.postsSql = postsSql;
        getPostsSql().setPostImageSqls(getPostImageSqls());
    }

    public List<PostImageSql> getPostImageSqls() {
        return getPostsSql().getPostImages(getPostsSql().getPost_id());
    }

}
