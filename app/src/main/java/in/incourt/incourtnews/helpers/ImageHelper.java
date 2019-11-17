package in.incourt.incourtnews.helpers;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.core.sql.PostImageSql;
import in.incourt.incourtnews.helpers.interfaces.PostsNewsImageSliderHelperInterface;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by bhavan on 1/20/17.
 */

public class ImageHelper {

    static String HD_IMAGE_ON_KEY = "HD_IMAGE_ON_KEY";
    static boolean HD_IMAGE_ON = true;
    static boolean HD_IMAGE_OFF = false;

    public static String IMAGE_TYPE_PHOTO = "1";

    public static String IMAGE_TYPE_VIDEO = "2";

    public static PostsNewsImageSliderHelperInterface postsNewsImageSliderHelperInterface;

    public static void loadImage(String imgurl, ImageView imageView) {
        ImageLoader.getInstance().displayImage(imgurl, imageView, getDisplayImage(), null, null);
    }


    public static void postImageLoad(String imgurl, ImageView imageView, final PostsNewsImageSliderHelperInterface postsNewsImageSliderHelperInterface) {

        ImageLoader.getInstance().displayImage(imgurl, imageView, getDisplayImage(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                postsNewsImageSliderHelperInterface.imageLoadedComplete();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    public static DisplayImageOptions getDisplayImage() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(in.incourt.incourtnews.R.drawable.logo_background)
                .showImageForEmptyUri(in.incourt.incourtnews.R.drawable.logo_background)
                .showImageOnFail(in.incourt.incourtnews.R.drawable.logo_background)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    public static void hdImageOn() {
        hdImageChange(HD_IMAGE_ON);
    }

    public static void hdImageOff() {
        hdImageChange(HD_IMAGE_OFF);
    }

    public static void hdImageChange(boolean status) {
        SharedPreferences.Editor shEditor = IncourtApplication.getDefaultSharedPreferencesEditor();
        shEditor.putBoolean(HD_IMAGE_ON_KEY, status);
        shEditor.commit();
    }

    public static boolean getHdImageStatus() {
        return IncourtApplication.getDefaultSharedPreferences().getBoolean(HD_IMAGE_ON_KEY, false);
    }


    public static void loadImageWithStatus(PostImageSql postImageSql, ImageView galleryimage) {
        if(getHdImageStatus()){
            loadImage(postImageSql.getImage_name_hd(), galleryimage);
        }
        else{
            loadImage(postImageSql.getImage_name(), galleryimage);
        }
    }
}
