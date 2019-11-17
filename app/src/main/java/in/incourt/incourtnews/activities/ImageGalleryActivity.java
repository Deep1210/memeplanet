package in.incourt.incourtnews.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.adapters.ImageGalleryAdaptor;
import in.incourt.incourtnews.core.sql.PostsSql;

import butterknife.BindView;
import butterknife.OnClick;

public class ImageGalleryActivity extends IncourtActivity {

    @BindView(R.id.crossimg)
    ImageView crossimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_gallery);
        PostsSql postsSql = IncourtApplication.getPostsSql();
        setupGalleryView(postsSql);
        IncourtApplication.setPostsSql(null);
    }

    private void setupGalleryView(PostsSql postsSql) {

        setupViewPager(postsSql);

        changeTotalTextView(1, postsSql.getPostImages(postsSql.getPost_id()).size());

        ((TextView) findViewById(R.id.gallery_view_news_title)).setText(postsSql.getTitle());
    }

    void setupViewPager(final PostsSql postsSql){

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ImageGalleryAdaptor(this, postsSql.getPostImages()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTotalTextView(position+1, postsSql.getPostImages(postsSql.getPost_id()).size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    void changeTotalTextView(int page_no, int total_size){
        ((TextView) findViewById(R.id.gallery_view_total)).setText( String.valueOf(page_no) + "/" + String.valueOf(total_size));
    }

    @OnClick(R.id.crossimg)
    void crossimgclICK() {
        onBackPressed();
    }

}
