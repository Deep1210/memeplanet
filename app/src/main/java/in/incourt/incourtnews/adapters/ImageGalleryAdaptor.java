package in.incourt.incourtnews.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import in.incourt.incourtnews.core.sql.PostImageSql;
import in.incourt.incourtnews.helpers.ImageHelper;

import java.util.List;

/**
 * Created by bhavan on 2/10/17.
 */

public class ImageGalleryAdaptor extends PagerAdapter {

    List<PostImageSql> postImageSqls;
    Context context;

    private LayoutInflater inflater;


    public ImageGalleryAdaptor(Context context, List<PostImageSql> postImageSqls) {
        this.context = context;
        this.postImageSqls = postImageSqls;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (postImageSqls != null)
            return postImageSqls.size();
        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = inflater.inflate(in.incourt.incourtnews.R.layout.image_gallery_adapter, container, false);
        ImageHelper.loadImageWithStatus(getPostImageSqls().get(position), (ImageView) itemView.findViewById(in.incourt.incourtnews.R.id.galleryimage));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public List<PostImageSql> getPostImageSqls() {
        return postImageSqls;
    }
}
