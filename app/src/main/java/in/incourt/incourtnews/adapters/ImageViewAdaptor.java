package in.incourt.incourtnews.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.ImageGalleryActivity;
import in.incourt.incourtnews.activities.YouTubeActivity;
import in.incourt.incourtnews.core.sql.PostImageSql;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.helpers.ImageHelper;
import in.incourt.incourtnews.helpers.interfaces.PostsNewsImageSliderHelperInterface;
import in.incourt.incourtnews.pagers.ImageViewPager;

/**
 * Created by bhavan on 2/10/17.
 */

public class ImageViewAdaptor extends PagerAdapter implements PostsNewsImageSliderHelperInterface {

    PostsSql postsSql;

    Context context;

    ImageViewPager imageViewPager;

    Animation animFadeOut;

    View itemView;

    private LayoutInflater inflater;


    public ImageViewAdaptor(ImageViewPager imageViewPager, PostsSql postsSql) {
        this.imageViewPager = imageViewPager;
        this.context = imageViewPager.getContext();
        this.postsSql = postsSql;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (getPostImageSqls() != null)
            return getPostImageSqls().size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        itemView = inflater.inflate(in.incourt.incourtnews.R.layout.layout_image_slider, container, false);
        animFadeOut = AnimationUtils.loadAnimation(itemView.getContext(),
                in.incourt.incourtnews.R.anim.fade_outgallery);

        ImageView imageView = (ImageView) itemView.findViewById(in.incourt.incourtnews.R.id.imageView);
        itemView.findViewById(in.incourt.incourtnews.R.id.galleryview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IncourtApplication.setPostsSql(getPostsSql());
                Intent intent = new Intent(itemView.getContext(), ImageGalleryActivity.class);
                itemView.getContext().startActivity(intent);
            }
        });

        if (getPostImageSqls().get(position).getType().equals(ImageHelper.IMAGE_TYPE_VIDEO)) {
            getVideoImage((ImageView) itemView.findViewById(in.incourt.incourtnews.R.id.galleryicon), imageView, position);
        }
        else {
            getPostImages(position, imageView, (ImageView) itemView.findViewById(in.incourt.incourtnews.R.id.images_galleryicon));
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public void getVideoImage(ImageView galleryicon, ImageView imageView, final int position) {
        ImageHelper.loadImage("https://img.youtube.com/vi/" + getPostImageSqls().get(position).getImage_name() + "/0.jpg", imageView);
        galleryicon.setVisibility(View.VISIBLE);
        galleryicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YouTubeActivity.class);
                intent.putExtra("getImage_name", getPostImageSqls().get(position).getImage_name());
                context.startActivity(intent);
            }
        });
    }


    void getPostImages(int position, ImageView imageView, final ImageView gallery_icon) {
        ImageHelper.postImageLoad(getPostImageSqls().get(position).getPostImage(), imageView, this);
        if (getPostImageSqls().size() > 1 && position == 0) {
            gallery_icon.setVisibility(View.VISIBLE);
        }
    }

    public int index_image = 0;

    @Override
    public void imageLoadedComplete() {
        index_image++;
        if (index_image >= getPostImageSqls().size() && getPostImageSqls().size() > 1) {
            startAnimation();
            getImageViewPager().setUpThread();
        }
    }

    void startAnimation() {
        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getGalleryIcon().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        getGalleryIcon().startAnimation(animFadeOut);
    }

    public ImageViewPager getImageViewPager() {
        return imageViewPager;
    }

    public PostsSql getPostsSql() {
        return postsSql;
    }

    public List<PostImageSql> getPostImageSqls() {
        return getPostsSql().getPostImages(getPostsSql().getPost_id());
    }

    public ImageView getGalleryIcon() {
        return (ImageView) getImageViewPager().getChildAt(0).findViewById(R.id.images_galleryicon);
    }


}
