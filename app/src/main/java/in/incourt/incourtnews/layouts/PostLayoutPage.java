package in.incourt.incourtnews.layouts;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import in.incourt.incourtnews.BuildConfig;
import in.incourt.incourtnews.FabricKpi.IncourtFabricSpeach;
import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.FacebookLoginActivity;
import in.incourt.incourtnews.activities.IncourtActivity;
import in.incourt.incourtnews.activities.IncourtLauncherActivity;
import in.incourt.incourtnews.anim.PostWiredAnimation;
import in.incourt.incourtnews.anim.RotateAnimation;
import in.incourt.incourtnews.anim.ZoomingAnimation;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.helpers.IncourtDateHelper;
import in.incourt.incourtnews.helpers.IncourtShareNewsHelper;
import in.incourt.incourtnews.helpers.IncourtTextSpeachHelper;
import in.incourt.incourtnews.helpers.IncourtToastHelprer;
import in.incourt.incourtnews.helpers.NetworkHelper;
import in.incourt.incourtnews.helpers.PostListTypeHelper;
import in.incourt.incourtnews.helpers.UserHelper;
import in.incourt.incourtnews.helpers.interfaces.TextToSpeechInterface;
import in.incourt.incourtnews.newslist.adapters.NewsListVerticalViewPagerAdapter;
import in.incourt.incourtnews.newslist.pager.NewsListVerticalViewPager;

import static in.incourt.incourtnews.helpers.IncourtShareNewsHelper.getView;

/**
 * Created by bhavan on 2/14/17.
 */

public class PostLayoutPage implements TextToSpeechInterface {


    NewsListVerticalViewPagerAdapter newsListVerticalViewPagerAdapter;
    ImageView bottom_nav_bar_bookmarks_button, wirechecked;
    ZoomingAnimation zoomingAnimation;
    PostWiredAnimation postWiredAnimation;
    public TextView newstitle;

    public PostLayoutPage(NewsListVerticalViewPagerAdapter newsListVerticalViewPagerAdapter) {
        this.newsListVerticalViewPagerAdapter = newsListVerticalViewPagerAdapter;
        this.postWiredAnimation = PostWiredAnimation.getInstance().setNewsListVerticalViewPager(getNewsListVerticalViewPager());
    }

    public void setUpLayout(final View itemView, final PostsSql postsSql) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getIncourtActivity().getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        int densityDpi = displayMetrics.densityDpi;

        int size= (int)densityDpi/10;

        int textSize = 0;
        int textHeight = 0;



        if(size>42){
            if(width<=1080){
                textSize = (int) ((width / 100)*2)-4;
                textHeight = (int) (height*35)/100;
            }else if(width>1200){
                textSize = (int) ((width / 100))+4;
                textHeight = (int) (height * 35) / 100;
            }else if(width>1400){
                textSize = (int) ((width / 100))+2;
                textHeight = (int) (height * 35) / 100;
            }


        }else{
            if(width<800) {
                width = width / 10;
                textSize = (int) (((width * 10) / 100) * 2) + 1;
                textHeight = (int) (height * 35) / 100;

            }else if(width<=1080){
                textSize = (int) ((width / 100)*2)-1;
                textHeight = (int) (height * 35) / 100;

            }else{
                textSize = (int) ((width / 100))+5;
                textHeight = (int) (height * 35) / 100;
            }
        }


        newstitle = (TextView) itemView.findViewById(in.incourt.incourtnews.R.id.post_news_title);
        newstitle.setTextSize(Integer.parseInt(String.valueOf(textSize)));

        newstitle.setText(postsSql.getTitle());


        TextView post_like_count_text = (TextView) itemView.findViewById(in.incourt.incourtnews.R.id.post_like_count_text);

        if(postsSql.getLike_count() > 0) {
            post_like_count_text.setText(postWiredAnimation.filterCount(postsSql.getLike_count()));
        }else{
            post_like_count_text.setVisibility(View.INVISIBLE);
        }

        newstitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPostBookmarkChange(postsSql, itemView);
            }
        });

        TextView newscontent = (SmartTextView) itemView.findViewById(in.incourt.incourtnews.R.id.newscontent);


        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT,textHeight);
        newscontent.setLayoutParams(rp);



       // Log.e("Size:",textSize+""+size+"  ");

        newscontent.setTextSize(Integer.parseInt(String.valueOf(textSize)));
        newscontent.setText(postsSql.getDescription());

        ImageView likeImageView = (ImageView) itemView.findViewById(in.incourt.incourtnews.R.id.likebtn);

        if (postsSql.getIs_like() == PostsSql.POST_LIKE && UserHelper.getState()) {
            likeImageView.setImageResource(in.incourt.incourtnews.R.drawable.ic_heart_n_fill);
        }

        likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPostLikeChange(postsSql);
            }
        });

        wirechecked = (ImageView) itemView.findViewById(in.incourt.incourtnews.R.id.wirechecked);

        wirechecked.setVisibility(View.GONE);

        ImageView post_title_speak_buuton = (ImageView) itemView.findViewById(in.incourt.incourtnews.R.id.post_title_speak_buuton);

        post_title_speak_buuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IncourtTextSpeachHelper.isSpeaking()) {
                    IncourtTextSpeachHelper.stop();
                    //IncourtFabricSpeach.logPostSpeechStop(postsSql);
                    onDone(getNewsListVerticalViewPager().getCurrentItem());
                } else {
                    //IncourtFabricSpeach.logPostSpeech(postsSql);
                    new IncourtTextSpeachHelper().speak(PostLayoutPage.this, postsSql.getTitle(), getNewsListVerticalViewPager().getCurrentItem());
                }
            }
        });

        bottom_nav_bar_bookmarks_button = (ImageView) itemView.findViewById(in.incourt.incourtnews.R.id.bottom_nav_bar_bookmarks_button);

        bottom_nav_bar_bookmarks_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPostBookmarkChange(postsSql, itemView);
            }
        });

        itemView.findViewById(in.incourt.incourtnews.R.id.post_page_refresh_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkHelper.state()) {
                    RotateAnimation rotateAnimation = new RotateAnimation(v.getContext());
                    v.startAnimation(rotateAnimation.getAnimation(v));
                    getNewsListVerticalViewPager().onRefreshButtonClick();
                } else {
                    IncourtToastHelprer.showUnableToRefresh();
                }
            }
        });

        itemView.findViewById(in.incourt.incourtnews.R.id.sharebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shareNewsPost(postsSql);
            }
        });


        if (postsSql.getIs_bookmarks() == PostsSql.POST_BOOKMARKS) {
            postBookmarks(itemView);
        } else {
            postBookmarksRemove(itemView);
        }

        if (postsSql.getPublish_type() == PostsSql.POST_PUBLISH_TYPE_POWERED_BY) {
            itemView.findViewById(R.id.post_news_read_more_at).setVisibility(View.GONE);
            itemView.findViewById(R.id.powered_by_main_container).setVisibility(View.VISIBLE);
            ((TextView) itemView.findViewById(in.incourt.incourtnews.R.id.powered_by_main_container_author_name)).setText(String.valueOf(" " + postsSql.getAuthor_name()));
        } else {
            ((TextView) itemView.findViewById(R.id.author_name)).setText(" " + String.valueOf(postsSql.getContributor_name() + " / "));
            ((TextView) itemView.findViewById(R.id.read_more_at_name)).setText(String.valueOf(" " + postsSql.getAuthor_name() + " / " + IncourtDateHelper.getTimeAgo(postsSql.getPubDate())));
            ((TextView) itemView.findViewById(R.id.publish_date)).setText(IncourtDateHelper.getTimeAgo(postsSql.getPubDate()));
        }

        itemView.findViewById(in.incourt.incourtnews.R.id.bottom_wired_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Bitmap bmp = null;
                    URL url = new URL(String.valueOf(getNewsImage()));
                    URLConnection conn = null;
                    conn = url.openConnection();
                    bmp = BitmapFactory.decodeStream(conn.getInputStream());
                    File f = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis() + ".jpg");
                    if(f.exists())
                        f.delete();
                    f.createNewFile();
                    Bitmap bitmap = bmp;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        itemView.findViewById(in.incourt.incourtnews.R.id.circle_animation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postWiredAnimation.setStatus(false).clearAnimation();

            }
        });

        postWiredAnimation.checkState(itemView, postsSql);

    }





    public void getPostLikeChange(PostsSql postsSql) {
        if (!UserHelper.getFacebookState()) {
            startFacebookLoginFor(PostListTypeHelper.FACEBOOK_LOGIN_FOR_POST_LIKE, postsSql);
            return;
        }
        postLikeCheck(postsSql);
    }

    public void postLikeCheck(PostsSql postsSql) {
        if (postsSql.getIs_like() == PostsSql.POST_LIKE) {
            postUnLike(postsSql);
        } else if (postsSql.getIs_like() == PostsSql.POST_UN_LIKE) {
            postLike(postsSql);
        }
    }

    public void postLike(PostsSql postsSql) {
        PostsSql.getLike(postsSql);
        postsSql.setIs_like(PostsSql.POST_LIKE);
        int new_like_count = Integer.parseInt(String.valueOf(postsSql.getLike_count() + 1));
        TextView post_like_count_text = (TextView) getCurrentViewItem().findViewById(in.incourt.incourtnews.R.id.post_like_count_text);
        post_like_count_text.setText(String.valueOf(new_like_count));
        post_like_count_text.setVisibility(View.VISIBLE);
        ImageView likebtn = (ImageView) getCurrentViewItem().findViewById(in.incourt.incourtnews.R.id.likebtn);
        likebtn.setImageResource(in.incourt.incourtnews.R.drawable.ic_heart_n_fill);
        postsSql.setLike_count(new_like_count);
        getNewsListVerticalViewPager().getPostsSqls().set(getNewsListVerticalViewPager().getCurrentItem(), postsSql);
    }

    void postUnLike(PostsSql postsSql) {
        PostsSql.getUnLike(postsSql);
        postsSql.setIs_like(PostsSql.POST_UN_LIKE);
        int new_like_count = Integer.parseInt(String.valueOf(postsSql.getLike_count() - 1));
        TextView post_like_count_text = (TextView) getCurrentViewItem().findViewById(in.incourt.incourtnews.R.id.post_like_count_text);
        post_like_count_text.setText(String.valueOf(new_like_count));
        if(new_like_count <= 0) post_like_count_text.setVisibility(View.INVISIBLE);
        ImageView likebtn = (ImageView) getCurrentViewItem().findViewById(in.incourt.incourtnews.R.id.likebtn);
        likebtn.setImageResource(in.incourt.incourtnews.R.drawable.ic_heart_n);
        postsSql.setLike_count(new_like_count);
        getNewsListVerticalViewPager().getPostsSqls().set(getNewsListVerticalViewPager().getCurrentItem(), postsSql);
    }

    void getPostBookmarkChange(PostsSql postsSql, View itemView) {
        if (postsSql.getIs_bookmarks() == PostsSql.POST_BOOKMARKS) {
            PostsSql.postBookmarksRemove(postsSql);
            postsSql.setIs_bookmarks(PostsSql.POST_BOOKMARKS_REMOVE);
            postBookmarksRemove(itemView);

        } else if (postsSql.getIs_bookmarks() == PostsSql.POST_BOOKMARKS_REMOVE) {
            PostsSql.postBookmarks(postsSql);
            postsSql.setIs_bookmarks(PostsSql.POST_BOOKMARKS);
            postBookmarks(itemView);
        }
    }



    void postBookmarks(View itemView) {
        TextView post_title_text = (TextView) itemView.findViewById(in.incourt.incourtnews.R.id.post_news_title);
        post_title_text.setTextColor(itemView.getResources().getColor(in.incourt.incourtnews.R.color.clr_blue));
        itemView.findViewById(in.incourt.incourtnews.R.id.bottom_nav_bar_bookmarks_button).setActivated(true);
    }

    void postBookmarksRemove(View itemView) {
        TextView post_title_text = (TextView) itemView.findViewById(in.incourt.incourtnews.R.id.post_news_title);
        post_title_text.setTextColor(itemView.getResources().getColor(in.incourt.incourtnews.R.color.clr_white));
        itemView.findViewById(in.incourt.incourtnews.R.id.bottom_nav_bar_bookmarks_button).setActivated(false);


    }


    public Uri getNewsImage() {

        getCurrentViewItem().setDrawingCacheEnabled(true);
        getCurrentViewItem().buildDrawingCache(true);

        if (getCurrentViewItem().findViewById(in.incourt.incourtnews.R.id.buttonslayer) != null) {
            getCurrentViewItem().findViewById(in.incourt.incourtnews.R.id.buttonslayer).setVisibility(View.INVISIBLE);
        }

        if (getCurrentViewItem().findViewById(R.id.addview_layer) != null) {
            getCurrentViewItem().findViewById(R.id.addview_layer).setVisibility(View.VISIBLE);
        }

        Bitmap bitmap = Bitmap.createScaledBitmap(getCurrentViewItem().getDrawingCache(), 720, 1280, false);

        if (getCurrentViewItem().findViewById(R.id.addview_layer) != null) {
            getCurrentViewItem().findViewById(R.id.addview_layer).setVisibility(View.INVISIBLE);
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "352511083461145.jpg");
        try {
            f.createNewFile();
            new FileOutputStream(f).write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(getIncourtActivity().getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", f);
        } else {
            return Uri.fromFile(f);
        }
    }

    public void shareNewsPost(PostsSql postsSql) {
        //IncourtShareNewsHelper.shareNews(getCurrentViewItem(), postsSql);
    }

    public void postWiredStart(PostsSql postsSql) {
        if (!UserHelper.getFacebookState()) {
            startFacebookLoginFor(PostListTypeHelper.FACEBOOK_LOGIN_FOR_POST_WIRED, postsSql);
        } else {
            postWiredAnimation.getWired(getCurrentViewItem(), postsSql);
        }
    }


    public RelativeLayout getCurrentViewItem() {
        return (RelativeLayout) getNewsListVerticalViewPager().findViewWithTag(getNewsListVerticalViewPager().getCurrentItem());
    }

    @Override
    public void onStart(final int position) {
        getIncourtActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                zoomingAnimation = new ZoomingAnimation(getTextToSpeechView(position).getContext());
                getTextToSpeechView(position).startAnimation(getZoomingAnimation().getAnimation());
            }
        });
    }

    @Override
    public void onDone(final int position) {
        getIncourtActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView imageView = getTextToSpeechView(position);
                if (imageView != null) imageView.clearAnimation();
            }
        });
    }

    @Override
    public void onError(int position) {

    }

    public ImageView getTextToSpeechView(int position) {
        if (getViewItemWithPosition(position) != null) {
            return (ImageView) getViewItemWithPosition(position).findViewById(in.incourt.incourtnews.R.id.post_title_speak_buuton);
        }
        return null;
    }

    private View getViewItemWithPosition(int position) {
        return getNewsListVerticalViewPager().findViewWithTag(position);
    }

    public void startFacebookLoginFor(int type, PostsSql postsSql) {
        IncourtLauncherActivity incourtLauncherActivity = IncourtApplication.getIncourtLauncherActivity();
        incourtLauncherActivity.setPostLayoutPage(this);
        incourtLauncherActivity.setPostsSql(postsSql);
        Intent intent = new Intent(incourtLauncherActivity, FacebookLoginActivity.class);
        IncourtApplication.getIncourtLauncherActivity().startActivityForResult(intent, type);
    }

    public void stopRefreshAnimation() {
        if (getCurrentViewItem() != null) {
            ImageView imageView = (ImageView) getCurrentViewItem().findViewById(in.incourt.incourtnews.R.id.post_page_refresh_button);
            if (imageView != null && imageView.getAnimation() != null) {
                imageView.clearAnimation();
            }
        }
    }

    public ZoomingAnimation getZoomingAnimation() {
        if (zoomingAnimation == null)
            zoomingAnimation = new ZoomingAnimation(getCurrentViewItem().getContext());
        return zoomingAnimation;
    }

    public NewsListVerticalViewPager getNewsListVerticalViewPager() {
        return newsListVerticalViewPagerAdapter.getNewsListVerticalViewPager();
    }

    IncourtActivity getIncourtActivity() {
        return (IncourtActivity) getNewsListVerticalViewPager().getContext();
    }

    public void showReadMoreAt() {
        if (getCurrentViewItem() != null) {
            if (getCurrentViewItem().findViewById(R.id.powered_by_main_container) != null && getCurrentViewItem().findViewById(R.id.powered_by_main_container).getVisibility() == View.VISIBLE) {

            } else {
                getCurrentViewItem().findViewById(in.incourt.incourtnews.R.id.post_news_filed_by).setVisibility(View.GONE);
                getCurrentViewItem().findViewById(in.incourt.incourtnews.R.id.post_news_read_more_at).setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideReadMoreAt() {
        if (getCurrentViewItem() != null) {
            if (getCurrentViewItem().findViewById(R.id.powered_by_main_container) != null && getCurrentViewItem().findViewById(R.id.powered_by_main_container).getVisibility() == View.VISIBLE) {

            } else {
                getCurrentViewItem().findViewById(in.incourt.incourtnews.R.id.post_news_filed_by).setVisibility(View.VISIBLE);
                getCurrentViewItem().findViewById(in.incourt.incourtnews.R.id.post_news_read_more_at).setVisibility(View.GONE);
            }
        }
    }
}
